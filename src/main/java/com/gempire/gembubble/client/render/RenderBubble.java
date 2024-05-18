package com.gempire.gembubble.client.render;

import com.gempire.Gempire;
import com.gempire.client.entity.model.ModelQuartz;
import com.gempire.entities.gems.EntityAgate;
import com.gempire.gembubble.GemBubble;
import com.gempire.gembubble.client.model.ModelBubble;
import com.gempire.gembubble.entity.EntityBubble;
import com.gempire.gembubble.events.RenderItemInBubbleEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.common.MinecraftForge;

public class RenderBubble<T extends EntityBubble> extends EntityRenderer<T> {

    private final ItemRenderer itemRenderer;

    private ModelBubble modelBubble;

    public RenderBubble(EntityRendererProvider.Context renderManagerIn, ModelBubble<EntityBubble> baseModel) {
        super(renderManagerIn);
        this.itemRenderer = renderManagerIn.getItemRenderer();
        modelBubble = baseModel;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBubble entity) {
        return new ResourceLocation(GemBubble.MODID + ":textures/entity/bubble/bubble.png");
    }

    protected int getBlockLightLevel(T p_174216_, BlockPos p_174217_) {
        return p_174216_.getType() == EntityType.GLOW_ITEM_FRAME ? Math.max(5, super.getBlockLightLevel(p_174216_, p_174217_)) : super.getBlockLightLevel(p_174216_, p_174217_);
    }

    @Override
    public void render(T bubble, float p_115077_, float p_115078_, PoseStack stack, MultiBufferSource bufferSource, int p_115081_) {
        super.render(bubble, p_115077_, p_115078_, stack, bufferSource, p_115081_);
        stack.pushPose();
        float[] bubbleColours = Sheep.getColorArray(DyeColor.byId(bubble.getColor()));
        Direction direction = bubble.getDirection();
        Vec3 vec3 = this.getRenderOffset(bubble, p_115078_);
        stack.translate(-vec3.x(), -vec3.y(), -vec3.z());
        //double d0 = 0.46875;
        stack.translate((double)direction.getStepX() * 0.46875, (double)direction.getStepY() * 0.46875, (double)direction.getStepZ() * 0.46875);
        stack.mulPose(Axis.XP.rotationDegrees(bubble.getXRot()));
        stack.mulPose(Axis.YP.rotationDegrees(180.0F - bubble.getYRot()));
        ItemStack itemstack = bubble.getItem();
        if (!itemstack.isEmpty()) {
            stack.translate(0.0F, 0.4375F, 0.4375F);

            if (!MinecraftForge.EVENT_BUS.post(new RenderItemInBubbleEvent(bubble, this, stack, bufferSource, p_115081_))) {
                int k = p_115081_;
                stack.scale(0.6F, 0.6F, 0.6F);
                this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, k, OverlayTexture.NO_OVERLAY, stack, bufferSource, bubble.level, 0);
            }

            stack.scale(1.66666f + 0.25f, 1.66666f+ 0.25f, 1.66666f+ 0.25f);
            stack.translate(0, -0.4375f, 0);
        }
        stack.translate(0, -.9375F - 0.2, 0);
        modelBubble.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(bubble))), p_115081_, OverlayTexture.NO_OVERLAY, bubbleColours[0], bubbleColours[1], bubbleColours[2], 0.5f);



        stack.popPose();
    }

    public Vec3 getRenderOffset(T p_115073_, float p_115074_) {
        return new Vec3((double)((float)p_115073_.getDirection().getStepX() * 0.3F), -0.25, (double)((float)p_115073_.getDirection().getStepZ() * 0.3F));
    }
}
