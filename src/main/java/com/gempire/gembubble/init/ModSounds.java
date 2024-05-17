package com.gempire.gembubble.init;

import com.gempire.Gempire;
import com.gempire.gembubble.GemBubble;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Gempire.MODID);

    public static final RegistryObject<SoundEvent> BUBBLE = SOUNDS.register("bubble", () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(GemBubble.MODID, "bubble"), 16));
    public static final RegistryObject<SoundEvent> SEND = SOUNDS.register("send", () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(GemBubble.MODID, "send"), 16));
    public static final RegistryObject<SoundEvent> POP = SOUNDS.register("pop", () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(GemBubble.MODID, "pop"), 16));
}
