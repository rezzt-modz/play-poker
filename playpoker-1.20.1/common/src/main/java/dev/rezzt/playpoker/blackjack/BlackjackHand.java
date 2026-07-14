package dev.rezzt.playpoker.blackjack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A blackjack hand. Calculates its own best value using the same soft-fix logic as the 777 datapack:
 * aces count as 11 while possible, then degrade to 1 as needed.
 */
public final class BlackjackHand {

    private final List<BlackjackCard> cards = new ArrayList<>();

    public void add(BlackjackCard card) {
        cards.add(card);
    }

    public void clear() {
        cards.clear();
    }

    public List<BlackjackCard> cards() {
        return Collections.unmodifiableList(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    /**
     * Best hand value with aces valued as 11 or 1.
     */
    public int value() {
        int total = 0;
        int softAces = 0;
        for (BlackjackCard card : cards) {
            int v = card.blackjackValue();
            total += v;
            if (card.isAce()) {
                softAces++;
            }
        }
        // Soft-fix: while busted and we have an ace still counting as 11, make it 1.
        while (total >= 22 && softAces > 0) {
            total -= 10;
            softAces--;
        }
        return total;
    }

    public boolean isBust() {
        return value() >= 22;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && value() == 21;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (BlackjackCard card : cards) {
            list.add(card.save());
        }
        tag.put("cards", list);
        return tag;
    }

    public void load(CompoundTag tag) {
        cards.clear();
        ListTag list = tag.getList("cards", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            cards.add(BlackjackCard.load(list.getCompound(i)));
        }
    }
}
