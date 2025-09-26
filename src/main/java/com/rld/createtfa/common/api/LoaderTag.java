package com.rld.createtfa.common.api;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.function.Consumer;

// Stores a trio of tags that are used for per-loader tagging
public class LoaderTag<T> {
  public TagKey<T> tag;

  public LoaderTag (TagKey<T> fabric) { this.tag = fabric; }

  public LoaderTag(
      ResourceKey<? extends Registry<T>> registry,
      ResourceLocation fabric
  ) { this(TagKey.create(registry, fabric)); }

  public static <T> LoaderTag<T> standard(
      ResourceKey<? extends Registry<T>> registry,
      String fabric
  ) { return new LoaderTag<T>(registry, ResourceLocation.fromNamespaceAndPath("c", fabric)); }

  public static <T> LoaderTag<T> same (ResourceKey<? extends Registry<T>> registry, String path) {
    return standard(registry, path);
  }

  public LoaderTag<T> genBoth (RegistrateTagsProvider<T> tags, Consumer<TagsProvider.TagAppender<T>> cons) {
    cons.accept(CDTagGen.append(tags, this.tag));
    return this;
  }

  public LoaderTag<T> genCommon (RegistrateTagsProvider<T> tags) {
    CDTagGen.append(tags, tag);
    return this;
  }
}
