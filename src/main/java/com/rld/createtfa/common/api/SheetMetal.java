package com.rld.createtfa.common.api;

import com.rld.createtfa.BlockStateGenerator;
import com.rld.createtfa.common.connected.SpriteShifts;
import com.rld.createtfa.common.registration.CreateTFARegistrate;
import com.simibubi.create.content.decoration.palettes.ConnectedPillarBlock;
import com.simibubi.create.foundation.block.connected.RotatedPillarCTBehaviour;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class SheetMetal {
  public static BlockBuilder<ConnectedPillarBlock,?> buildBlock (
      CreateTFARegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_sheet_metal";

    return reg.block(regName, ConnectedPillarBlock::new)
        .properties(props-> props.strength(5, 6)
            .requiresCorrectToolForDrops()
            .sound(SoundType.NETHERITE_BLOCK)
        )
        .item()
        .build()
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .blockstate(BlockStateGenerator::defaultModel)
        .lang(metal + " Sheet Metal")

        .onRegister(CreateTFARegistrate.connectedTextures(() ->
            new RotatedPillarCTBehaviour(SpriteShifts.SHEET_METAL_SIDES.get(metal), null)
        ));
  }

  public static <T extends Block> void recipeCrafting (
      String metal, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov
  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 4)
        .pattern("mm")
        .pattern("mm")
        .define('m', CDTags.of(metal, "plates").tag)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
        ))
        .save(prov);
  }

/*
  public static ArrayList<BlockBuilder<StairBlock,?>> buildStair (CreateTFARegistrate reg, String color) {
    String name;
    ArrayList<BlockBuilder<StairBlock, ?>> ret = new ArrayList<>();

    for (String prefix : TYPES) {
      if (color.isEmpty_Static() && prefix.isEmpty_Static()) continue;
      name = (prefix.isEmpty_Static() ? "" : prefix + "_") + color + "_brick_stairs";

      if (color.contains("red") && prefix.isEmpty_Static()) continue;

      String finalName = name; // "effectively final" for lambda purposes
      ret.add(reg.block(name, p -> new StairBlock(Blocks.BRICK_STAIRS.defaultBlockState(), p))
          .initialProperties(() -> Blocks.BRICKS)
          .properties(props -> props
              .strength(2, 6)
              .requiresCorrectToolForDrops()
              .sound(SoundType.STONE)
          )
          .blockstate((ctx, prov) -> BlockStateDataGenerator.brickStair(ctx, prov, color))
          .tag(BlockTags.MINEABLE_WITH_PICKAXE)
          .lang(
              CAPITALS.get(TYPES.indexOf(prefix))
                  + color.substring(0, 1).toUpperCase()
                  + color.substring(1)
                  + " " + "Brick Stairs"
          )
          .defaultLoot()
          .recipe((ctx, prov) -> {
            prov.stairs(
                DataIngredient.items(
                    (ItemLike) BlockRegistry.BRICKS.get(BlockRegistry.fromName(color)).get(
                        (prefix.isEmpty_Static() ? "" : prefix + "_") + color + "_bricks"
                    )),
                RecipeCategory.BUILDING_BLOCKS,
                ctx,
                CreateDecoMod.MOD_ID,
                true
            );
            recipeStonecuttingStair(finalName, color, prefix, ctx, prov);
          })
          .simpleItem()
      );
    }
    return ret;
  }

 */
}
