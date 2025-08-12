package com.rld.createdeco.common.api;

import com.rld.createdeco.BlockStateGenerator;
import com.rld.createdeco.common.blocks.MeshFenceBlock;
import com.rld.createdeco.common.registration.CreateDecoRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class MeshFences {
  public static <T extends Block> void fenceRecipe (
      String metal,
      DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 16)
        .pattern("psp")
        .pattern("psp")
        .define('p', CDTags.of(metal, "plates").tag)
        .define('s', Items.STRING)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
        ))
        .save(prov);
  }

  public static BlockBuilder<MeshFenceBlock,?> build (CreateDecoRegistrate reg, String metal) {
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_mesh_fence", MeshFenceBlock::new)
      .properties(props-> props.strength(5, 6).requiresCorrectToolForDrops()
        .sound(SoundType.CHAIN)
      )
      .addLayer(()-> RenderType::translucent)
      .tag(BlockTags.FENCES)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .item().tag(ItemTags.FENCES)
      .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
      .model((ctx,prov)-> prov.singleTexture(
        ctx.getName(), prov.mcLoc("item/generated"),
        "layer0", prov.modLoc("block/palettes/chain_link_fence/" + metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_chain_link")))
      .build()
      .blockstate((ctx,prov)-> BlockStateGenerator.fence(metal, ctx, prov));
  }
}
