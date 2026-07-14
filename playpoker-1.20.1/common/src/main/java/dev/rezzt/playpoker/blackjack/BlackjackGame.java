package dev.rezzt.playpoker.blackjack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Pure-logic blackjack game that mirrors the 777 datapack behavior.
 *
 * <p>Supports three player seats (groups 1-3) plus the dealer (group 0). The game advances
 * through the same phases as the datapack: betting, dealing, playing, finishing dealer hand,
 * settling and resetting.</p>
 */
public final class BlackjackGame {

    public enum Phase {
        IDLE,
        DEALING,
        PLAYING,
        FINISHING,
        SETTLING,
        RESETTING
    }

    public static final int DEALER_GROUP = 0;
    public static final int MIN_PLAYER_GROUP = 1;
    public static final int MAX_PLAYER_GROUP = 3;
    public static final int MAX_CARDS_PER_HAND = 7;

    // Datapack timers.
    private static final int DEAL_DELAY_TICKS = 7;
    private static final int DEALER_HIT_DELAY_TICKS = 12;
    private static final int SETTLE_TICK = 40; // compare_scores runs at this tick
    private static final int RESET_DELAY_TICKS = 70;

    private Phase phase = Phase.IDLE;
    private BlackjackDeck deck;

    private final PlayerSeat[] seats = new PlayerSeat[MAX_PLAYER_GROUP + 1];
    private final BlackjackHand dealerHand = new BlackjackHand();
    private int dealerHitCounter = 0;

    private int dealTimer = 0;
    private int dealToGroup = MAX_PLAYER_GROUP; // start with group 3
    private int dealRound = 1;
    private int resetTimer = 0;

    private boolean holeCardRevealed = false;

