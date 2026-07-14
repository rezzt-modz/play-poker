package dev.rezzt.playpoker.blackjack.block;

import dev.rezzt.playpoker.ModBlockEntities;
import dev.rezzt.playpoker.blackjack.BlackjackCard;
import dev.rezzt.playpoker.blackjack.BlackjackGame;
import dev.rezzt.playpoker.blackjack.BlackjackInteractionHandler;
import dev.rezzt.playpoker.blackjack.GameCallbacks;
import dev.rezzt.playpoker.blackjack.PlayerSeat;
import dev.rezzt.playpoker.blackjack.visual.BlackjackTableVisuals;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Server-side block entity that binds the pure-logic {@link BlackjackGame} to the Minecraft world.
 *
 * <p>It implements {@link GameCallbacks} so the game can produce chat, sound, particle and visual
 * effects without depending on the game class itself.</p>
 */
public class BlackjackTableBlockEntity extends BlockEntity implements GameCallbacks {

    private static final double PLAYER_FRONT_OFFSET = 0.70;
    private static final double DEALER_FRONT_OFFSET = 0.90;
    private static final double[] SIDE_OFFSETS = {-0.35, 0.0, 0.35};

    private final BlackjackGame game = new BlackjackGame();
    private final BlackjackTableVisuals visuals = new BlackjackTableVisuals();
    private boolean needsVisualSync;

