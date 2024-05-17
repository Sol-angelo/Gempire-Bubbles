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
import net.minecraft.world.entity.EntityType;
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
    private final BlockRenderDispatcher blockRenderer;

    public RenderBubble(EntityRendererProvider.Context renderManagerIn, ModelBubble<EntityBubble> baseModel) {
        super(renderManagerIn);
        this.itemRenderer = renderManagerIn.getItemRenderer();
        this.blockRenderer = renderManagerIn.getBlockRenderDispatcher();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBubble entity) {
        return new ResourceLocation(GemBubble.MODID + ":textures/entity/bubble/bubble.png");
    }

    protected int getBlockLightLevel(T p_174216_, BlockPos p_174217_) {
        return p_174216_.getType() == EntityType.GLOW_ITEM_FRAME ? Math.max(5, super.getBlockLightLevel(p_174216_, p_174217_)) : super.getBlockLightLevel(p_174216_, p_174217_);
    }

    @Override
    public void render(T p_115076_, float p_115077_, float p_115078_, PoseStack p_115079_, MultiBufferSource p_115080_, int p_115081_) {
        super.render(p_115076_, p_115077_, p_115078_, p_115079_, p_115080_, p_115081_);
        p_115079_.pushPose();
        Direction direction = p_115076_.getDirection();
        Vec3 vec3 = this.getRenderOffset(p_115076_, p_115078_);
        p_115079_.translate(-vec3.x(), -vec3.y(), -vec3.z());
        double d0 = 0.46875;
        p_115079_.translate((double)direction.getStepX() * 0.46875, (double)direction.getStepY() * 0.46875, (double)direction.getStepZ() * 0.46875);
        p_115079_.mulPose(Axis.XP.rotationDegrees(p_115076_.getXRot()));
        p_115079_.mulPose(Axis.YP.rotationDegrees(180.0F - p_115076_.getYRot()));
        boolean flag = p_115076_.isInvisible();
        ItemStack itemstack = p_115076_.getItem();
        if (!flag) {
            ModelManager modelmanager = this.blockRenderer.getBlockModelShaper().getModelManager();
            p_115079_.pushPose();
            p_115079_.translate(-0.5F, -0.5F, -0.5F);
            this.blockRenderer.getModelRenderer().renderModel(p_115079_.last(), p_115080_.getBuffer(Sheets.solidBlockSheet()), (BlockState)null, modelmanager.getModel(ModelBubble.LAYER_LOCATION.getModel()), 1.0F, 1.0F, 1.0F, p_115081_, OverlayTexture.NO_OVERLAY);
            p_115079_.popPose();
        }

        if (!itemstack.isEmpty()) {
            if (flag) {
                p_115079_.translate(0.0F, 0.0F, 0.5F);
            } else {
                p_115079_.translate(0.0F, 0.0F, 0.4375F);
            }

            if (!MinecraftForge.EVENT_BUS.post(new RenderItemInBubbleEvent(p_115076_, this, p_115079_, p_115080_, p_115081_))) {
                    int k = this.getLightVal(p_115076_, 15728880, p_115081_);
                    p_115079_.scale(0.5F, 0.5F, 0.5F);
                    this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, k, OverlayTexture.NO_OVERLAY, p_115079_, p_115080_, p_115076_.level, p_115076_.getId());
            }
        }

        p_115079_.popPose();
    }

    private int getLightVal(T p_174209_, int p_174210_, int p_174211_) {
        return p_174209_.getType() == EntityType.GLOW_ITEM_FRAME ? p_174210_ : p_174211_;
    }

    public Vec3 getRenderOffset(T p_115073_, float p_115074_) {
        return new Vec3((double)((float)p_115073_.getDirection().getStepX() * 0.3F), -0.25, (double)((float)p_115073_.getDirection().getStepZ() * 0.3F));
    }
}
