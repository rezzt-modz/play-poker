package dev.rezzt.playpoker.blackjack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A standard 52-card deck following the exact card distribution of the 777 datapack.
 *
 * <p>The datapack deck is encoded as integers with four copies of each rank and no suits.</p>
 */
public final class BlackjackDeck {

    private static final int[] DATAPACK_DECK = {
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
            5, 5, 5, 5,
            6, 6, 6, 6,
            7, 7, 7, 7,
            8, 8, 8, 8,
            9, 9, 9, 9,
            10, 10, 10, 10,
            BlackjackCard.JACK, BlackjackCard.JACK, BlackjackCard.JACK, BlackjackCard.JACK,
            BlackjackCard.QUEEN, BlackjackCard.QUEEN, BlackjackCard.QUEEN, BlackjackCard.QUEEN,
            BlackjackCard.KING, BlackjackCard.KING, BlackjackCard.KING, BlackjackCard.KING,
            BlackjackCard.ACE, BlackjackCard.ACE, BlackjackCard.ACE, BlackjackCard.ACE
    };

    private final List<BlackjackCard> cards;

    public BlackjackDeck(Random random) {
        this.cards = new ArrayList<>(DATAPACK_DECK.length);
        for (int value : DATAPACK_DECK) {
            this.cards.add(new BlackjackCard(value));
        }
        Collections.shuffle(this.cards, random);
    }

    private BlackjackDeck(List<BlackjackCard> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int remaining() {
        return cards.size();
    }

    /**
     * Draws the top card from the deck.
     *
     * @throws IllegalStateException if the deck is empty
     */
    public BlackjackCard draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck");
        }
        return cards.remove(cards.size() - 1);
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

    public static BlackjackDeck load(CompoundTag tag, Random random) {
        if (!tag.contains("cards", Tag.TAG_LIST)) {
            return new BlackjackDeck(random);
        }
        ListTag list = tag.getList("cards", Tag.TAG_COMPOUND);
        List<BlackjackCard> cards = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            cards.add(BlackjackCard.load(list.getCompound(i)));
        }
        return new BlackjackDeck(cards);
    }
}
