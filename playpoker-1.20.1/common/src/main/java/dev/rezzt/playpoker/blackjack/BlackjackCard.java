package dev.rezzt.playpoker.blackjack;

import net.minecraft.nbt.CompoundTag;

/**
 * Immutable representation of a blackjack card using the same encoding as the 777 datapack.
 *
 * <p>The datapack stores cards as integers:</p>
 * <ul>
 *   <li>2-10 numeric value</li>
 *   <li>74 = 'J' (jack)</li>
 *   <li>81 = 'Q' (queen)</li>
 *   <li>75 = 'K' (king)</li>
 *   <li>65 = 'A' (ace)</li>
 * </ul>
 *
 * <p>Aces are always valued at 11 by the card itself; hand logic performs the soft-fix.</p>
 */
public record BlackjackCard(int datapackValue) {

    public static final int JACK = 74;
    public static final int QUEEN = 81;
    public static final int KING = 75;
    public static final int ACE = 65;

    public BlackjackCard {
        if (!isValid(datapackValue)) {
            throw new IllegalArgumentException("Invalid datapack card value: " + datapackValue);
        }
    }

    public static boolean isValid(int value) {
        return (value >= 2 && value <= 10) || value == JACK || value == QUEEN || value == KING || value == ACE;
    }

    /**
     * Blackjack value: 2-10 keep their face value, J/Q/K are 10, A is 11.
     */
    public int blackjackValue() {
        return switch (datapackValue) {
            case JACK, QUEEN, KING -> 10;
            case ACE -> 11;
            default -> datapackValue;
        };
    }

    public boolean isAce() {
        return datapackValue == ACE;
    }

    /**
     * Unicode glyph used by the 777 datapack for the card face.
     */
    public String displayGlyph() {
        return switch (datapackValue) {
            case 2 -> "\u278B";
            case 3 -> "\u278C";
            case 4 -> "\u278D";
            case 5 -> "\u278E";
            case 6 -> "\u278F";
            case 7 -> "\u2790";
            case 8 -> "\u2791";
            case 9 -> "\u2792";
            case 10 -> "\u2793";
            case JACK -> "\uD83C\uDD79";
            case QUEEN -> "\uD83C\uDD80";
            case KING -> "\uD83C\uDD7A";
            case ACE -> "\u2660";
            default -> String.valueOf(datapackValue);
        };
    }

    public String displayText() {
        return switch (datapackValue) {
            case JACK -> "J";
            case QUEEN -> "Q";
            case KING -> "K";
            case ACE -> "A";
            default -> String.valueOf(datapackValue);
        };
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("value", datapackValue);
        return tag;
    }

    public static BlackjackCard load(CompoundTag tag) {
        return new BlackjackCard(tag.getInt("value"));
    }
}
