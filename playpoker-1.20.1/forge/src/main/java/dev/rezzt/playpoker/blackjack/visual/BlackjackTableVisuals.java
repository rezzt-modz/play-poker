package dev.rezzt.playpoker.blackjack.visual;

import dev.rezzt.playpoker.blackjack.BlackjackCard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.UUID;

/**
 * Manages the visual entities of a blackjack table, matching the 777 datapack layout.
 *
 * <p>Entities are spawned relative to the block center and its {@code FACING} direction.</p>
 */
public final class BlackjackTableVisuals {

    private static final int PLAYER_CARD_COUNT = 7;
    private static final int SEAT_COUNT = 3;

    // ----- Layout constants from the 777 datapack (local coordinates: right, up, forward) -----
    // Table model
    private static final Vector3f TABLE_MODEL_LOCAL = new Vector3f(0.0f, 0.5f, 0.0f);

    // Dealer (feet on the ground, one block behind the table)
    private static final Vector3f DEALER_LOCAL = new Vector3f(0.0f, -0.5f, -1.0f);

    // Player cards (seat index 0 = group 1, 1 = group 2, 2 = group 3)
    private static final Vector3f[][] PLAYER_CARD_LOCALS = new Vector3f[SEAT_COUNT][PLAYER_CARD_COUNT];
    private static final Vector3f[] PLAYER_HAND_VALUE_LOCALS = new Vector3f[SEAT_COUNT];
    private static final Vector3f[] PLAYER_BET_LOCALS = new Vector3f[SEAT_COUNT];
    private static final Vector3f[] PLAYER_HIT_LOCALS = new Vector3f[SEAT_COUNT];
    private static final Vector3f[] PLAYER_STAND_LOCALS = new Vector3f[SEAT_COUNT];
    private static final Vector3f[] PLAYER_BET_INTERACTION_LOCALS = new Vector3f[SEAT_COUNT];
    private static final Vector3f[] PLAYER_READY_LOCALS = new Vector3f[SEAT_COUNT];

    // Dealer cards
    private static final Vector3f[] DEALER_CARD_LOCALS = new Vector3f[PLAYER_CARD_COUNT];
    private static final Vector3f DEALER_HAND_VALUE_LOCAL = new Vector3f(0.0f, 1.14f, -0.12f);

