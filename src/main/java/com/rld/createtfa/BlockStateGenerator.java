package com.rld.createtfa;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class BlockStateGenerator {
    public static void barItem(DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov) {
        ConfiguredModel.builder().modelFile(prov.getExistingFile(prov.modLoc(ctx.getName())));
    }

    public static void cageLamp(DataGenContext<Block, ?> ctx, RegistrateBlockstateProvider prov) {
        prov.getVariantBuilder(ctx.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(
            ctx.getName() + (state.getValue(BlockStateProperties.LIT) ? "" : "_off")
        ))).build());
    }

    public static void catwalkItem(DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov) {
        ConfiguredModel.builder().modelFile(prov.getExistingFile(prov.modLoc(ctx.getName())));
    }

    public static void catwalkRailingItem(DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov) {
        prov.withExistingParent(ctx.getName(), prov.modLoc("block/" + ctx.getName()));
    }

    public static void brickWallItem(DataGenContext<Item, BlockItem> ctx, RegistrateItemModelProvider prov) {
        ConfiguredModel.builder().modelFile(prov.getExistingFile(prov.modLoc(ctx.getName())));
    }


    public static void doorItem(DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov) {
        prov.withExistingParent(ctx.getName(), prov.modLoc("block/" + ctx.getName()));
    }

    public static void trapdoorItem(DataGenContext<Item, ?> ctx, RegistrateItemModelProvider prov) {
        prov.withExistingParent(ctx.getName(), prov.modLoc("block/" + ctx.getName()));
    }

    public static void defaultModel(DataGenContext<Block,?> ctx, RegistrateBlockstateProvider prov) {
        ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc(ctx.getName())));
    }
}