    public BlackjackGame() {
        for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
            seats[i] = new PlayerSeat();
        }
    }

    public Phase phase() {
        return phase;
    }

    public PlayerSeat seat(int group) {
        if (group < MIN_PLAYER_GROUP || group > MAX_PLAYER_GROUP) {
            throw new IllegalArgumentException("Invalid player group: " + group);
        }
        return seats[group];
    }

    public List<PlayerSeat> activeSeats() {
        List<PlayerSeat> list = new ArrayList<>();
        for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
            if (seats[i].hasBet()) {
                list.add(seats[i]);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public BlackjackHand dealerHand() {
        return dealerHand;
    }

    public boolean isHoleCardHidden() {
        return phase == Phase.DEALING || phase == Phase.PLAYING || (phase == Phase.FINISHING && !holeCardRevealed);
    }

    /**
     * Places or replaces a bet for the given seat. Matches datapack behavior where placing a bet
     * first returns any existing bet to the player.
     */
    public boolean placeBet(int group, ItemStack heldItem, GameCallbacks callbacks) {
        PlayerSeat seat = seat(group);
        if (phase != Phase.IDLE) {
            callbacks.sendInGameError();
            return false;
        }
        if (heldItem.isEmpty()) {
            callbacks.chat("chat.playpoker.blackjack.must_hold_item");
            return false;
        }
        int amount = Math.min(heldItem.getCount(), 64);
        if (seat.hasBet()) {
            ItemStack current = seat.bet();
            if (ItemStack.isSameItemSameTags(current, heldItem)) {
                int newCount = Math.min(current.getCount() + amount, 64);
                if (newCount == current.getCount()) {
                    callbacks.chat("chat.playpoker.blackjack.bet_full");
                    return false;
                }
                ItemStack merged = current.copy();
                merged.setCount(newCount);
                seat.setBet(merged);
                callbacks.playSound(group, "entity.pig.saddle", 1.0f, 2.0f);
                callbacks.spawnParticle(group, "composter");
                return true;
            }
            callbacks.returnBet(group, current);
        }
        ItemStack bet = heldItem.copy();
        bet.setCount(amount);
        seat.setBet(bet);
        callbacks.playSound(group, "entity.pig.saddle", 1.0f, 2.0f);
        callbacks.spawnParticle(group, "composter");
        return true;
    }

    /**
     * Removes a bet and returns it to the player.
     */
    public boolean removeBet(int group, GameCallbacks callbacks) {
        PlayerSeat seat = seat(group);
        if (phase != Phase.IDLE) {
            callbacks.sendInGameError();
            return false;
        }
        if (!seat.hasBet()) {
            return false;
        }
        callbacks.returnBet(group, seat.bet());
        seat.clearBet();
        callbacks.playSound(group, "entity.item.pickup", 1.0f, 0.7f);
        callbacks.spawnParticle(group, "white_smoke");
        return true;
    }

    /**
     * Toggles the ready state for a seat. If all occupied seats are ready, the round starts.
     */
    public boolean toggleReady(int group, GameCallbacks callbacks, Random random) {
        PlayerSeat seat = seat(group);
        if (phase != Phase.IDLE) {
            callbacks.sendInGameError();
            return false;
        }
        if (!seat.hasBet()) {
            callbacks.chat("chat.playpoker.blackjack.no_bet");
            return false;
        }
        if (seat.isReady()) {
            seat.setReady(false);
            seat.setActive(false);
            callbacks.playSound(group, "ui.stonecutter.take_result", 1.0f, 1.5f);
            callbacks.spawnParticle(group, "dust_red");
        } else {
            seat.setReady(true);
            seat.setActive(true);
            callbacks.chat("chat.playpoker.blackjack.bet_confirmed");
            callbacks.playSound(group, "block.note_block.harp", 1.0f, 2.0f);
            callbacks.spawnParticle(group, "dust_green");

            // Check for any other occupied seat that is not ready.
            for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
                if (seats[i].hasBet() && !seats[i].isReady() && i != group) {
                    callbacks.chat("chat.playpoker.blackjack.bet_not_ready");
                    return true;
                }
            }
            if (allBetsReady()) {
                startRound(callbacks, random);
            }
        }
        return true;
    }

    private boolean allBetsReady() {
        boolean anyBet = false;
        for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
            if (seats[i].hasBet()) {
                anyBet = true;
                if (!seats[i].isReady()) {
                    return false;
                }
            }
        }
        return anyBet;
    }

    private void startRound(GameCallbacks callbacks, Random random) {
        deck = new BlackjackDeck(random);
        phase = Phase.DEALING;
        dealTimer = 0;
        dealToGroup = MAX_PLAYER_GROUP;
        dealRound = 1;
        for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
            seats[i].hand().clear();
            seats[i].setStanding(false);
            seats[i].setBusted(false);
            seats[i].setHitCounter(0);
        }
        dealerHand.clear();
        dealerHitCounter = 0;
        holeCardRevealed = false;
        callbacks.dealerChat(DealerChat.startDeal(random));
    }

    /**
     * Player requests a hit.
     */
    public boolean hit(int group, GameCallbacks callbacks, Random random) {
        PlayerSeat seat = seat(group);
        if (phase == Phase.DEALING || phase != Phase.PLAYING || !seat.isActive() || seat.isStanding() || seat.isBusted()) {
            callbacks.sendInGameError();
            return false;
        }
        if (seat.hitCounter() >= MAX_CARDS_PER_HAND) {
            callbacks.chatToGroup(group, "chat.playpoker.blackjack.max_cards");
            return false;
        }
        callbacks.dealerChat(DealerChat.hit(random));
        dealCardTo(group, seat.hitCounter(), callbacks);
        seat.setHitCounter(seat.hitCounter() + 1);
        callbacks.playSound(group, "block.copper_bulb.place", 1.0f, 1.25f);

        if (seat.hand().isBust()) {
            bustPlayer(group, callbacks, random);
        }
        return true;
    }

    private void bustPlayer(int group, GameCallbacks callbacks, Random random) {
        PlayerSeat seat = seat(group);
        seat.setBusted(true);
        seat.setStanding(true);
        seat.setActive(false);
        callbacks.showBust(group);
        callbacks.playSound(group, "block.glass.break", 1.0f, 1.25f);
        callbacks.spawnParticle(group, "totem_of_undying");
        callbacks.dealerChat(DealerChat.bustPlayer(random));
        callbacks.loseBet(group, seat.bet());
        seat.clearBet();
        checkAllStood(callbacks, random);
    }

    /**
     * Player stands.
     */
    public boolean stand(int group, GameCallbacks callbacks, Random random) {
        PlayerSeat seat = seat(group);
        if (phase == Phase.DEALING || phase != Phase.PLAYING || !seat.isActive() || seat.isStanding() || seat.isBusted()) {
            callbacks.sendInGameError();
            return false;
        }
        seat.setStanding(true);
        seat.setActive(false);
        callbacks.playSound(group, "block.note_block.pling", 0.5f, 2.0f);
        callbacks.dealerChat(DealerChat.stand(random));

        if (!checkAllStood(callbacks, random)) {
            callbacks.chatToGroup(group, "chat.playpoker.blackjack.waiting_for_others");
        }
        return true;
    }

    private boolean checkAllStood(GameCallbacks callbacks, Random random) {
        for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
            PlayerSeat seat = seats[i];
            if (seat.hasBet() && !seat.isBusted() && !seat.isStanding()) {
                return false;
            }
        }
        startDealerTurn(callbacks);
        return true;
    }

    private void startDealerTurn(GameCallbacks callbacks) {
        phase = Phase.FINISHING;
        dealTimer = 0;
        dealerHitCounter = 2; // dealer already has 2 cards
        holeCardRevealed = true;
        callbacks.revealDealerHoleCard();
    }

    /**
     * Advances the game by one server tick.
     */
    public void tick(Random random, GameCallbacks callbacks) {
        switch (phase) {
            case DEALING -> tickDealing(callbacks);
            case FINISHING -> tickFinishing(callbacks, random);
            case SETTLING -> tickSettling(callbacks, random);
            case RESETTING -> tickResetting(callbacks);
            default -> {
                // IDLE and PLAYING wait for player input.
            }
        }
    }

    private void tickDealing(GameCallbacks callbacks) {
        dealTimer++;
        if (dealTimer < DEAL_DELAY_TICKS) {
            return;
        }
        dealTimer = 0;

        // If the target seat has no bet, skip dealing a card to it but still advance.
        if (dealToGroup == DEALER_GROUP || seats[dealToGroup].hasBet()) {
            dealCardTo(dealToGroup, dealRound - 1, callbacks);
            if (dealToGroup == DEALER_GROUP) {
                dealerHitCounter++;
            } else {
                seats[dealToGroup].setHitCounter(dealRound);
            }
        }

        // Advance group 3 -> 2 -> 1 -> 0 (dealer), then next round.
        dealToGroup--;
        if (dealToGroup < DEALER_GROUP) {
            dealToGroup = MAX_PLAYER_GROUP;
            dealRound++;
        }

        if (dealRound > 2) {
            phase = Phase.PLAYING;
            for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
                if (seats[i].hasBet() && !seats[i].isBusted()) {
                    seats[i].setActive(true);
                }
            }
        }
    }

    private void dealCardTo(int group, int slotIndex, GameCallbacks callbacks) {
        BlackjackCard card = deck.draw();
        if (group == DEALER_GROUP) {
            dealerHand.add(card);
            callbacks.showDealerCard(slotIndex, card, slotIndex == 1 && !holeCardRevealed);
        } else {
            seats[group].hand().add(card);
            callbacks.showPlayerCard(group, slotIndex, card);
        }
        callbacks.playSound(group, "block.moss.step", 1.0f, 1.5f);
        callbacks.spawnParticle(group, "electric_spark");
        if (group != DEALER_GROUP || slotIndex != 1 || holeCardRevealed) {
            callbacks.dealerChat(String.format("Card Dealt: %s", card.displayText()));
        }
    }

    private void tickFinishing(GameCallbacks callbacks, Random random) {
        if (dealerHand.value() >= 17) {
            phase = Phase.SETTLING;
            resetTimer = 0;
            if (dealerHand.isBust()) {
                callbacks.broadcastDealerBust(dealerHand.value());
                callbacks.dealerChat(DealerChat.bustDealer(random));
            }
            return;
        }

        dealTimer++;
        if (dealTimer < DEALER_HIT_DELAY_TICKS) {
            return;
        }
        dealTimer = 0;

        dealCardTo(DEALER_GROUP, dealerHitCounter, callbacks);
        dealerHitCounter++;
    }

    private void tickSettling(GameCallbacks callbacks, Random random) {
        resetTimer++;
        if (resetTimer < SETTLE_TICK) {
            return;
        }

        int dealerValue = dealerHand.value();
        boolean dealerBust = dealerHand.isBust();

        for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
            PlayerSeat seat = seats[i];
            if (!seat.hasBet() || seat.isBusted()) {
                continue;
            }
            int playerValue = seat.hand().value();
            ItemStack bet = seat.bet();

            if (playerValue == 21 && dealerValue != 21) {
                callbacks.dealerChat(DealerChat.blackjack(random));
                callbacks.payoutBlackjack(i, bet);
            } else if (dealerBust) {
                callbacks.dealerChat(DealerChat.win(random));
                callbacks.payoutWin(i, bet);
            } else if (playerValue > dealerValue) {
                callbacks.dealerChat(DealerChat.win(random));
                callbacks.payoutWin(i, bet);
            } else if (playerValue == dealerValue) {
                callbacks.dealerChat(DealerChat.tie(random));
                callbacks.returnBet(i, bet);
            } else {
                callbacks.dealerChat(DealerChat.lose(random));
                callbacks.loseBet(i, bet);
            }
            seat.clearBet();
        }

        phase = Phase.RESETTING;
        resetTimer = 0;
    }

    private void tickResetting(GameCallbacks callbacks) {
        resetTimer++;
        if (resetTimer < RESET_DELAY_TICKS) {
            return;
        }
        resetRound(callbacks);
    }

    private void resetRound(GameCallbacks callbacks) {
        phase = Phase.IDLE;
        dealTimer = 0;
        resetTimer = 0;
        dealToGroup = MAX_PLAYER_GROUP;
        dealRound = 1;
        holeCardRevealed = false;
        dealerHand.clear();
        dealerHitCounter = 0;
        for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
            PlayerSeat seat = seats[i];
            seat.hand().clear();
            seat.setReady(false);
            seat.setActive(false);
            seat.setStanding(false);
            seat.setBusted(false);
            seat.setHitCounter(0);
        }
        callbacks.resetVisuals();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("phase", phase.name());
        if (deck != null) {
            tag.put("deck", deck.save());
        }
        tag.put("dealerHand", dealerHand.save());
        tag.putInt("dealerHitCounter", dealerHitCounter);
        tag.putInt("dealTimer", dealTimer);
        tag.putInt("dealToGroup", dealToGroup);
        tag.putInt("dealRound", dealRound);
        tag.putInt("resetTimer", resetTimer);
        tag.putBoolean("holeCardRevealed", holeCardRevealed);

        ListTag seatsTag = new ListTag();
        for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP; i++) {
            seatsTag.add(seats[i].save());
        }
        tag.put("seats", seatsTag);
        return tag;
    }

    public void load(CompoundTag tag) {
        try {
            this.phase = Phase.valueOf(tag.getString("phase"));
        } catch (IllegalArgumentException ignored) {
            this.phase = Phase.IDLE;
        }
        if (tag.contains("deck", Tag.TAG_COMPOUND)) {
            this.deck = BlackjackDeck.load(tag.getCompound("deck"), new Random());
        } else {
            this.deck = null;
        }
        this.dealerHand.load(tag.getCompound("dealerHand"));
        this.dealerHitCounter = tag.getInt("dealerHitCounter");
        this.dealTimer = tag.getInt("dealTimer");
        this.dealToGroup = tag.getInt("dealToGroup");
        this.dealRound = tag.getInt("dealRound");
        this.resetTimer = tag.getInt("resetTimer");
        this.holeCardRevealed = tag.getBoolean("holeCardRevealed");

        ListTag seatsTag = tag.getList("seats", Tag.TAG_COMPOUND);
        for (int i = MIN_PLAYER_GROUP; i <= MAX_PLAYER_GROUP && i - MIN_PLAYER_GROUP < seatsTag.size(); i++) {
            seats[i].load(seatsTag.getCompound(i - MIN_PLAYER_GROUP));
        }
    }
}
