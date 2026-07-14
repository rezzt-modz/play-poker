package dev.rezzt.playpoker.blackjack;

import dev.rezzt.playpoker.blackjack.block.BlackjackTableBlockEntity;
import dev.rezzt.playpoker.blackjack.visual.BlackjackTableVisuals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Routes Interaction entity clicks to the owning blackjack table.
 *
 * <p>The 777 datapack uses interaction entities for Hit, Stand, Bet and Ready. We replicate that
 * by maintaining an in-memory registry of interaction UUIDs to their table position, seat and
 * action type.</p>
 */
public final class BlackjackInteractionHandler {

    private static final Map<UUID, InteractionInfo> REGISTRY = new HashMap<>();

    public static void register(Interaction interaction, BlockPos tablePos, int seat,
                                BlackjackTableVisuals.InteractionType type) {
        REGISTRY.put(interaction.getUUID(), new InteractionInfo(tablePos, seat, type));
    }

    public static void unregister(UUID uuid) {
        REGISTRY.remove(uuid);
    }

    public static void clearForTable(BlockPos tablePos) {
        REGISTRY.values().removeIf(info -> info.tablePos.equals(tablePos));
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        Entity target = event.getTarget();
        if (!(target instanceof Interaction interaction)) {
            return;
        }
        InteractionInfo info = REGISTRY.get(interaction.getUUID());
        if (info == null) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.CONSUME);

        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) {
            return;
        }

        if (level.getBlockEntity(info.tablePos) instanceof BlackjackTableBlockEntity table) {
            table.handleInteraction(player, info.seat, info.type, true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }
        if (!(level.getBlockEntity(event.getPos()) instanceof BlackjackTableBlockEntity table)) {
            return;
        }

        BlockHitResult hit = event.getHitVec();
        if (hit == null) {
            return;
        }

        int group = dev.rezzt.playpoker.blackjack.block.BlackjackTableBlock.getGroupFromHit(
                table.getBlockState(), event.getPos(), hit.getLocation());
        if (group < 0) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.CONSUME);

        Player player = event.getEntity();
        table.handleInteraction(player, group - 1,
                table.getGame().phase() == dev.rezzt.playpoker.blackjack.BlackjackGame.Phase.IDLE
                        ? BlackjackTableVisuals.InteractionType.READY
                        : BlackjackTableVisuals.InteractionType.HIT,
                true);
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Entity target = event.getTarget();
        if (!(target instanceof Interaction interaction)) {
            return;
        }
        InteractionInfo info = REGISTRY.get(interaction.getUUID());
        if (info == null) {
            return;
        }

        event.setCanceled(true);

        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) {
            return;
        }

        if (level.getBlockEntity(info.tablePos) instanceof BlackjackTableBlockEntity table) {
            table.handleInteraction(player, info.seat, info.type, false);
        }
    }

    public record InteractionInfo(BlockPos tablePos, int seat, BlackjackTableVisuals.InteractionType type) {
    }
}