    public BlackjackTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLACKJACK_TABLE.get(), pos, state);
    }

    public BlackjackGame getGame() {
        return game;
    }

    public void tickServer() {
        if (level == null || level.isClientSide()) {
            return;
        }
        visuals.ensureSpawned(level, worldPosition, getBlockState().getValue(BlackjackTableBlock.FACING));
        registerInteractions();
        if (needsVisualSync) {
            rebuildVisuals();
            needsVisualSync = false;
        }
        syncBetDisplays();
        game.tick(new Random(level.getRandom().nextLong()), this);
    }

    private void registerInteractions() {
        for (int seat = 0; seat < 3; seat++) {
            int group = seat + 1;
            for (BlackjackTableVisuals.InteractionType type : BlackjackTableVisuals.InteractionType.values()) {
                UUID uuid = visuals.getInteraction(seat, type);
                net.minecraft.world.entity.Entity entity = uuid == null ? null : level.getServer().getLevel(level.dimension()).getEntity(uuid);
                if (entity instanceof net.minecraft.world.entity.Interaction interaction) {
                    BlackjackInteractionHandler.register(interaction, worldPosition, seat, type);
                }
            }
        }
    }

    private void syncBetDisplays() {
        if (level == null) return;
        for (int i = BlackjackGame.MIN_PLAYER_GROUP; i <= BlackjackGame.MAX_PLAYER_GROUP; i++) {
            visuals.updateBetItem(level, i, game.seat(i).bet());
        }
    }

    private void rebuildVisuals() {
        if (level == null) return;
        for (int i = BlackjackGame.MIN_PLAYER_GROUP; i <= BlackjackGame.MAX_PLAYER_GROUP; i++) {
            PlayerSeat seat = game.seat(i);
            List<BlackjackCard> cards = seat.hand().cards();
            for (int slot = 0; slot < cards.size(); slot++) {
                visuals.updatePlayerCard(level, i, slot, cards.get(slot), true);
            }
            visuals.updatePlayerHandValue(level, i, seat.isBusted() ? "BUST!" : String.valueOf(seat.hand().value()));
            visuals.updateBetItem(level, i, seat.bet());
        }
        List<BlackjackCard> dealerCards = game.dealerHand().cards();
        boolean hideHole = game.isHoleCardHidden();
        for (int slot = 0; slot < dealerCards.size(); slot++) {
            visuals.updateDealerCard(level, slot, dealerCards.get(slot), slot == 1 && hideHole);
        }
        if (!hideHole || dealerCards.size() > 2) {
            visuals.updateDealerHandValue(level, String.valueOf(game.dealerHand().value()));
        }
    }

    public void handleInteraction(Player player, int seat, BlackjackTableVisuals.InteractionType type, boolean rightClick) {
        if (level == null || level.isClientSide()) {
            return;
        }
        int group = seat + 1;
        Random random = new Random(level.getRandom().nextLong());

        if (rightClick) {
            switch (type) {
                case BET -> handleBetClick(player, group);
                case READY -> {
                    if (game.toggleReady(group, this, random)) {
                        visuals.updateBetItem(level, group, game.seat(group).bet());
                    }
                }
                case HIT -> game.hit(group, this, random);
                case STAND -> game.stand(group, this, random);
            }
        } else {
            // Left click
            if (type == BlackjackTableVisuals.InteractionType.BET) {
                if (game.removeBet(group, this)) {
                    visuals.updateBetItem(level, group, ItemStack.EMPTY);
                }
            } else {
                sendInGameError();
            }
        }
        setChanged();
    }

    private void handleBetClick(Player player, int group) {
        if (game.phase() != BlackjackGame.Phase.IDLE) {
            sendInGameError();
            return;
        }
        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (held.isEmpty()) {
            chat("chat.playpoker.blackjack.must_hold_item");
            return;
        }
        ItemStack bet = held.copy();
        bet.setCount(Math.min(held.getCount(), 64));
        int previousBetCount = game.seat(group).hasBet() ? game.seat(group).bet().getCount() : 0;
        if (game.placeBet(group, bet, this)) {
            int added = game.seat(group).bet().getCount() - previousBetCount;
            held.shrink(added);
            if (held.isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }
            visuals.updateBetItem(level, group, game.seat(group).bet());
        }
    }

    /**
     * Fallback used when the player right-clicks the block itself rather than an interaction entity.
     */
    public void onUse(Player player, BlockHitResult hit) {
        if (level == null || level.isClientSide()) {
            return;
        }
        int group = BlackjackTableBlock.getGroupFromHit(getBlockState(), worldPosition, hit.getLocation());
        if (group < 0) {
            sendInGameError();
            return;
        }
        handleInteraction(player, group - 1,
                game.phase() == BlackjackGame.Phase.IDLE ? BlackjackTableVisuals.InteractionType.READY : BlackjackTableVisuals.InteractionType.HIT,
                true);
    }

    /**
     * Fallback used when the player left-clicks the block itself.
     */
    public void onAttack(Player player) {
        if (level == null || level.isClientSide()) {
            return;
        }
        int group = resolveGroupFromPlayerPosition(player.position());
        if (group < 0 || game.phase() != BlackjackGame.Phase.IDLE) {
            sendInGameError();
            return;
        }
        handleInteraction(player, group - 1, BlackjackTableVisuals.InteractionType.BET, false);
    }

    /**
     * Called when the block is broken or replaced. Returns any pending bets and kills visuals.
     */
    public void onRemove() {
        if (level == null || level.isClientSide()) {
            return;
        }
        boolean inGame = game.phase() != BlackjackGame.Phase.IDLE;
        if (inGame) {
            chat("chat.playpoker.blackjack.table_broken");
        } else {
            for (int i = BlackjackGame.MIN_PLAYER_GROUP; i <= BlackjackGame.MAX_PLAYER_GROUP; i++) {
                if (game.seat(i).hasBet()) {
                    spawnItemAt(getGroupPos(i), game.seat(i).bet());
                }
            }
        }
        BlackjackInteractionHandler.clearForTable(worldPosition);
        visuals.removeAll(level);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Game", game.save());
        tag.put("Visuals", visuals.save());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        game.load(tag.getCompound("Game"));
        visuals.load(tag.getCompound("Visuals"));
        needsVisualSync = true;
    }

    // -------------------------------------------------------------------------
    // GameCallbacks implementation
    // -------------------------------------------------------------------------

    @Override
    public void dealerChat(String message) {
        if (level == null) return;
        Component text = Component.literal("").append(Component.literal("<").withStyle(net.minecraft.ChatFormatting.GRAY))
                .append(Component.literal("The Dealer").withStyle(net.minecraft.ChatFormatting.WHITE))
                .append(Component.literal("> ").withStyle(net.minecraft.ChatFormatting.GRAY))
                .append(Component.literal(message).withStyle(net.minecraft.ChatFormatting.GRAY));
        for (Player player : nearbyPlayers(worldPosition, 8.0)) {
            player.sendSystemMessage(text);
        }
    }

    @Override
    public void chat(String translationKey) {
        if (level == null) return;
        for (Player player : nearbyPlayers(worldPosition, 8.0)) {
            player.sendSystemMessage(Component.translatable(translationKey));
        }
    }

    @Override
    public void chatToGroup(int group, String translationKey) {
        if (level == null) return;
        for (Player player : nearbyPlayers(getGroupPos(group), 4.0)) {
            player.sendSystemMessage(Component.translatable(translationKey));
        }
    }

    @Override
    public void sendInGameError() {
        chat("chat.playpoker.blackjack.in_game_error");
    }

    @Override
    public void playSound(int group, String soundId, float volume, float pitch) {
        if (level == null) return;
        Vec3 pos = group == BlackjackGame.DEALER_GROUP ? Vec3.atCenterOf(worldPosition) : getGroupPos(group);
        level.playSound(null, BlockPos.containing(pos), sound(soundId), SoundSource.BLOCKS, volume, pitch);
    }

    @Override
    public void spawnParticle(int group, String particleId) {
        if (!(level instanceof ServerLevel server)) return;
        Vec3 pos = group == BlackjackGame.DEALER_GROUP ? Vec3.atCenterOf(worldPosition) : getGroupPos(group);
        ParticleOptions options = parseParticle(particleId);
        server.sendParticles(options, pos.x, pos.y + 0.8, pos.z, 8, 0.2, 0.2, 0.2, 0.05);
    }

    @Override
    public void returnBet(int group, ItemStack bet) {
        if (level == null) return;
        spawnItemAt(getGroupPos(group), bet);
    }

    @Override
    public void loseBet(int group, ItemStack bet) {
        // Bet is forfeited; nothing is spawned.
    }

    @Override
    public void payoutWin(int group, ItemStack bet) {
        if (level == null) return;
        ItemStack payout = bet.copy();
        payout.setCount(bet.getCount() * 2);
        spawnItemAt(getGroupPos(group), payout);
    }

    @Override
    public void payoutBlackjack(int group, ItemStack bet) {
        if (level == null) return;
        ItemStack payout = bet.copy();
        payout.setCount(bet.getCount() * 3);
        spawnItemAt(getGroupPos(group), payout);
    }

    @Override
    public void showPlayerCard(int group, int slot, BlackjackCard card) {
        visuals.updatePlayerCard(level, group, slot, card, true);
        visuals.updatePlayerHandValue(level, group, String.valueOf(game.seat(group).hand().value()));
    }

    @Override
    public void showDealerCard(int slot, BlackjackCard card, boolean hidden) {
        visuals.updateDealerCard(level, slot, card, hidden);
        if (!hidden) {
            visuals.updateDealerHandValue(level, String.valueOf(game.dealerHand().value()));
        }
    }

    @Override
    public void revealDealerHoleCard() {
        if (game.dealerHand().cards().size() > 1) {
            BlackjackCard card = game.dealerHand().cards().get(1);
            visuals.revealDealerHoleCard(level, card);
            visuals.updateDealerHandValue(level, String.valueOf(game.dealerHand().value()));
        }
    }

    @Override
    public void showBust(int group) {
        visuals.showBust(level, group);
    }

    @Override
    public void broadcastDealerBust(int dealerValue) {
        dealerChat("Dealer busts with " + dealerValue + "!");
        visuals.updateDealerHandValue(level, String.valueOf(dealerValue));
    }

    @Override
    public void resetVisuals() {
        visuals.ensureSpawned(level, worldPosition, getBlockState().getValue(BlackjackTableBlock.FACING));
        visuals.resetAllCards(level);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Vec3 getGroupPos(int group) {
        BlockPos p = worldPosition;
        Direction facing = getBlockState().getValue(BlackjackTableBlock.FACING);
        Vec3 center = new Vec3(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5);
        Vec3 front = new Vec3(facing.getStepX(), 0, facing.getStepZ());
        Vec3 side = new Vec3(-front.z, 0, front.x);

        double frontOffset = group == BlackjackGame.DEALER_GROUP ? DEALER_FRONT_OFFSET : PLAYER_FRONT_OFFSET;
        double sideOffset = group == BlackjackGame.DEALER_GROUP ? 0.0 : SIDE_OFFSETS[group - 1];
        return center.add(front.scale(frontOffset)).add(side.scale(sideOffset));
    }

    /**
     * Resolves the closest seat group to a player position, used when the precise hit location is
     * unavailable (left-click attack).
     */
    private int resolveGroupFromPlayerPosition(Vec3 playerPos) {
        BlockPos p = worldPosition;
        Direction facing = getBlockState().getValue(BlackjackTableBlock.FACING);
        Vec3 center = new Vec3(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5);
        Vec3 toPlayer = playerPos.subtract(center);
        Vec3 front = new Vec3(facing.getStepX(), 0, facing.getStepZ());
        Vec3 side = new Vec3(front.z, 0, -front.x); // right of the player facing the table

        // Reject players behind the table.
        if (toPlayer.dot(front) < 0) {
            return -1;
        }
        double sideCoord = toPlayer.dot(side);
        if (sideCoord < -0.55 || sideCoord > 0.55) {
            return -1;
        }
        if (sideCoord < -1.0 / 6.0) {
            return 1; // player's left
        }
        if (sideCoord > 1.0 / 6.0) {
            return 3; // player's right
        }
        return 2;
    }

    private void spawnItemAt(Vec3 pos, ItemStack stack) {
        if (level == null || stack.isEmpty()) return;
        ItemEntity entity = new ItemEntity(level, pos.x, pos.y + 0.5, pos.z, stack.copy());
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
    }

    private List<Player> nearbyPlayers(Vec3 pos, double radius) {
        if (level == null) return Collections.emptyList();
        AABB box = new AABB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).inflate(radius);
        return level.getEntitiesOfClass(Player.class, box);
    }

    private List<Player> nearbyPlayers(BlockPos pos, double radius) {
        return nearbyPlayers(Vec3.atCenterOf(pos), radius);
    }

    private static SoundEvent sound(String id) {
        ResourceLocation location = ResourceLocation.tryParse(id);
        if (location == null) {
            location = ResourceLocation.tryParse("minecraft:block.note_block.pling");
        }
        return SoundEvent.createVariableRangeEvent(location);
    }

    private static ParticleOptions parseParticle(String id) {
        return switch (id) {
            case "composter" -> ParticleTypes.COMPOSTER;
            case "white_smoke" -> ParticleTypes.LARGE_SMOKE;
            case "dust_red" -> new DustParticleOptions(new Vector3f(1.0f, 0.0f, 0.0f), 1.0f);
            case "dust_green" -> new DustParticleOptions(new Vector3f(0.0f, 1.0f, 0.0f), 1.0f);
            case "electric_spark" -> ParticleTypes.ELECTRIC_SPARK;
            case "totem_of_undying" -> ParticleTypes.TOTEM_OF_UNDYING;
            default -> ParticleTypes.SMOKE;
        };
    }
}
