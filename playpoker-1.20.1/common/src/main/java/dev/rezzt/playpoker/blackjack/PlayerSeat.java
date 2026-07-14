package dev.rezzt.playpoker.blackjack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/**
 * Mutable state for one player seat at the blackjack table.
 */
public final class PlayerSeat {

    private ItemStack bet = ItemStack.EMPTY;
    private boolean ready;
    private boolean active;
    private boolean standing;
    private boolean busted;
    private final BlackjackHand hand = new BlackjackHand();
    private int hitCounter;

    public boolean hasBet() {
        return !bet.isEmpty();
    }

    public ItemStack bet() {
        return bet.copy();
    }

    public void setBet(ItemStack bet) {
        this.bet = bet.copy();
    }

    public void clearBet() {
        this.bet = ItemStack.EMPTY;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isStanding() {
        return standing;
    }

    public void setStanding(boolean standing) {
        this.standing = standing;
    }

    public boolean isBusted() {
        return busted;
    }

    public void setBusted(boolean busted) {
        this.busted = busted;
    }

    public BlackjackHand hand() {
        return hand;
    }

    public int hitCounter() {
        return hitCounter;
    }

    public void setHitCounter(int hitCounter) {
        this.hitCounter = hitCounter;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("bet", bet.save(new CompoundTag()));
        tag.putBoolean("ready", ready);
        tag.putBoolean("active", active);
        tag.putBoolean("standing", standing);
        tag.putBoolean("busted", busted);
        tag.put("hand", hand.save());
        tag.putInt("hitCounter", hitCounter);
        return tag;
    }

    public void load(CompoundTag tag) {
        this.bet = ItemStack.of(tag.getCompound("bet"));
        this.ready = tag.getBoolean("ready");
        this.active = tag.getBoolean("active");
        this.standing = tag.getBoolean("standing");
        this.busted = tag.getBoolean("busted");
        this.hand.load(tag.getCompound("hand"));
        this.hitCounter = tag.getInt("hitCounter");
    }
}
