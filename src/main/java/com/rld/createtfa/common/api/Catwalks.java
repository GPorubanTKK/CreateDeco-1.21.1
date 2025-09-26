package com.rld.createtfa.common.api;

import com.rld.createtfa.common.registration.BlockRegistry;
import com.rld.createtfa.BlockStateGenerator;
import com.rld.createtfa.common.blocks.CatwalkBlock;
import com.rld.createtfa.common.blocks.CatwalkRailingBlock;
import com.rld.createtfa.common.blocks.CatwalkStairBlock;
import com.rld.createtfa.common.connected.CatwalkCTBehaviour;
import com.rld.createtfa.common.connected.SpriteShifts;
import com.rld.createtfa.common.items.CatwalkBlockItem;
import com.rld.createtfa.common.items.CatwalkStairBlockItem;
import com.rld.createtfa.common.items.RailingBlockItem;
import com.rld.createtfa.common.registration.CreateTFARegistrate;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Locale;
import java.util.function.Supplier;


public class Catwalks {

  public static BlockBuilder<CatwalkBlock,?> build (
      CreateTFARegistrate reg, String metal
  ) {
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk", CatwalkBlock::new)
      .properties(props->
        props.strength(5, 6).requiresCorrectToolForDrops().noOcclusion()
          .sound(SoundType.NETHERITE_BLOCK)
      )

      .loot((table, block) -> {
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder catwalksPool     = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        LootPool.Builder supportPool     = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        LootItem.Builder<?> catwalks = LootItem.lootTableItem(block);
        catwalks.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

        catwalksPool.add(catwalks);
        table.add(block, builder.withPool(catwalksPool).withPool(supportPool));
      })
      .addLayer(() -> RenderType::cutoutMipped)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
      .item(CatwalkBlockItem::new)
      .properties(p -> (metal.equals("Netherite")) ? p.fireResistant() : p)
      .model(BlockStateGenerator::catwalkItem)
      .build()
      .blockstate(BlockStateGenerator::defaultModel)
      .onRegister(CreateTFARegistrate.connectedTextures(
        new CatwalkCTBehaviour(SpriteShifts.CATWALK_TOPS.get(metal)).getSupplier()
      ));
  }

  public static BlockBuilder<CatwalkStairBlock,?> buildStair (
      CreateTFARegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    String texture = reg.getModid() + ":block/palettes/catwalks/" + regName + "_catwalk";
    return reg.block(metal.toLowerCase(Locale.ROOT)
      .replaceAll(" ", "_") + "_catwalk_stairs",
      p -> new CatwalkStairBlock(p, metal)
    )
      .properties(props-> props
        .strength(5, 6)
        .requiresCorrectToolForDrops().noOcclusion()
        .sound(SoundType.NETHERITE_BLOCK)
      )
      .loot((table, block) -> {
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder stairsPool     = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        LootPool.Builder railingsPool     = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        LootItem.Builder<?> stairs = LootItem.lootTableItem(block);
        stairs.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

        LootItem.Builder<?> rails = LootItem.lootTableItem(
          BlockRegistry.CATWALK_RAILINGS.get(metal)
        );

        rails.apply(SetItemCountFunction.setCount(ConstantValue.exactly(0)));

        rails.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1), true)
            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                    .hasProperty(CatwalkStairBlock.RAILING_LEFT, true)
                )));
        rails.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1), true)
            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                    .hasProperty(CatwalkStairBlock.RAILING_RIGHT, true)
                )));

        railingsPool.add(rails);
        stairsPool.add(stairs);
        table.add(block, builder.withPool(stairsPool).withPool(railingsPool));
      })
      .addLayer(()-> RenderType::translucent)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
      .blockstate(BlockStateGenerator::defaultModel)
      .item(CatwalkStairBlockItem::new).build();
  }

  public static BlockBuilder<CatwalkRailingBlock,?> buildRailing (
      CreateTFARegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    String texture = reg.getModid() + ":block/palettes/catwalks/" + regName + "_catwalk";
    return reg.block(metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_catwalk_railing", CatwalkRailingBlock::new)
      .properties(props->
        props.strength(5, 6)
          .requiresCorrectToolForDrops().noOcclusion().sound(SoundType.NETHERITE_BLOCK)
      )
      .addLayer(()-> RenderType::translucent)
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
      .blockstate(BlockStateGenerator::defaultModel)
      .recipe((ctx,prov)-> {})
      .loot((table, block) -> {
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder pool     = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        LootItem.Builder<?> entry = LootItem.lootTableItem(block);

        entry.apply(SetItemCountFunction.setCount(ConstantValue.exactly(0)));
        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
          entry.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1), true)
              .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                  .hasProperty(CatwalkRailingBlock.fromDirection(dir), true)
                )));
        }
        pool.add(entry);
        table.add(block, builder.withPool(pool));
      })
      .item(RailingBlockItem::new)
      .build();
  }


  public static <T extends Block> void recipeStonecutting (
      Supplier<Item> ingot, DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov, int count
  ) {
    SingleItemRecipeBuilder.stonecutting(Ingredient.of(ingot.get()), RecipeCategory.DECORATIONS, ctx.get(), count)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(ingot.get()).build()
        ))
        .save(prov, ctx.getName() + "_from_stonecutting");

  }

  public static <T extends Block> void recipeStairs (
      String metal, ItemLike barItem,
      DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov

  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 2)
        .pattern(" c")
        .pattern("cb")
        .define('c', BlockRegistry.CATWALKS.get(metal).get())
        .define('b', barItem)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            BlockRegistry.CATWALKS.get(metal).get()
        ))
        .save(prov);
  }

  public static <T extends Block> void recipeCatwalk (
      String metal, ItemLike barItem,
      DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov

  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 4)
        .pattern(" p ")
        .pattern("pBp")
        .pattern(" p ")
        .define('p', CDTags.of(metal, "plates").tag)
        .define('B', barItem)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
        ))
        .save(prov, ctx.getName() + "_forge");
  }

  public static <T extends Block> void recipeRailing (
      String metal, ItemLike barItem,
      DataGenContext<Block, T> ctx, RegistrateRecipeProvider prov

  ) {
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 8)
        .pattern("ppp")
        .pattern("B B")
        .pattern("B B")
        .define('p', CDTags.of(metal, "plates").tag)
        .define('B', barItem)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(
            ItemPredicate.Builder.item().of(CDTags.of(metal, "plates").tag).build()
        ))
        .save(prov, ctx.getName() + "_forge");
  }
}
