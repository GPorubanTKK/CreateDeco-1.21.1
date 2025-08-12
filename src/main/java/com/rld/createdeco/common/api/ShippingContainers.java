package com.rld.createdeco.common.api;

import com.rld.createdeco.BlockStateGenerator;
import com.rld.createdeco.CreateDecoMod;
import com.rld.createdeco.common.blocks.ShippingContainerBlock;
import com.rld.createdeco.common.connected.ShippingContainerCTBehavior;
import com.rld.createdeco.common.items.ShippingContainerBlockItem;
import com.rld.createdeco.common.registration.CreateDecoRegistrate;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;

import static com.rld.createdeco.common.registration.CreateDecoRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ShippingContainers {
  public static BlockBuilder<ShippingContainerBlock,?> build (
      CreateDecoRegistrate reg, DyeColor color
  ) {
    String regName = color.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_shipping_container";

    return reg.block(regName, p -> new ShippingContainerBlock(p, color))
      .initialProperties(SharedProperties::softMetal)
      .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
      .properties(p -> p.mapColor(color))
      .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).explosionResistance(1200))
      .transform(pickaxeOnly())
      .item(ShippingContainerBlockItem::new).build()
      //.tag(BlockTags.MINEABLE_WITH_PICKAXE) //shipping containers are not registered, adding them to tag breaks a lot of things
      .lang(color.name().charAt(0) + color.name().substring(1).toLowerCase()
          .replaceAll("_", " ") + " Shipping Container")
      .blockstate((ctx, prov) -> BlockStateGenerator.shippingContainer(CreateDecoMod.REGISTRATE, color, ctx, prov))
      .onRegister(connectedTextures(ShippingContainerCTBehavior::new));
  }

  public static <T extends Block> void recipeCrafting (DyeColor color, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov) {
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
      .pattern("CS")
      .pattern("SB")
      .define('S', AllItems.IRON_SHEET)
      .define('C', DyeItem.byColor(color))
      .define('B', Items.BARREL)
      .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
        ItemPredicate.Builder.item().of(AllItems.IRON_SHEET).build()
      ))
      .save(prov, color.getName() + "_shipping_container");
  }

  public static <T extends Block> void recipeDyeing (DyeColor color, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
      .requires(DyeItem.byColor(color))
      .requires(AllBlocks.ITEM_VAULT.asItem())
      .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
        ItemPredicate.Builder.item().of(AllBlocks.ITEM_VAULT.asItem()).build()
      ))
      .save(prov, color.getName() + "_shipping_container_from_dyeing_vaults");
  }
}