    static {
        // Group 1 (left) - datapack uses ^-1.005 ^ ^ offset for the interaction set
        float g1Right = -1.005f;
        PLAYER_CARD_LOCALS[0] = new Vector3f[]{
                new Vector3f(g1Right - 0.26f, 1.126f, 0.12f),
                new Vector3f(g1Right - 0.072f, 1.126f, 0.12f),
                new Vector3f(g1Right + 0.115f, 1.126f, 0.12f),
                new Vector3f(g1Right + 0.3035f, 1.126f, 0.12f),
                new Vector3f(g1Right - 0.165f, 1.126f, -0.1f),
                new Vector3f(g1Right + 0.02f, 1.126f, -0.1f),
                new Vector3f(g1Right + 0.21f, 1.126f, -0.1f)
        };
        PLAYER_HAND_VALUE_LOCALS[0] = new Vector3f(g1Right + 0.02f, 1.126f, 0.19f);
        PLAYER_BET_LOCALS[0] = new Vector3f(g1Right + 0.25f, 1.135f, 0.325f);
        PLAYER_HIT_LOCALS[0] = new Vector3f(g1Right - 0.06f, 1.0f, 0.375f);
        PLAYER_STAND_LOCALS[0] = new Vector3f(g1Right - 0.25f, 1.0f, 0.375f);
        PLAYER_BET_INTERACTION_LOCALS[0] = PLAYER_BET_LOCALS[0];
        PLAYER_READY_LOCALS[0] = new Vector3f(g1Right - 0.16f, 1.025f, 0.225f);

        // Group 2 (center)
        float g2Right = 0.0f;
        PLAYER_CARD_LOCALS[1] = new Vector3f[]{
                new Vector3f(g2Right - 0.26f, 1.126f, 0.12f),
                new Vector3f(g2Right - 0.072f, 1.126f, 0.12f),
                new Vector3f(g2Right + 0.115f, 1.126f, 0.12f),
                new Vector3f(g2Right + 0.3035f, 1.126f, 0.12f),
                new Vector3f(g2Right - 0.165f, 1.126f, -0.1f),
                new Vector3f(g2Right + 0.02f, 1.126f, -0.1f),
                new Vector3f(g2Right + 0.21f, 1.126f, -0.1f)
        };
        PLAYER_HAND_VALUE_LOCALS[1] = new Vector3f(g2Right + 0.02f, 1.126f, 0.19f);
        PLAYER_BET_LOCALS[1] = new Vector3f(g2Right + 0.25f, 1.135f, 0.325f);
        PLAYER_HIT_LOCALS[1] = new Vector3f(g2Right - 0.06f, 1.0f, 0.375f);
        PLAYER_STAND_LOCALS[1] = new Vector3f(g2Right - 0.25f, 1.0f, 0.375f);
        PLAYER_BET_INTERACTION_LOCALS[1] = PLAYER_BET_LOCALS[1];
        PLAYER_READY_LOCALS[1] = new Vector3f(g2Right - 0.16f, 1.025f, 0.225f);

        // Group 3 (right) - datapack uses ^0.94 ^ ^ offset
        float g3Right = 0.94f;
        PLAYER_CARD_LOCALS[2] = new Vector3f[]{
                new Vector3f(g3Right - 0.26f, 1.126f, 0.12f),
                new Vector3f(g3Right - 0.072f, 1.126f, 0.12f),
                new Vector3f(g3Right + 0.115f, 1.126f, 0.12f),
                new Vector3f(g3Right + 0.3035f, 1.126f, 0.12f),
                new Vector3f(g3Right - 0.165f, 1.126f, -0.1f),
                new Vector3f(g3Right + 0.02f, 1.126f, -0.1f),
                new Vector3f(g3Right + 0.21f, 1.126f, -0.1f)
        };
        PLAYER_HAND_VALUE_LOCALS[2] = new Vector3f(g3Right + 0.02f, 1.126f, 0.19f);
        PLAYER_BET_LOCALS[2] = new Vector3f(g3Right + 0.25f, 1.135f, 0.325f);
        PLAYER_HIT_LOCALS[2] = new Vector3f(g3Right - 0.06f, 1.0f, 0.375f);
        PLAYER_STAND_LOCALS[2] = new Vector3f(g3Right - 0.25f, 1.0f, 0.375f);
        PLAYER_BET_INTERACTION_LOCALS[2] = PLAYER_BET_LOCALS[2];
        PLAYER_READY_LOCALS[2] = new Vector3f(g3Right - 0.16f, 1.025f, 0.225f);

        // Dealer cards
        DEALER_CARD_LOCALS[0] = new Vector3f(-0.385f, 1.13f, -0.238f);
        DEALER_CARD_LOCALS[1] = new Vector3f(-0.2f, 1.13f, -0.238f);
        DEALER_CARD_LOCALS[2] = new Vector3f(-0.01f, 1.13f, -0.238f);
        DEALER_CARD_LOCALS[3] = new Vector3f(0.18f, 1.13f, -0.238f);
        DEALER_CARD_LOCALS[4] = new Vector3f(0.37f, 1.13f, -0.238f);
        DEALER_CARD_LOCALS[5] = new Vector3f(0.76f, 1.13f, -0.238f);
        DEALER_CARD_LOCALS[6] = new Vector3f(0.75f, 1.13f, -0.1f);
    }

    private static final int PLAYER_CARD_BG = -2082760;
    private static final int DEALER_CARD_BG = -13927937;
    private static final int HAND_VALUE_BG = 0;

