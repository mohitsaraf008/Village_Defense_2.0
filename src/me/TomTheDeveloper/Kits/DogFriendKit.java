package me.TomTheDeveloper.Kits;

import me.TomTheDeveloper.Game.GameInstance;
import me.TomTheDeveloper.Handlers.ChatManager;
import me.TomTheDeveloper.Handlers.UserManager;
import me.TomTheDeveloper.InvasionInstance;
import me.TomTheDeveloper.KitAPI.BaseKits.PremiumKit;
import me.TomTheDeveloper.Utils.ArmorHelper;
import me.TomTheDeveloper.Utils.Util;
import me.TomTheDeveloper.Utils.WeaponHelper;
import me.TomTheDeveloper.YoutuberInvasion;
import me.TomTheDeveloper.versions.InvasionInstance1_12_R1;
import me.TomTheDeveloper.versions.InvasionInstance1_7_10;
import me.TomTheDeveloper.versions.InvasionInstance1_8_R3;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Tom on 18/07/2015.
 */
public class DogFriendKit extends PremiumKit {

    private YoutuberInvasion plugin;

    public DogFriendKit(YoutuberInvasion invasion){
        this.plugin = invasion;
        this.setName(ChatManager.getSingleMessage("Dog-Friend-Kit-Name",ChatManager.HIGHLIGHTED + "Dog Friend"));
        List<String> description = Util.splitString(ChatManager.getSingleMessage("Dog-Friend-Kit-Description", "Start off with three dogs and get one extra dog every wave!!"), 40);
        this.setDescription(description.toArray(new String[description.size()]));

    }


    @Override
    public boolean isUnlockedByPlayer(Player player) {
        if(UserManager.getUser(player.getUniqueId()).isPremium() || player.hasPermission("villagedefense.kit.dogfriend"))
            return true;
        return false;
    }

    @Override
    public void giveKitItems(Player player) {

        player.getInventory().addItem(WeaponHelper.getUnBreakingSword(WeaponHelper.ResourceType.STONE, 10));
        ArmorHelper.setArmor(player, ArmorHelper.ArmorType.LEATHER);
        player.getInventory().addItem(new ItemStack(Material.GRILLED_PORK, 8));
        player.getInventory().addItem(new ItemStack(Material.SADDLE));
        GameInstance gameInstance = plugin.getGameAPI().getGameInstanceManager().getGameInstance(player);
        if(gameInstance == null)
            return;
        if(!(gameInstance instanceof InvasionInstance)){
            return;
        }
        if(plugin.is1_8_R3()){
            InvasionInstance1_8_R3 invasionInstance1_8_r3 = (InvasionInstance1_8_R3) gameInstance;
            invasionInstance1_8_r3.spawnWolf(invasionInstance1_8_r3.getStartLocation(), player);
            invasionInstance1_8_r3.spawnWolf(invasionInstance1_8_r3.getStartLocation(), player);
            invasionInstance1_8_r3.spawnWolf(invasionInstance1_8_r3.getStartLocation(), player);
        }
        if(plugin.is1_12_R1()){
            InvasionInstance1_12_R1 invasionInstance1_8_r3 = (InvasionInstance1_12_R1) gameInstance;
            invasionInstance1_8_r3.spawnWolf(invasionInstance1_8_r3.getStartLocation(), player);
            invasionInstance1_8_r3.spawnWolf(invasionInstance1_8_r3.getStartLocation(), player);
            invasionInstance1_8_r3.spawnWolf(invasionInstance1_8_r3.getStartLocation(), player);
        }
    }

    @Override
    public Material getMaterial() {
        return Material.BONE;
    }

    @Override
    public void reStock(Player player) {
        GameInstance gameInstance = plugin.getGameAPI().getGameInstanceManager().getGameInstance(player);
        if(gameInstance == null)
            return;
        if(!(gameInstance instanceof InvasionInstance)){
            return;
        }
        if(plugin.is1_8_R3()){
            InvasionInstance1_8_R3 invasionInstance1_8_r3 = (InvasionInstance1_8_R3) gameInstance;
            invasionInstance1_8_r3.spawnWolf(invasionInstance1_8_r3.getStartLocation(), player);
        }
        if(plugin.is1_12_R1()){
            InvasionInstance1_12_R1 invasionInstance1_12_R1 = (InvasionInstance1_12_R1) gameInstance;
            invasionInstance1_12_R1.spawnWolf(invasionInstance1_12_R1.getStartLocation(), player);
        }
    }
}
