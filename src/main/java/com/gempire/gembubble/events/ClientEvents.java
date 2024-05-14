package com.gempire.gembubble.events;

import com.gempire.Gempire;
import com.gempire.gembubble.GemBubble;
import com.gempire.gembubble.client.model.ModelBubble;
import com.gempire.gembubble.client.render.RenderBubble;
import com.gempire.gembubble.init.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = GemBubble.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onClientSetup(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.BUBBLE.get(), m -> new RenderBubble<>(m, new ModelBubble<>(m.bakeLayer(ModelBubble.LAYER_LOCATION))));
        }

        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ModelBubble.LAYER_LOCATION, ModelBubble::createBodyLayer);
        }
    }
}
