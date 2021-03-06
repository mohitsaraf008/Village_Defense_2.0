package me.TomTheDeveloper.Creatures.v1_7_R4;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tom on 15/08/2014.
 */
public class PlayerBuster extends EntityZombie {

    public int damage;
    private float bw;



    @SuppressWarnings("rawtypes")
    public PlayerBuster(org.bukkit.World world) {
        super(((CraftWorld) world).getHandle());
        this.bw = 1.5F; //Change this to your liking. This is were you set the speed
        this.damage = 15; // set the damage
        //There's also a ton of options of you do this. play around with it


        List goalB = (List) getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
        goalB.clear();
        List goalC = (List) getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
        goalC.clear();
        List targetB = (List) getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        targetB.clear();
        List targetC = (List) getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        targetC.clear();


        ((Navigation)getNavigation()).b(true);

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityHuman.class, (float) (this.bw), false)); // this one to attack human
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityIronGolem.class, (float) this.bw, true));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, (float) this.bw));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F)); // this one to look at human
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true)); // this one to target human
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityIronGolem.class, 0, false));

        this.setHealth(1);


    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

    @Override
    public void setOnFire(int i) {
        // don't set on fire
        //super.setOnFire(i);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (damagesource != null && damagesource.getEntity() != null && damagesource.getEntity().getBukkitEntity().getType() == EntityType.PLAYER) {

            Player golem = (Player) damagesource.getEntity().getBukkitEntity();
            org.bukkit.inventory.ItemStack[] itemStack = new org.bukkit.inventory.ItemStack[]{new org.bukkit.inventory.ItemStack(org.bukkit.Material.ROTTEN_FLESH)};

            Bukkit.getServer().getPluginManager().callEvent(new EntityDeathEvent((LivingEntity) this.getBukkitEntity(), Arrays.asList(itemStack),expToDrop));

            //golem.getWorld().createExplosion(golem.getLocation(), 4);
            org.bukkit.entity.Entity primed= golem.getWorld().spawnEntity(golem.getLocation(), EntityType.PRIMED_TNT);
            this.die();


            return true;

        } else {
            super.damageEntity(damagesource, f);
            return false;
        }
    }
}
