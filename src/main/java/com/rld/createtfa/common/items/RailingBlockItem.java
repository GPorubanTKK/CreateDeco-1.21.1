package com.rld.createtfa.common.items;

import com.rld.createtfa.common.registration.BlockRegistry;
import com.rld.createtfa.common.blocks.CatwalkRailingBlock;
import com.rld.createtfa.common.blocks.CatwalkStairBlock;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.function.Predicate;


public class RailingBlockItem extends BlockItem {
    private final int placementHelperID;

    public RailingBlockItem (CatwalkRailingBlock block, Properties props) {
        super(block, props);
        placementHelperID = PlacementHelpers.register(new RailingHelper());
    }

    @Override
    public InteractionResult useOn (UseOnContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Direction face = ctx.getClickedFace();
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();

        BlockState state = level.getBlockState(pos);
        IPlacementHelper helper = PlacementHelpers.get(placementHelperID);
        BlockHitResult ray = new BlockHitResult(ctx.getClickLocation(), face, pos, true);

        if (player == null) return InteractionResult.PASS;
        if (state.getBlock() instanceof CatwalkStairBlock catstair && this.getBlock().equals(BlockRegistry.CATWALK_RAILINGS.get(catstair.metal).get())) {
            var dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            var xPos = ctx.getClickLocation().x - (double) pos.getX() - 0.5;
            var zPos = ctx.getClickLocation().z - (double) pos.getZ() - 0.5;
            boolean left = switch(dir) {
                case NORTH -> xPos > 0;
                case SOUTH -> xPos < 0;
                case EAST -> zPos > 0;
                case WEST -> zPos < 0;
                default -> false;
            };
            if (state.getValue(left ? CatwalkStairBlock.RAILING_LEFT : CatwalkStairBlock.RAILING_RIGHT)) return InteractionResult.PASS;
            SoundType soundType = state.getSoundType(level, pos, player);
            level.setBlock(pos, state.setValue(left ? CatwalkStairBlock.RAILING_LEFT : CatwalkStairBlock.RAILING_RIGHT, true), 3);
            level.playSound(
                player, pos, getPlaceSound(state, level, pos, player),
                SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F
            );
            level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, state));
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        PlacementOffset offset = null;
        if (helper.matchesState(state)) {
            offset = helper.getOffset(player, level, state, pos, ray);
            //return offset.placeInWorld(world, this, player, ctx.getHand(), ray);
        }

        if (offset != null && offset.isSuccessful() && !player.isShiftKeyDown() //&& railMatchTest
        ) {
            state = offset.getGhostState(); //level.getBlockState(offset.getBlockPos());
            var offsetPos = offset.getBlockPos();
            var soundType = state.getSoundType();

            level.setBlock(offsetPos, offset.getTransform().apply(state), 3);
            level.playSound(player, offsetPos, this.getPlaceSound(state), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
            level.gameEvent(GameEvent.BLOCK_PLACE, offsetPos, GameEvent.Context.of(player, state));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        return super.useOn(ctx);
    }

    @MethodsReturnNonnullByDefault
    public static class RailingHelper implements IPlacementHelper {
        @Override
        public Predicate<ItemStack> getItemPredicate () {
            return CatwalkRailingBlock::isRailing;
        }

        @Override
        public Predicate<BlockState> getStatePredicate () {
            return state -> true; //CatwalkRailingBlock.isRailing(state.getBlock());
        }

        @Override
        public PlacementOffset getOffset (
            Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray
        ) {
            Direction face = ray.getDirection();
            BlockState adjacent = world.getBlockState(pos.relative(face));
            if (CatwalkRailingBlock.isRailing(adjacent.getBlock())) {
                pos = pos.relative(face);
                state = adjacent;
            }

            boolean railMatchTest = player.isHolding(state.getBlock().asItem());

            if (!CatwalkRailingBlock.isRailing(state.getBlock()) ||
                (state.getValue(CatwalkRailingBlock.NORTH_FENCE)
                    && state.getValue(CatwalkRailingBlock.SOUTH_FENCE)
                    && state.getValue(CatwalkRailingBlock.EAST_FENCE)
                    && state.getValue(CatwalkRailingBlock.WEST_FENCE)) || !railMatchTest) {
                return PlacementOffset.fail();
            }

            List<Direction> dirs = IPlacementHelper.orderedByDistanceExceptAxis(
                pos, ray.getLocation(), Direction.Axis.Y
            );
            for (Direction offset : dirs) {
                if (!state.getValue(CatwalkRailingBlock.fromDirection(offset))) {
                    state = state.setValue(CatwalkRailingBlock.fromDirection(offset), true);
                    break;
                }
            }

            BlockState finalState = state;
            return PlacementOffset.success(pos, newState -> finalState).withGhostState(finalState);
        }
    }
}
