package com.rld.createdeco;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.IItemHandler;

public class LoaderUtil {
  public static int getSignal (IBE<?> be, BlockState pState, Level pLevel, BlockPos pPos) {
    return be.getBlockEntityOptional(pLevel, pPos).map((blockEntity) ->
        pLevel.getCapability(LoaderUtil.ITEM_HANDLER, pPos, pState, blockEntity)
    ).map(ItemHelper::calcRedstoneFromInventory).orElse(0);
  }

  // this check is only done in Fabric's implementation, forge doesn't seem to need it
  public static boolean checkPlacingNbt (BlockPlaceContext ctx) { return true; }

  public static final BlockCapability<IItemHandler, Void> ITEM_HANDLER = BlockCapability.createVoid(
      ResourceLocation.fromNamespaceAndPath(CreateDecoMod.MOD_ID, "item_handler"),
      IItemHandler.class
  );
}
