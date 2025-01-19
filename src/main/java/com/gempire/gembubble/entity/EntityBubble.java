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
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.*;
import java.util.UUID;

public class EntityBubble extends Entity {

    private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(EntityBubble.class, EntityDataSerializers.ITEM_STACK);
    public static EntityDataAccessor<Integer> COLOR = SynchedEntityData.<Integer>defineId(EntityBubble.class, EntityDataSerializers.INT);

    public UUID owner;

    public EntityBubble(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        owner = UUID.randomUUID();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(EntityBubble.COLOR, 5);
        this.entityData.define(EntityBubble.ITEM, Items.AIR.getDefaultInstance());
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
        tag.put("item", getItem().save(new CompoundTag()));
        tag.putString("uuid", owner.toString());
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
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
        }
        this.level().playLocalSound(this.getOnPos(), ModSounds.POP.get(), SoundSource.PLAYERS, 2f, 1, false);
        if (!this.level().isClientSide && !this.isRemoved()) {
            this.spawnAtLocation(getItem());
            this.kill();
            return true;
        }
        else return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public void sendHome(Player owner, Level level) {
        System.out.println("send home");
        this.level().playSound(this, this.getOnPos(), ModSounds.SEND.get(), SoundSource.PLAYERS, 2f, 1);
        if (level instanceof ServerLevel && owner instanceof ServerPlayer) {
            if (((ServerPlayer) owner).getRespawnPosition() != null) {
                int x = ((ServerPlayer) owner).getRespawnPosition().getX();
                int y = ((ServerPlayer) owner).getRespawnPosition().getY() + 1;
                int z = ((ServerPlayer) owner).getRespawnPosition().getZ();
                this.teleportTo(x, y, z);
            } else {
                this.teleportTo(level.getSharedSpawnPos().getX(), level.getSharedSpawnPos().getY(), level.getSharedSpawnPos().getZ());
            }
        }
    }

    @Override
    public void push(Entity p_20293_) {
        super.push(p_20293_);
        if (!this.isPassengerOfSameVehicle(p_20293_) && !p_20293_.noPhysics && !this.noPhysics) {
            double d0 = p_20293_.getX() - this.getX();
            double d1 = p_20293_.getZ() - this.getZ();
            double d2 = Mth.absMax(d0, d1);
            if (d2 >= 0.009999999776482582) {
                d2 = Math.sqrt(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0 / d2;
                if (d3 > 1.0) {
                    d3 = 1.0;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806;
                d1 *= 0.05000000074505806;
                this.push(-d0, 0.0, -d1);
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.level().isClientSide) {
            return InteractionResult.PASS;
        }

        if (hand == InteractionHand.MAIN_HAND) {
            if (owner == player.getUUID()) {
                if (player.getItemInHand(hand).isEmpty()) {
                    sendHome(player, player.level());
                } else if (player.getMainHandItem().getItem() instanceof DyeItem dye) {
                    setColor(dye.getDyeColor().getId());
                }
            }
        }

        return InteractionResult.CONSUME;
    }

    public float getGroundFriction() {
        AABB aabb = this.getBoundingBox();
        AABB aabb1 = new AABB(aabb.minX, aabb.minY - 0.001D, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ);
        int i = Mth.floor(aabb1.minX) - 1;
        int j = Mth.ceil(aabb1.maxX) + 1;
        int k = Mth.floor(aabb1.minY) - 1;
        int l = Mth.ceil(aabb1.maxY) + 1;
        int i1 = Mth.floor(aabb1.minZ) - 1;
        int j1 = Mth.ceil(aabb1.maxZ) + 1;
        VoxelShape voxelshape = Shapes.create(aabb1);
        float f = 0.0F;
        int k1 = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int l1 = i; l1 < j; ++l1) {
            for(int i2 = i1; i2 < j1; ++i2) {
                int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
                if (j2 != 2) {
                    for(int k2 = k; k2 < l; ++k2) {
                        if (j2 <= 0 || k2 != k && k2 != l - 1) {
                            blockpos$mutableblockpos.set(l1, k2, i2);
                            BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
                            if (!(blockstate.getBlock() instanceof WaterlilyBlock) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level(), blockpos$mutableblockpos).move((double)l1, (double)k2, (double)i2), voxelshape, BooleanOp.AND)) {
                                f += blockstate.getFriction(this.level(), blockpos$mutableblockpos, this);
                                ++k1;
                            }
                        }
                    }
                }
            }
        }
        return f / (float)k1;
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            this.move(MoverType.SELF, this.getDeltaMovement());

            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x * (double) this.getGroundFriction(), vec3.y, vec3.z * (double) this.getGroundFriction());

            Vec3 vec32 = this.getDeltaMovement();
            if (!this.onGround()) {
                this.setDeltaMovement(vec32.x, -100, vec32.y);
            }
            if (!this.isNoGravity()) {
                double d0 = this.isInWater() ? -0.005D : -0.02D;
                this.setDeltaMovement(vec32.x, -100, vec32.y);
            }
        }
    }

    @Override
    public boolean onGround() {
        return !level().getBlockState(this.getOnPos()).is(Blocks.AIR);
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
