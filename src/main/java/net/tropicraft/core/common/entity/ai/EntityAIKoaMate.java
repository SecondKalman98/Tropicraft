package net.tropicraft.core.common.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.capability.WorldDataInstance;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

import java.util.List;

public class EntityAIKoaMate extends EntityAIBase
{

    /**
     * TODO: potential problems from vanilla one
     * - mate isnt set isMating
     * - does not check if already mating
     */

    private final EntityKoaBase villagerObj;
    private EntityKoaBase mate;
    private final World world;
    private int matingTimeout;

    public EntityAIKoaMate(EntityKoaBase villagerIn)
    {
        this.villagerObj = villagerIn;
        this.world = villagerIn.world;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.villagerObj.getGrowingAge() != 0)
        {
            return false;
        }
        else if (this.villagerObj.getRNG().nextInt(500) != 0)
        {
            return false;
        }
        else
        {
            if (this.canTownHandleMoreVillagers() && this.villagerObj.getIsWillingToMate(true)) {
                List<EntityKoaBase> listEntities = this.world.getEntitiesWithinAABB(EntityKoaBase.class, this.villagerObj.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D));
                EntityKoaBase clEnt = null;
                double clDist = 9999;
                for (EntityKoaBase ent : listEntities) {
                    if (ent != villagerObj) {
                        if (villagerObj.willBone(ent)) {
                            if (villagerObj.getDistanceToEntity(ent) < clDist) {
                                clEnt = ent;
                                clDist = villagerObj.getDistanceToEntity(ent);
                            }
                        }
                    }
                }
                if (clEnt != null) {
                    this.mate = clEnt;
                    System.out.println("start mate");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.matingTimeout = 300;
        this.villagerObj.setMating(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.mate = null;
        this.villagerObj.setMating(false);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.matingTimeout >= 0 && this.canTownHandleMoreVillagers() && this.villagerObj.getGrowingAge() == 0 && this.villagerObj.getIsWillingToMate(false);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        --this.matingTimeout;
        this.villagerObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);

        if (this.villagerObj.getDistanceSqToEntity(this.mate) > 2.25D)
        {
            this.villagerObj.getNavigator().tryMoveToEntityLiving(this.mate, 0.25D);
        }
        else if (this.matingTimeout == 0 && this.mate.isMating())
        {
            System.out.println("mate complete");
            if (villagerObj.getOrientation() == EntityKoaBase.Orientations.STRAIT) {
                this.giveBirth();
            }
        }

        if (this.villagerObj.getRNG().nextInt(35) == 0)
        {
            this.world.setEntityState(this.villagerObj, (byte)12);
        }
    }

    private boolean canTownHandleMoreVillagers()
    {
        WorldDataInstance data = villagerObj.world.getCapability(Tropicraft.WORLD_DATA_INSTANCE, null);
        if (data != null) {

        } else {
            return false;
        }
        return true;
    }

    private void giveBirth()
    {
        net.minecraft.entity.EntityAgeable entityvillager = this.villagerObj.createChild(this.mate);
        this.mate.setGrowingAge(6000);
        this.villagerObj.setGrowingAge(6000);
        this.mate.setIsWillingToMate(false);
        this.villagerObj.setIsWillingToMate(false);

        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(villagerObj, mate, entityvillager);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event) || event.getChild() == null) { return; }
        entityvillager = event.getChild();
        entityvillager.setGrowingAge(-24000);
        entityvillager.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
        if (entityvillager instanceof EntityKoaBase) {
            ((EntityKoaBase) entityvillager).setVillageID(villagerObj.getVillageID());
            entityvillager.setHomePosAndDistance(villagerObj.getHomePosition(), EntityKoaBase.MAX_HOME_DISTANCE);
        }
        this.world.spawnEntity(entityvillager);
        this.world.setEntityState(entityvillager, (byte)12);
    }
}