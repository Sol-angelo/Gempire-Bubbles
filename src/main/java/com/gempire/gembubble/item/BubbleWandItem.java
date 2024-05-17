package com.gempire.gembubble.item;

import com.gempire.gembubble.entity.EntityBubble;
import com.gempire.gembubble.init.ModEntities;
import com.gempire.gembubble.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class BubbleWandItem extends Item {

    public BubbleWandItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            EntityHitResult util = ProjectileUtil.getEntityHitResult(level, player, player.position(), player.position().add(player.getDeltaMovement()), player.getBoundingBox().inflate(30D), this::isEntityValid);
            ItemEntity item = (ItemEntity) util.getEntity();
            Vec3 pos = util.getLocation();
            EntityBubble bubble = new EntityBubble(ModEntities.BUBBLE.get(), level);
            level.addFreshEntity(bubble);
            bubble.setPos(pos);
            bubble.setItem(item.getItem());
            bubble.owner = player.getUUID();
            bubble.setColor(player.getRandom().nextInt(15));
            bubble.playSound(ModSounds.BUBBLE.get());
        }
        return super.use(level, player, hand);
    }

    public boolean isEntityValid(Entity entity) {
        return entity instanceof ItemEntity;
    }
}