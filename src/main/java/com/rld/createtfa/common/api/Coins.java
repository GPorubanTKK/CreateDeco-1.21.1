package com.rld.createtfa.common.api;

import com.rld.createtfa.BlockStateGenerator;
import com.rld.createtfa.common.blocks.CoinStackBlock;
import com.rld.createtfa.common.items.CoinStackItem;
import com.rld.createtfa.common.registration.CreateTFARegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Locale;


public class Coins {
  public static ItemBuilder<Item,?> buildCoinItem (
      CreateTFARegistrate reg, NonNullSupplier<Item> coinstack, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_coin";
    return reg.item(regName, Item::new)
      .properties(p -> (metal.contains("Netherite")) ? p.fireResistant() : p)
      .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get(), 4)
        .requires(coinstack.get())
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(coinstack.get()))
        .save(prov)
      )
      .lang(metal + " Coin");
  }

  public static ItemBuilder<CoinStackItem,?> buildCoinStackItem (
      CreateTFARegistrate reg, NonNullSupplier<Item> coin, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_coinstack";
    return reg.item(regName, (p)-> new CoinStackItem(p, metal))
      .properties(p -> (metal.contains("Netherite")) ? p.fireResistant() : p)
      .recipe((ctx, prov)-> ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get())
        .requires(coin.get(), 4)
        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(coin.get()))
        .save(prov)
      )
      .lang(metal + " Coin Stack");
  }

  public static BlockBuilder<CoinStackBlock,?> buildCoinStackBlock (
      CreateTFARegistrate reg, NonNullSupplier<Item> material, String metal,
      ResourceLocation side, ResourceLocation bottom, ResourceLocation top
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_coinstack";
    return reg.block(regName, (p)->new CoinStackBlock(p, metal))
      .properties(props -> props.noOcclusion().strength(0.5f).sound(SoundType.CHAIN))
      .blockstate(BlockStateGenerator::defaultModel)
      .addLayer(()-> RenderType::cutoutMipped)
      .lang(metal + "Coin Stack Block")
      .loot((table, block) -> {
        LootTable.Builder builder      = LootTable.lootTable();
        LootPool.Builder pool          = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        for (int layer = 1; layer <= 8; layer++) {
          LootItem.Builder<?> entry = LootItem.lootTableItem(material.get());
          entry.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
            .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(BlockStateProperties.LAYERS, layer)
            )).apply(SetItemCountFunction.setCount(ConstantValue.exactly(layer)));
          pool.add(entry);
        }
        table.add(block, builder.withPool(pool));
      });
  }
}
