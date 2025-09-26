package com.rld.createtfa.common.api;

import com.rld.createtfa.BlockStateGenerator;
import com.rld.createtfa.common.blocks.FacadeBlock;
import com.rld.createtfa.common.registration.CreateTFARegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class Facades {
  public static BlockBuilder<FacadeBlock,?> build (
      CreateTFARegistrate reg, String metal
  ) {
    String regName = metal.toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "_facade";

    return reg.block(regName, FacadeBlock::new)
      .properties(props -> props.strength(5, 6)
        .requiresCorrectToolForDrops()
        .sound(SoundType.NETHERITE_BLOCK)
        .noOcclusion()
        .isViewBlocking((a, b, c) -> false)
        .isSuffocating((a, b, c) -> false)
      )
      .addLayer(() -> RenderType::translucent)
      .item()
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .blockstate(BlockStateGenerator::defaultModel)
      .lang(metal + " Facade");
  }
}
