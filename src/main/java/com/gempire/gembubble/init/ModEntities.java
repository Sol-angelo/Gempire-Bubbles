package com.gempire.gembubble.init;

import com.gempire.Gempire;
import com.gempire.gembubble.entity.EntityBubble;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Gempire.MODID);

    public static final RegistryObject<EntityType<EntityBubble>> BUBBLE = ENTITIES.register("bubble",
            () -> EntityType.Builder.of(EntityBubble::new, MobCategory.MISC)
                    .sized(0.6F, 0.6F) // Hitbox Size
                    .build(new ResourceLocation(Gempire.MODID, "bubble").toString()));


}
