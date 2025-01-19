package com.gempire.gembubble.item;

import com.gempire.gembubble.entity.EntityBubble;
import com.gempire.gembubble.init.ModEntities;
import com.gempire.gembubble.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BubbleWandItem extends Item {

    public BubbleWandItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Nonnull
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (!level.isClientSide && !player.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty()) {
            BlockHitResult util = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            Vec3 pos = util.getLocation();
            ItemStack stack = player.getItemBySlot(EquipmentSlot.OFFHAND);
            if (!player.isCreative()) player.setItemSlot(EquipmentSlot.OFFHAND, Items.AIR.getDefaultInstance());
            EntityBubble bubble = new EntityBubble(ModEntities.BUBBLE.get(), level);
            bubble.setPos(pos);
            bubble.owner = player.getUUID();
            bubble.setColor(player.getRandom().nextInt(15));
            level.playLocalSound(bubble.getOnPos(), ModSounds.BUBBLE.get(), SoundSource.PLAYERS, 2f, 1, false);
            bubble.setItem(stack);
            level.addFreshEntity(bubble);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}