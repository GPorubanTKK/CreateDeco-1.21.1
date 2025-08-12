package com.rld.createdeco.common.api;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CDTagGen {
  public static TagsProvider.TagAppender<Item> append(RegistrateItemTagsProvider prov, TagKey<Item> tag) {
    return prov.addTag(tag);
  }

//  @ExpectPlatform
//  public static <T> TagsProvider.TagAppender append (RegistrateTagsProvider<T> prov, TagKey<T> tag) {
//    throw new AssertionError();
//  }
  public static <T> TagsProvider.TagAppender<T> append (RegistrateTagsProvider<T> prov, TagKey<T> tag) {
    return prov.addTag(tag);
  }
}
