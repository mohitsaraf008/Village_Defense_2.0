package me.TomTheDeveloper.Kits;

import me.TomTheDeveloper.Handlers.ChatManager;
import me.TomTheDeveloper.Handlers.UserManager;
import me.TomTheDeveloper.KitAPI.BaseKits.LevelKit;
import me.TomTheDeveloper.Utils.ArmorHelper;
import me.TomTheDeveloper.Utils.Util;
import me.TomTheDeveloper.Utils.WeaponHelper;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Tom on 18/08/2014.
 */
public class PuncherKit extends LevelKit {

    public PuncherKit() {
        setName(ChatManager.getFromLanguageConfig("Puncher-Kit-Name",ChatManager.PREFIX + "Puncher"));
        List<String> description = Util.splitString(ChatManager.getFromLanguageConfig("Puncher-Kit-Description", "Punch those zombies back" +
                " with your epic shovel! Knockback V is not unknow for u!"), 40);
        this.setDescription(description.toArray(new String[description.size()]));
        setLevel(4);
    }

    @Override
    public boolean isUnlockedByPlayer(Player player) {
        if(UserManager.getUser(player.getUniqueId()).getInt("level") >= this.getLevel() || player.isOp()|| player.hasPermission("villagefense.kit.puncher"))
            return true;
        return false;
    }

    @Override
    public void giveKitItems(Player player) {
        player.getInventory().addItem(WeaponHelper.getEnchanted(new ItemStack(Material.DIAMOND_SPADE), new Enchantment[]{
                Enchantment.DURABILITY, Enchantment.KNOCKBACK, Enchantment.DAMAGE_ALL
                 }, new int[]{10,5,2}));
        ArmorHelper.setColouredArmor(Color.BLACK, player);
        player.getInventory().addItem(WeaponHelper.getEnchantedBow(Enchantment.DURABILITY, 5));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 25));
        player.getInventory().addItem(new ItemStack(Material.GRILLED_PORK, 8));
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_SPADE;
    }

    @Override
    public void reStock(Player player) {

    }
}
