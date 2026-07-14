package dev.rezzt.playpoker.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

/**
 * A creative-only casino chip item.
 *
 * <p>It has no crafting recipe or loot source and is only available through
 * the Creative inventory (in the mod's custom "Play Poker" tab).</p>
 */
public class PokerChipItem extends Item {

    public PokerChipItem() {
        super(new Properties()
                .stacksTo(64)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }
}
