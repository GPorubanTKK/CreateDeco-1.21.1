package com.rld.createdeco;

import com.rld.createdeco.common.BlockRegistry;
import com.rld.createdeco.common.ItemRegistry;
import com.rld.createdeco.common.data.CreateDecoPressingRecipeGen;
import com.rld.createdeco.common.registration.CreateDecoRegistrate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Mod(CreateDecoMod.MOD_ID)
@EventBusSubscriber
public class CreateDecoMod {
  public static final String MOD_ID = "createdeco";
  public static final String NAME = "Create Deco";
  public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

  public static final CreateDecoRegistrate REGISTRATE = CreateDecoRegistrate.create(MOD_ID).skipErrors(true);

  public CreateDecoMod() {
    IEventBus bus = ModLoadingContext.get().getActiveContainer().getEventBus();
    CreativeTabs.register(bus);
    CreateDecoMod.REGISTRATE.registerEventListeners(bus);
    init();
  }

  public static void init() {
    ItemRegistry.init();
    BlockRegistry.init();
  }

  @SubscribeEvent
  public static void gatherDataEvent(GatherDataEvent gde) {
    DataGenerator gen = gde.getGenerator();
    PackOutput pOut = gen.getPackOutput();
    CompletableFuture<HolderLookup.Provider> prov = gde.getLookupProvider();
    gen.addProvider(true, new DataProvider() {
      @Override
      public CompletableFuture<?> run(CachedOutput output) {
        CreateDecoMod.LOGGER.info("Run GDE");
        return new CreateDecoPressingRecipeGen(pOut, prov).run(output);
      }

      @Override
      public String getName() {
        return "Create Deco Pressing";
      }
    });
  }

  public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
