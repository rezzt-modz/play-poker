package dev.rezzt.playpoker;

import dev.rezzt.playpoker.blackjack.block.BlackjackTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES,
            ExampleMod.MOD_ID
    );

    public static final RegistryObject<BlockEntityType<BlackjackTableBlockEntity>> BLACKJACK_TABLE = BLOCK_ENTITIES.register(
            "blackjack_table",
            () -> BlockEntityType.Builder.of(
                    BlackjackTableBlockEntity::new,
                    ModBlocks.BLACKJACK_TABLE.get()
            ).build(null)
    );

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
