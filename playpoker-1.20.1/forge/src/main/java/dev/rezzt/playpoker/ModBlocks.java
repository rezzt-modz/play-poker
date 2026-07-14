package dev.rezzt.playpoker;

import dev.rezzt.playpoker.blackjack.block.BlackjackTableBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            ForgeRegistries.BLOCKS,
            ExampleMod.MOD_ID
    );

    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS,
            ExampleMod.MOD_ID
    );

    // The block defines its own properties and the horizontal FACING state internally.
    public static final RegistryObject<Block> BLACKJACK_TABLE = BLOCKS.register(
            "blackjack_table",
            BlackjackTableBlock::new
    );

    public static final RegistryObject<Item> BLACKJACK_TABLE_ITEM = BLOCK_ITEMS.register(
            "blackjack_table",
            () -> new BlockItem(BLACKJACK_TABLE.get(), new Item.Properties())
    );

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ITEMS.register(modEventBus);
    }
}
