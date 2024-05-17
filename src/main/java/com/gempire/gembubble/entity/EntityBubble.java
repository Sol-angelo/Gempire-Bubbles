package com.gempire.gembubble.entity;

import com.gempire.entities.bases.EntityGem;
import com.gempire.gembubble.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.UUID;

public class EntityBubble extends Entity {

    private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(EntityBubble.class, EntityDataSerializers.ITEM_STACK);
    public static EntityDataAccessor<Integer> COLOR = SynchedEntityData.<Integer>defineId(EntityBubble.class, EntityDataSerializers.INT);

    public UUID owner;

    public EntityBubble(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.entityData.define(EntityBubble.COLOR, 5);
        this.entityData.define(EntityBubble.ITEM, Items.ALLIUM.getDefaultInstance());
        owner = UUID.randomUUID();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setColor(tag.getInt("color"));
        CompoundTag itemTag = tag.getCompound("item");
        setItem(ItemStack.of(itemTag));
        if (!tag.getString("uuid").isEmpty()) {
            owner = UUID.fromString(tag.getString("uuid"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("color", getColor());
        tag.put("item", getItem().save(tag));
        tag.putString("uuid", owner.toString());
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float p_19947_) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.level.isClientSide && !this.isRemoved()) {
            this.kill();
            return true;
        }
        else return false;
    }

    @Override
    public void kill() {
        this.spawnAtLocation(getItem());
        this.playSound(ModSounds.POP.get());
        super.kill();
    }

    public void sendHome(Player owner, Level level) {
        System.out.println("send home");
        if (level instanceof ServerLevel) {
            BlockPos pos = ((ServerPlayer) owner).getRespawnPosition();
            ResourceKey<Level> dim = ((ServerPlayer) owner).getRespawnDimension();
            if (dim.equals(this.level.dimension())) {
                this.setPos(pos.getX(), pos.getY(), pos.getZ());
                playSound(ModSounds.SEND.get());
            } else {
                System.out.println("not correct dim");
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.level.isClientSide) {
            return InteractionResult.PASS;
        }

        if (hand == InteractionHand.MAIN_HAND) {
            //if (owner == player.getUUID()) {
                if (player.getItemInHand(hand).isEmpty()) {
                    sendHome(player, player.level);
                } else if (player.getMainHandItem().getItem() instanceof DyeItem dye) {
                    setColor(dye.getDyeColor().getId());
                }
            //}
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }

    public int getColor() {
        return this.entityData.get(EntityBubble.COLOR);
    }

    public void setColor(int value) {
        this.entityData.set(EntityBubble.COLOR, value);
    }

    public ItemStack getItem() {
        return this.entityData.get(EntityBubble.ITEM);
    }

    public void setItem(ItemStack value) {
        this.entityData.set(EntityBubble.ITEM, value);
    }
}
