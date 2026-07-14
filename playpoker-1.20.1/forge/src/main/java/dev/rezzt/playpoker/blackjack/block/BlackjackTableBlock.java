package dev.rezzt.playpoker.blackjack.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Placeable blackjack table block. Right-click to play, left-click to remove a bet in the IDLE phase.
 *
 * <p>The block has a horizontal {@code FACING} property that points toward the player side of the table.</p>
 */
public class BlackjackTableBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlackjackTableBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_GREEN)
                .strength(2.5f, 6.0f)
                .sound(SoundType.WOOD)
                .noOcclusion());
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    // Small centred shape so the player can target and break the anchor block.
    private static final VoxelShape ANCHOR_SHAPE = Shapes.box(0.4, 0.0, 0.4, 0.6, 0.1, 0.6);

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return ANCHOR_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        return ANCHOR_SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlackjackTableBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide()
                ? null
                : createTickerHelper(type, dev.rezzt.playpoker.ModBlockEntities.BLACKJACK_TABLE.get(),
                (l, p, s, be) -> be.tickServer());
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        // Interaction is handled by Interaction entities (PlayerInteractEvent.EntityInteractSpecific)
        // or by the RightClickBlock fallback. We never consume the click here so entities stay clickable.
        return InteractionResult.PASS;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide()) {
            return;
        }
        if (level.getBlockEntity(pos) instanceof BlackjackTableBlockEntity blockEntity) {
            blockEntity.onAttack(player);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock() && level.getBlockEntity(pos) instanceof BlackjackTableBlockEntity blockEntity) {
            blockEntity.onRemove();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    /**
     * Maps a hit location on the block to a player seat group (1, 2 or 3).
     *
     * <p>Group 2 is the centre, group 1 is to the left and group 3 to the right from the perspective
     * of someone standing in front of the table (looking in the {@code FACING} direction).</p>
     *
     * @return the seat group, or {@code -1} if the hit is outside the horizontal seat area.
     */
    public static int getGroupFromHit(BlockState state, BlockPos pos, Vec3 hitLocation) {
        Direction facing = state.getValue(FACING);
        Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        Vec3 front = new Vec3(facing.getStepX(), 0, facing.getStepZ());
        Vec3 side = new Vec3(front.z, 0, -front.x); // right of the player facing the table

        Vec3 toHit = hitLocation.subtract(center);
        if (toHit.dot(front) < -0.55 || toHit.dot(front) > 0.55) {
            return -1; // too far forward/back
        }
        double sideCoord = toHit.dot(side);
        if (Math.abs(sideCoord) > 0.55) {
            return -1;
        }
        if (sideCoord < -1.0 / 6.0) {
            return 1; // player's left
        }
        if (sideCoord > 1.0 / 6.0) {
            return 3; // player's right
        }
        return 2;
    }
}
