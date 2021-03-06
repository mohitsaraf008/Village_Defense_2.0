package me.TomTheDeveloper.Kits;

import me.TomTheDeveloper.Handlers.ChatManager;
import me.TomTheDeveloper.Handlers.UserManager;
import me.TomTheDeveloper.KitAPI.BaseKits.PremiumKit;
import me.TomTheDeveloper.User;
import me.TomTheDeveloper.Utils.ArmorHelper;
import me.TomTheDeveloper.Utils.Util;
import me.TomTheDeveloper.Utils.WeaponHelper;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Tom on 27/08/2014.
 */
public class ShotBowKit extends PremiumKit implements Listener {


    public ShotBowKit() {
        List<String> description = Util.splitString(ChatManager.getFromLanguageConfig("ShotBow-Kit-Description", "You invented " +
                "a crazy shotbow!"), 40);
        this.setDescription(description.toArray(new String[description.size()]));
        setName(ChatManager.getFromLanguageConfig("Shotbow-Kit-Name",ChatManager.HIGHLIGHTED + "Shotbow Master"));

    }

    @Override
    public boolean isUnlockedByPlayer(Player player) {
        if(UserManager.getUser(player.getUniqueId()).isPremium() || player.hasPermission("villagedefense.kit.shotbow"))
            return true;
        return false;
    }

    @Override
    public void giveKitItems(Player player) {
       player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
       player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
       player.getInventory().addItem(WeaponHelper.getEnchantedBow(Enchantment.DURABILITY, 10));
        ArmorHelper.setColouredArmor(Color.YELLOW, player);
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 8));
        player.getInventory().addItem(new ItemStack(Material.SADDLE));

    }

    @Override
    public Material getMaterial() {
        return Material.ARROW;
    }

    @Override
    public void reStock(Player player) {
        player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
    }

    @EventHandler
    public void ShootArrow(PlayerInteractEvent e){
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
            if(e.getPlayer().getItemInHand() != null){
                if(e.getPlayer().getItemInHand().getType() == Material.BOW ){
                    if(e.getPlayer().getInventory().contains(Material.ARROW) && UserManager.getUser(e.getPlayer().getUniqueId()).getKit() instanceof ShotBowKit && !UserManager.getUser(e.getPlayer().getUniqueId()).isSpectator()){
                        if( UserManager.getUser(e.getPlayer().getUniqueId()).getCooldown("shotbow") == 0){
                           for(int i = 0; i<4;i++) {
                               Projectile pr = e.getPlayer().launchProjectile(Arrow.class);
                               pr.setVelocity(e.getPlayer().getLocation().getDirection().multiply(3));

                               if(e.getPlayer().getInventory().contains(Material.ARROW))
                                    e.getPlayer().getInventory().removeItem(new ItemStack(Material.ARROW, 1));
                           }
                            e.setCancelled(true);
                            UserManager.getUser(e.getPlayer().getUniqueId()).setCooldown("shotbow", 5);
                        }else{
                           e.getPlayer().sendMessage(ChatColor.RED + "Ability on cooldown for " + UserManager.getUser(e.getPlayer().getUniqueId()).getCooldown("shotbow") + " more seconds!");
                        }
                    }
                }
            }
        }
    }




}
