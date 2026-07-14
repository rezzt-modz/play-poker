package dev.rezzt.playpoker;

import dev.rezzt.playpoker.item.PokerChipItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS,
            ExampleMod.MOD_ID
    );

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            ExampleMod.MOD_ID
    );

    public static final RegistryObject<Item> POKER_CHIP = ITEMS.register(
            "poker_chip",
            PokerChipItem::new
    );

    public static final RegistryObject<CreativeModeTab> PLAY_POKER_TAB = CREATIVE_MODE_TABS.register(
            "play_poker_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(POKER_CHIP.get()))
                    .title(Component.translatable("itemGroup.playpoker.play_poker_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(POKER_CHIP.get());
                        output.accept(ModBlocks.BLACKJACK_TABLE_ITEM.get());
                    })
                    .build()
    );

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