    private static final Quaternionf IDENTITY_ROTATION = new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f);

    private static final Quaternionf CARD_LEFT_ROTATION = new Quaternionf(0.0f, 2.0f, 0.0f, 1.0f).normalize();
    private static final Quaternionf CARD_RIGHT_ROTATION = new Quaternionf(0.75f, 0.0f, 0.0f, 1.0f).normalize();
    private static final Vector3f CARD_TRANSLATION = new Vector3f(0.0f, 0.6f, 0.2f);
    private static final Vector3f CARD_SCALE_HIDDEN = new Vector3f(0.0f, 0.0f, 0.0f);
    private static final Vector3f CARD_SCALE_SHOWN = new Vector3f(0.7f, 0.7f, 0.7f);
    private static final Vector3f CARD_SCALE_EXTRA = new Vector3f(0.5f, 0.5f, 0.5f);

    private static final Quaternionf HAND_VALUE_LEFT_ROTATION = IDENTITY_ROTATION;
    private static final Quaternionf HAND_VALUE_RIGHT_ROTATION = IDENTITY_ROTATION;
    private static final Vector3f HAND_VALUE_TRANSLATION = new Vector3f(0.0f, 0.0f, 0.0f);
    private static final Vector3f PLAYER_HAND_VALUE_SCALE = new Vector3f(0.3f, 0.3f, 0.3f);
    private static final Vector3f DEALER_HAND_VALUE_SCALE = new Vector3f(0.4f, 0.4f, 0.4f);

    private static final Quaternionf BET_ITEM_LEFT_ROTATION = new Quaternionf(0.0f, 10000.0f, 0.0f, 1.0f).normalize();
    private static final Quaternionf BET_ITEM_RIGHT_ROTATION = IDENTITY_ROTATION;
    private static final Vector3f BET_ITEM_TRANSLATION = new Vector3f(0.0f, 0.0f, -0.0075f);
    private static final Vector3f BET_ITEM_SCALE = new Vector3f(0.35f, 0.35f, 0.35f);

    private static final float PLAYER_CARD_PITCH = -90.0f;
    private static final float PLAYER_EXTRA_CARD_PITCH = -15.0f;
    private static final float PLAYER_VALUE_PITCH = -90.0f;
    private static final float DEALER_CARD_PITCH = -75.0f;
    private static final float DEALER_VALUE_PITCH = -80.0f;
    private static final float BET_ITEM_PITCH = -90.0f;

    private static final VillagerProfession[] PROFESSIONS = {
            VillagerProfession.ARMORER,
            VillagerProfession.BUTCHER,
            VillagerProfession.CARTOGRAPHER,
            VillagerProfession.CLERIC,
            VillagerProfession.FARMER,
            VillagerProfession.FISHERMAN,
            VillagerProfession.FLETCHER,
            VillagerProfession.LEATHERWORKER,
            VillagerProfession.LIBRARIAN,
            VillagerProfession.MASON,
            VillagerProfession.NITWIT,
            VillagerProfession.SHEPHERD,
            VillagerProfession.TOOLSMITH,
            VillagerProfession.WEAPONSMITH
    };

    private BlockPos pos;
    private Direction facing;

    private UUID tableModel;
    private UUID dealer;
    private final UUID[] dealerCards = new UUID[PLAYER_CARD_COUNT];
    private UUID dealerValue;
    private final UUID[][] playerCards = new UUID[SEAT_COUNT][PLAYER_CARD_COUNT];
    private final UUID[] playerValues = new UUID[SEAT_COUNT];
    private final UUID[] playerBets = new UUID[SEAT_COUNT];
    private final UUID[][] interactions = new UUID[SEAT_COUNT][4]; // 0=hit, 1=stand, 2=bet, 3=ready

    public BlackjackTableVisuals() {
        clearUuids();
    }

    public BlockPos getPos() {
        return pos;
    }

    public Direction getFacing() {
        return facing;
    }

    public UUID getInteraction(int seat, InteractionType type) {
        return interactions[seat][type.ordinal()];
    }

    public enum InteractionType {
        HIT, STAND, BET, READY
    }

    /**
     * Spawns every visual entity. Existing tracked entities are removed first to avoid duplicates.
     */
    public void spawnAll(Level level, BlockPos pos, Direction facing) {
        this.pos = pos;
        this.facing = facing;
        removeAll(level);

        Vec3 center = blockCenter(pos);

        this.tableModel = spawnTableModel(level, center, facing).getUUID();
        this.dealer = spawnDealer(level, center, facing).getUUID();

        for (int slot = 0; slot < PLAYER_CARD_COUNT; slot++) {
            Vec3 cardPos = toWorldPos(center, facing, DEALER_CARD_LOCALS[slot]);
            Display.TextDisplay display = spawnText(level, cardPos, "-");
            initCardDisplay(display, DEALER_CARD_BG, false);
            setEntityPitch(display, DEALER_CARD_PITCH);
            dealerCards[slot] = display.getUUID();
        }

        Vec3 dealerValuePos = toWorldPos(center, facing, DEALER_HAND_VALUE_LOCAL);
        Display.TextDisplay dealerValueDisplay = spawnText(level, dealerValuePos, "");
        initHandValueDisplay(dealerValueDisplay, DEALER_HAND_VALUE_SCALE);
        setEntityPitch(dealerValueDisplay, DEALER_VALUE_PITCH);
        this.dealerValue = dealerValueDisplay.getUUID();

        for (int seat = 0; seat < SEAT_COUNT; seat++) {
            for (int slot = 0; slot < PLAYER_CARD_COUNT; slot++) {
                Vec3 cardPos = toWorldPos(center, facing, PLAYER_CARD_LOCALS[seat][slot]);
                Display.TextDisplay display = spawnText(level, cardPos, "-");
                initCardDisplay(display, PLAYER_CARD_BG, false);
                setEntityPitch(display, slot >= 4 ? PLAYER_EXTRA_CARD_PITCH : PLAYER_CARD_PITCH);
                playerCards[seat][slot] = display.getUUID();
            }

            Vec3 valuePos = toWorldPos(center, facing, PLAYER_HAND_VALUE_LOCALS[seat]);
            Display.TextDisplay valueDisplay = spawnText(level, valuePos, "");
            initHandValueDisplay(valueDisplay, PLAYER_HAND_VALUE_SCALE);
            setEntityPitch(valueDisplay, PLAYER_VALUE_PITCH);
            playerValues[seat] = valueDisplay.getUUID();

            // Interactions first so the bet item can ride the bet interaction.
            Interaction hit = spawnInteraction(level, toWorldPos(center, facing, PLAYER_HIT_LOCALS[seat]), 0.125f, 0.125f);
            interactions[seat][InteractionType.HIT.ordinal()] = hit.getUUID();

            Interaction stand = spawnInteraction(level, toWorldPos(center, facing, PLAYER_STAND_LOCALS[seat]), 0.125f, 0.125f);
            interactions[seat][InteractionType.STAND.ordinal()] = stand.getUUID();

            Interaction bet = spawnInteraction(level, toWorldPos(center, facing, PLAYER_BET_INTERACTION_LOCALS[seat]), 0.235f, 0.01f);
            interactions[seat][InteractionType.BET.ordinal()] = bet.getUUID();

            Interaction ready = spawnInteraction(level, toWorldPos(center, facing, PLAYER_READY_LOCALS[seat]), 0.1f, 0.1f);
            interactions[seat][InteractionType.READY.ordinal()] = ready.getUUID();

            Vec3 betPos = toWorldPos(center, facing, PLAYER_BET_LOCALS[seat]);
            Display.ItemDisplay betDisplay = spawnItem(level, betPos, new ItemStack(Items.AIR));
            DisplayHelper.setItemTransform(betDisplay, net.minecraft.world.item.ItemDisplayContext.FIXED);
            DisplayHelper.setTransformation(betDisplay, BET_ITEM_TRANSLATION, BET_ITEM_LEFT_ROTATION, new Vector3f(0.0f, 0.0f, 0.0f), BET_ITEM_RIGHT_ROTATION);
            DisplayHelper.setBillboard(betDisplay, Display.BillboardConstraints.FIXED);
            setEntityPitch(betDisplay, BET_ITEM_PITCH);
            DisplayHelper.mount(betDisplay, bet);
            playerBets[seat] = betDisplay.getUUID();
        }
    }

    public void removeAll(Level level) {
        discard(level, tableModel);
        tableModel = null;
        discard(level, dealer);
        dealer = null;
        for (int i = 0; i < PLAYER_CARD_COUNT; i++) {
            discard(level, dealerCards[i]);
            dealerCards[i] = null;
        }
        discard(level, dealerValue);
        dealerValue = null;

        for (int seat = 0; seat < SEAT_COUNT; seat++) {
            for (int slot = 0; slot < PLAYER_CARD_COUNT; slot++) {
                discard(level, playerCards[seat][slot]);
                playerCards[seat][slot] = null;
            }
            discard(level, playerValues[seat]);
            playerValues[seat] = null;
            discard(level, playerBets[seat]);
            playerBets[seat] = null;
            for (int type = 0; type < 4; type++) {
                discard(level, interactions[seat][type]);
                interactions[seat][type] = null;
            }
        }
    }

    public void ensureSpawned(Level level, BlockPos pos, Direction facing) {
        if (this.pos == null || !this.pos.equals(pos) || this.facing != facing || !isAlive(level, dealer)) {
            spawnAll(level, pos, facing);
        }
    }

    public void updatePlayerCard(Level level, int group, int slot, BlackjackCard card, boolean visible) {
        if (!isValidPlayerSlot(group, slot)) return;
        Display.TextDisplay display = findText(level, playerCards[group - 1][slot]);
        if (display == null) {
            ensureSpawned(level, pos, facing);
            display = findText(level, playerCards[group - 1][slot]);
        }
        if (display != null) {
            showCard(display, visible ? card.displayGlyph() : "?", slot >= 4);
        }
    }

    public void updateDealerCard(Level level, int slot, BlackjackCard card, boolean hidden) {
        if (slot < 0 || slot >= PLAYER_CARD_COUNT) return;
        Display.TextDisplay display = findText(level, dealerCards[slot]);
        if (display == null) {
            ensureSpawned(level, pos, facing);
            display = findText(level, dealerCards[slot]);
        }
        if (display != null) {
            showCard(display, hidden ? "?" : card.displayGlyph(), false);
        }
    }

    public void updatePlayerHandValue(Level level, int group, String text) {
        if (group < 1 || group > SEAT_COUNT) return;
        Display.TextDisplay display = findText(level, playerValues[group - 1]);
        if (display == null) {
            ensureSpawned(level, pos, facing);
            display = findText(level, playerValues[group - 1]);
        }
        if (display != null) {
            DisplayHelper.setText(display, Component.literal(toMathematicalBoldDigits(text)));
        }
    }

    public void updateDealerHandValue(Level level, String text) {
        Display.TextDisplay display = findText(level, dealerValue);
        if (display == null) {
            ensureSpawned(level, pos, facing);
            display = findText(level, dealerValue);
        }
        if (display != null) {
            DisplayHelper.setText(display, Component.literal(toMathematicalBoldDigits(text)));
        }
    }

    public void updateBetItem(Level level, int group, ItemStack bet) {
        if (group < 1 || group > SEAT_COUNT) return;
        Display.ItemDisplay display = findItem(level, playerBets[group - 1]);
        if (display == null) {
            ensureSpawned(level, pos, facing);
            display = findItem(level, playerBets[group - 1]);
        }
        if (display != null) {
            if (bet.isEmpty()) {
                DisplayHelper.setItem(display, new ItemStack(Items.AIR));
                DisplayHelper.setScale(display, new Vector3f(0.0f, 0.0f, 0.0f));
            } else {
                DisplayHelper.setItem(display, bet.copy());
                DisplayHelper.setScale(display, BET_ITEM_SCALE);
            }
        }
    }

    public void revealDealerHoleCard(Level level, BlackjackCard card) {
        updateDealerCard(level, 1, card, false);
    }

    public void showBust(Level level, int group) {
        if (group < 1 || group > SEAT_COUNT) return;
        Display.TextDisplay display = findText(level, playerValues[group - 1]);
        if (display == null) {
            ensureSpawned(level, pos, facing);
            display = findText(level, playerValues[group - 1]);
        }
        if (display != null) {
            DisplayHelper.setText(display, Component.literal("BUST!"));
        }
    }

    public void resetAllCards(Level level) {
        for (int seat = 0; seat < SEAT_COUNT; seat++) {
            for (int slot = 0; slot < PLAYER_CARD_COUNT; slot++) {
                Display.TextDisplay display = findText(level, playerCards[seat][slot]);
                if (display != null) {
                    hideCard(display, slot >= 4);
                }
            }
            Display.TextDisplay value = findText(level, playerValues[seat]);
            if (value != null) {
                DisplayHelper.setText(value, Component.literal(""));
            }
            Display.ItemDisplay bet = findItem(level, playerBets[seat]);
            if (bet != null) {
                DisplayHelper.setItem(bet, new ItemStack(Items.AIR));
                DisplayHelper.setScale(bet, new Vector3f(0.0f, 0.0f, 0.0f));
            }
        }
        for (int slot = 0; slot < PLAYER_CARD_COUNT; slot++) {
            Display.TextDisplay display = findText(level, dealerCards[slot]);
            if (display != null) {
                hideCard(display, false);
            }
        }
        Display.TextDisplay dealerVal = findText(level, dealerValue);
        if (dealerVal != null) {
            DisplayHelper.setText(dealerVal, Component.literal(""));
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        putUuid(tag, "tableModel", tableModel);
        putUuid(tag, "dealer", dealer);
        for (int i = 0; i < PLAYER_CARD_COUNT; i++) {
            putUuid(tag, "dealerCard" + i, dealerCards[i]);
        }
        putUuid(tag, "dealerValue", dealerValue);
        for (int seat = 0; seat < SEAT_COUNT; seat++) {
            for (int slot = 0; slot < PLAYER_CARD_COUNT; slot++) {
                putUuid(tag, "playerCard_" + seat + "_" + slot, playerCards[seat][slot]);
            }
            putUuid(tag, "playerValue_" + seat, playerValues[seat]);
            putUuid(tag, "playerBet_" + seat, playerBets[seat]);
            for (int type = 0; type < 4; type++) {
                putUuid(tag, "interaction_" + seat + "_" + type, interactions[seat][type]);
            }
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        clearUuids();
        tableModel = getUuid(tag, "tableModel");
        dealer = getUuid(tag, "dealer");
        for (int i = 0; i < PLAYER_CARD_COUNT; i++) {
            dealerCards[i] = getUuid(tag, "dealerCard" + i);
        }
        dealerValue = getUuid(tag, "dealerValue");
        for (int seat = 0; seat < SEAT_COUNT; seat++) {
            for (int slot = 0; slot < PLAYER_CARD_COUNT; slot++) {
                playerCards[seat][slot] = getUuid(tag, "playerCard_" + seat + "_" + slot);
            }
            playerValues[seat] = getUuid(tag, "playerValue_" + seat);
            playerBets[seat] = getUuid(tag, "playerBet_" + seat);
            for (int type = 0; type < 4; type++) {
                interactions[seat][type] = getUuid(tag, "interaction_" + seat + "_" + type);
            }
        }
    }

    private void clearUuids() {
        tableModel = null;
        dealer = null;
        for (int i = 0; i < PLAYER_CARD_COUNT; i++) {
            dealerCards[i] = null;
        }
        dealerValue = null;
        for (int seat = 0; seat < SEAT_COUNT; seat++) {
            for (int slot = 0; slot < PLAYER_CARD_COUNT; slot++) {
                playerCards[seat][slot] = null;
            }
            playerValues[seat] = null;
            playerBets[seat] = null;
            for (int type = 0; type < 4; type++) {
                interactions[seat][type] = null;
            }
        }
    }

    private static boolean isValidPlayerSlot(int group, int slot) {
        return group >= 1 && group <= SEAT_COUNT && slot >= 0 && slot < PLAYER_CARD_COUNT;
    }

    // -------------------------------------------------------------------------
    // Spawning helpers
    // -------------------------------------------------------------------------

    private static Display.ItemDisplay spawnTableModel(Level level, Vec3 center, Direction facing) {
        Display.ItemDisplay display = new Display.ItemDisplay(EntityType.ITEM_DISPLAY, level);
        Vec3 modelPos = toWorldPos(center, facing, TABLE_MODEL_LOCAL);
        display.setPos(modelPos.x, modelPos.y, modelPos.z);

        ItemStack stack = new ItemStack(Items.ARMOR_STAND);
        stack.getOrCreateTag().putInt("CustomModelData", 6902);
        DisplayHelper.setItem(display, stack);
        DisplayHelper.setItemTransform(display, net.minecraft.world.item.ItemDisplayContext.NONE);
        DisplayHelper.setBillboard(display, Display.BillboardConstraints.FIXED);

        // The model's natural front points towards +Z. Rotate it so it faces the table's FACING direction.
        float yRot = facingToYRotation(facing.getOpposite());
        Quaternionf rotation = new Quaternionf().rotationY((float) Math.toRadians(yRot));
        DisplayHelper.setTransformation(display, new Vector3f(0, 0, 0), rotation, new Vector3f(1, 1, 1), new Quaternionf());

        level.addFreshEntity(display);
        return display;
    }

    private static Villager spawnDealer(Level level, Vec3 center, Direction facing) {
        Vec3 dealerPos = toWorldPos(center, facing, DEALER_LOCAL);
        Villager villager = new Villager(EntityType.VILLAGER, level);
        villager.setPos(dealerPos.x, dealerPos.y, dealerPos.z);
        villager.setYRot(facingToYRotation(facing));
        villager.setYHeadRot(facingToYRotation(facing));
        villager.setNoAi(true);
        villager.setInvulnerable(true);
        villager.setSilent(true);
        villager.setPersistenceRequired();

        VillagerProfession profession = PROFESSIONS[level.getRandom().nextInt(PROFESSIONS.length)];
        villager.setVillagerData(new VillagerData(VillagerType.PLAINS, profession, 1));
        villager.setOffers(new MerchantOffers());

        level.addFreshEntity(villager);
        return villager;
    }

    private static Display.TextDisplay spawnText(Level level, Vec3 pos, String text) {
        Display.TextDisplay display = new Display.TextDisplay(EntityType.TEXT_DISPLAY, level);
        display.setPos(pos.x, pos.y, pos.z);
        DisplayHelper.setText(display, Component.literal(text));
        level.addFreshEntity(display);
        return display;
    }

    private static Display.ItemDisplay spawnItem(Level level, Vec3 pos, ItemStack stack) {
        Display.ItemDisplay display = new Display.ItemDisplay(EntityType.ITEM_DISPLAY, level);
        display.setPos(pos.x, pos.y, pos.z);
        DisplayHelper.setItem(display, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        level.addFreshEntity(display);
        return display;
    }

    private static Interaction spawnInteraction(Level level, Vec3 pos, float width, float height) {
        Interaction interaction = new Interaction(EntityType.INTERACTION, level);
        interaction.setPos(pos.x, pos.y, pos.z);
        DisplayHelper.setInteractionSize(interaction, width, height);
        DisplayHelper.setInteractionResponse(interaction, true);
        level.addFreshEntity(interaction);
        return interaction;
    }

    // -------------------------------------------------------------------------
    // Display initialization
    // -------------------------------------------------------------------------

    private static void initCardDisplay(Display.TextDisplay display, int background, boolean extra) {
        DisplayHelper.setBillboard(display, Display.BillboardConstraints.FIXED);
        DisplayHelper.setLineWidth(display, 40);
        DisplayHelper.setBackgroundColor(display, background);
        DisplayHelper.setTextOpacity(display, (byte) -1);
        DisplayHelper.setTextFlags(display, (byte) 0);

        // All cards start hidden (scale 0) regardless of slot; showCard reveals them.
        DisplayHelper.setTransformation(display, CARD_TRANSLATION, CARD_LEFT_ROTATION,
                CARD_SCALE_HIDDEN, CARD_RIGHT_ROTATION);
    }

    private static void initHandValueDisplay(Display.TextDisplay display, Vector3f scale) {
        DisplayHelper.setBillboard(display, Display.BillboardConstraints.FIXED);
        DisplayHelper.setLineWidth(display, 40);
        DisplayHelper.setBackgroundColor(display, HAND_VALUE_BG);
        DisplayHelper.setTextOpacity(display, (byte) -1);
        DisplayHelper.setTextFlags(display, (byte) 0);
        DisplayHelper.setTransformation(display, HAND_VALUE_TRANSLATION, HAND_VALUE_LEFT_ROTATION,
                scale, HAND_VALUE_RIGHT_ROTATION);
    }

    private static void showCard(Display.TextDisplay display, String text, boolean extra) {
        DisplayHelper.setText(display, Component.literal(text));
        DisplayHelper.setTransformation(display, new Vector3f(0.0f, 0.0f, 0.0f), IDENTITY_ROTATION,
                extra ? CARD_SCALE_EXTRA : CARD_SCALE_SHOWN, IDENTITY_ROTATION);
        DisplayHelper.startInterpolation(display, 6);
    }

    private static void hideCard(Display.TextDisplay display, boolean extra) {
        DisplayHelper.setText(display, Component.literal("-"));
        DisplayHelper.setTransformation(display, CARD_TRANSLATION, CARD_LEFT_ROTATION,
                CARD_SCALE_HIDDEN, CARD_RIGHT_ROTATION);
        DisplayHelper.startInterpolation(display, 10);
    }

    // -------------------------------------------------------------------------
    // Coordinate conversion
    // -------------------------------------------------------------------------

    private static void setEntityPitch(Entity entity, float pitch) {
        entity.setXRot(pitch);
    }

    private static Vec3 blockCenter(BlockPos pos) {
        return new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    private static Vec3 toWorldPos(Vec3 center, Direction facing, Vector3f local) {
        Vec3 front = facingVector(facing);
        Vec3 right = sideVector(front);
        Vec3 up = new Vec3(0, 1, 0);
        return center.add(right.scale(local.x)).add(up.scale(local.y)).add(front.scale(local.z));
    }

    private static Vec3 facingVector(Direction facing) {
        return new Vec3(facing.getStepX(), 0, facing.getStepZ());
    }

    private static Vec3 sideVector(Vec3 front) {
        // Right-hand side of a player standing in front of the table (looking along {@code front}).
        return new Vec3(front.z, 0, -front.x);
    }

    private static float facingToYRotation(Direction facing) {
        return switch (facing) {
            case SOUTH -> 180.0f;
            case EAST -> 90.0f;
            case WEST -> -90.0f;
            default -> 0.0f;
        };
    }

    // -------------------------------------------------------------------------
    // Entity lookup helpers
    // -------------------------------------------------------------------------

    private static Display.TextDisplay findText(Level level, UUID uuid) {
        Entity entity = uuid == null ? null : getEntityByUuid(level, uuid);
        return entity instanceof Display.TextDisplay display && entity.isAlive() ? display : null;
    }

    private static Display.ItemDisplay findItem(Level level, UUID uuid) {
        Entity entity = uuid == null ? null : getEntityByUuid(level, uuid);
        return entity instanceof Display.ItemDisplay display && entity.isAlive() ? display : null;
    }

    private static Interaction findInteraction(Level level, UUID uuid) {
        Entity entity = uuid == null ? null : getEntityByUuid(level, uuid);
        return entity instanceof Interaction interaction && entity.isAlive() ? interaction : null;
    }

    private static boolean isAlive(Level level, UUID uuid) {
        Entity entity = uuid == null ? null : getEntityByUuid(level, uuid);
        return entity != null && entity.isAlive();
    }

    private static void discard(Level level, UUID uuid) {
        Entity entity = uuid == null ? null : getEntityByUuid(level, uuid);
        if (entity != null && entity.isAlive()) {
            entity.discard();
        }
    }

    private static Entity getEntityByUuid(Level level, UUID uuid) {
        return level instanceof ServerLevel server ? server.getEntity(uuid) : null;
    }

    /**
     * Converts ASCII digits to mathematical bold digits (𝟎-𝟗) like the 777 datapack hand values.
     */
    private static String toMathematicalBoldDigits(String text) {
        StringBuilder builder = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= '0' && c <= '9') {
                builder.append((char) (0x1D7CE + (c - '0'))); // MATHEMATICAL BOLD DIGIT ZERO
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private static void putUuid(CompoundTag tag, String key, UUID uuid) {
        if (uuid != null) {
            tag.putUUID(key, uuid);
        }
    }

    private static UUID getUuid(CompoundTag tag, String key) {
        return tag.hasUUID(key) ? tag.getUUID(key) : null;
    }
}
