package com.gempire.gembubble.entity;

import com.gempire.entities.bases.EntityGem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class EntityBubble extends Entity {

    private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(EntityBubble.class, EntityDataSerializers.ITEM_STACK);
    public static EntityDataAccessor<Integer> COLOR = SynchedEntityData.<Integer>defineId(EntityBubble.class, EntityDataSerializers.INT);

    public EntityBubble(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(EntityBubble.COLOR, 5);
        this.entityData.define(EntityBubble.ITEM, Items.ALLIUM.getDefaultInstance());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setColor(tag.getInt("color"));
        CompoundTag itemTag = tag.getCompound("item");
        setItem(ItemStack.of(itemTag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("color", getColor());
        tag.put("item", getItem().save(tag));
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
