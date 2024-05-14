package com.gempire.gembubble.events;

import com.gempire.gembubble.client.render.RenderBubble;
import com.gempire.gembubble.entity.EntityBubble;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;

@Cancelable
public class RenderItemInBubbleEvent  extends Event {
    private final ItemStack itemStack;
    private final EntityBubble itemFrameEntity;
    private final RenderBubble<?> renderer;
    private final PoseStack poseStack;
    private final MultiBufferSource multiBufferSource;
    private final int packedLight;

    @ApiStatus.Internal
    public RenderItemInBubbleEvent(EntityBubble itemFrame, RenderBubble<?> renderItemFrame, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        this.itemStack = itemFrame.getItem();
        this.itemFrameEntity = itemFrame;
        this.renderer = renderItemFrame;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.packedLight = packedLight;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public EntityBubble getItemFrameEntity() {
        return this.itemFrameEntity;
    }

    public RenderBubble<?> getRenderer() {
        return this.renderer;
    }

    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public MultiBufferSource getMultiBufferSource() {
        return this.multiBufferSource;
    }

    public int getPackedLight() {
        return this.packedLight;
    }
}
