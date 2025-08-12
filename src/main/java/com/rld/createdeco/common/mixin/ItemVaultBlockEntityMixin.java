package com.rld.createdeco.common.mixin;

import com.rld.createdeco.CreateDecoMod;
import com.rld.createdeco.common.blocks.ShippingContainerBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import com.simibubi.create.foundation.ICapabilityProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Collectors;

@Mixin(ItemVaultBlockEntity.class)
public abstract class ItemVaultBlockEntityMixin {
  @ModifyArg(
    method = "initCapability()V",
    at = @At(
      value = "INVOKE",
      target = "Lcom/simibubi/create/api/connectivity/ConnectivityHandler;partAt(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"
    ),
    index = 0
  )
  public BlockEntityType<?> initCapability (BlockEntityType<?> type) {

    if ((Object)this instanceof ShippingContainerBlock.Entity container) {
        CreateDecoMod.LOGGER.info("Injected: {}", ((ShippingContainerBlock) container.getBlockState().getBlock()).COLOR);
      return ((ShippingContainerBlock)container.getBlockState().getBlock()).getBlockEntityType();
    }
    return type;
  }
}
