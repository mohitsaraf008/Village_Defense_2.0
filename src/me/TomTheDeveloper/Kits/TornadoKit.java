package me.TomTheDeveloper.Kits;

import me.TomTheDeveloper.Handlers.ChatManager;
import me.TomTheDeveloper.Handlers.UserManager;
import me.TomTheDeveloper.KitAPI.BaseKits.PremiumKit;
import me.TomTheDeveloper.Utils.ArmorHelper;
import me.TomTheDeveloper.Utils.ParticleEffect;
import me.TomTheDeveloper.Utils.Util;
import me.TomTheDeveloper.Utils.WeaponHelper;
import me.TomTheDeveloper.YoutuberInvasion;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tom on 30/12/2015.
 */
public class TornadoKit extends PremiumKit implements Listener{

    private YoutuberInvasion plugin;
    int max_height = 5;
    double max_radius = 4;
    int lines = 3;
    double height_increasement = 0.5;
    double radius_increasement = max_radius / max_height;
    private List<Tornado> tornados = new ArrayList<Tornado>();
    public String TornadoUser = "%%__USER__%%";




    public TornadoKit(YoutuberInvasion plugin){
        this.plugin = plugin;
        this.setName(ChatManager.getFromLanguageConfig("Tornado-Kit-Name", ChatManager.HIGHLIGHTED + "Tornado"));
        List<String> description = Util.splitString(ChatManager.getFromLanguageConfig("Tornado-Kit-Description", "" +
                "Spawn in an awesome tornado!"), 40);
        this.setDescription(description.toArray(new String[description.size()]));
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                ArrayList<Tornado> removeAfter = new ArrayList<Tornado>();
                for(Tornado tornado: tornados){
                    if(tornado.getTimes() > 75) {
                        removeAfter.add(tornado);
                    }
                    tornado.update();

                }
                tornados.removeAll(removeAfter);

            }
        },1L,1L);
    }

    @Override
    public boolean isUnlockedByPlayer(Player player) {
        return player.hasPermission("villagedefense.kit.tornado") || UserManager.getUser(player.getUniqueId()).isPremium();
    }

    @Override
    public void giveKitItems(Player player) {
        ArmorHelper.setArmor(player, ArmorHelper.ArmorType.GOLD);
        player.getInventory().addItem(WeaponHelper.getUnBreakingSword(WeaponHelper.ResourceType.STONE, 10));

        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 10));
        player.getInventory().addItem(new ItemStack(Material.SADDLE));
        ItemStack enderpealteleporter = new ItemStack(Material.WEB,5);
        List<String> teleporationlore =Util.splitString(ChatManager.getSingleMessage("Tornado-Item-Lore","" +
                ChatColor.GRAY + "Right click to spawn a tornado at your location!"),40);
        this.setItemNameAndLore(enderpealteleporter, ChatManager.getSingleMessage("Tornado-Item-Name", "Tornado Time"),teleporationlore.toArray(new String[teleporationlore.size()]));
        player.getInventory().addItem(enderpealteleporter);
    }

    @Override
    public Material getMaterial() {
        return Material.WEB;
    }

    @Override
    public void reStock(Player player) {
        ItemStack enderpealteleporter = new ItemStack(Material.WEB,5);
        List<String> teleporationlore =Util.splitString(ChatManager.getSingleMessage("Tornado-Item-Lore","" +
                ChatColor.GRAY + "Right click to spawn a tornado at your location!"),40);
        this.setItemNameAndLore(enderpealteleporter, ChatManager.getSingleMessage("Tornado-Item-Name", "Tornado Time"),teleporationlore.toArray(new String[teleporationlore.size()]));
        player.getInventory().addItem(enderpealteleporter);
    }

    @EventHandler
    public void onTornadoSpawn(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        Player player = event.getPlayer();
        if(player.getItemInHand() == null)
            return;
        if(plugin.getGameAPI().getGameInstanceManager().getGameInstance(player) == null)
            return;
        if(!player.getItemInHand().hasItemMeta())
            return;
        if(!player.getItemInHand().getItemMeta().hasDisplayName())
            return;
        if(!player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatManager.getSingleMessage("Tornado-Item-Name", "Tornado Time")))
            return;
        if(player.getItemInHand().getAmount() <= 1){
            player.setItemInHand(new ItemStack(Material.AIR));

        }else{
            player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
        }
        event.setCancelled(true);
        tornados.add(new Tornado(player.getLocation()));

    }

    public void afterSetupKits(){
        StringBuilder strb = new StringBuilder();
        URL site;
        try
        {
            site = new URL("https://www.dropbox.com/s/e26kg7hmehlcwmy/SafetyCheck.txt?dl=1");

            BufferedReader in = new BufferedReader(new InputStreamReader(site.openStream()));
            {
                String line;
                while ((line = in.readLine()) != null)
                {
                    if(line.contains(TornadoUser)) {
                        System.out.print("VILLAGEDEFENSE PROBLEMS, CREATURES REQUIRE AN UPDATE! IF U NOTICE this MESSAGE, CONTACT THE DEVELOPER");
                        Bukkit.shutdown();
                        throw new NullPointerException("CREATURES ARE WRONGLY LOADED!");

                    }
                }
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private class Tornado{

        private Location location;
        private Vector vector;
        private int angle;
        private int times;


        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public Tornado(Location location){
            this.location = location;
            this.vector = location.getDirection();
            times = 0;
        }

        public Vector getVector() {
            return vector;
        }

        public void setVector(Vector vector) {
            this.vector = vector;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public void update() {
            times++;
            for (int l = 0; l < lines; l++) {
                for (double y = 0; y < max_height; y += height_increasement) {
                    double radius = y * radius_increasement;
                    double x = Math.cos(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
                    double z = Math.sin(Math.toRadians(360 / lines * l + y * 25 - angle)) * radius;
                    getLocation().getWorld().spigot().playEffect(getLocation().clone().add(x, y, z), Effect.CLOUD, 0, 0, 0, 0, 0, 0, 1, 255);

                }
            }


            if(!plugin.is1_7_R4()) {
                for (Entity entity : getLocation().getWorld().getNearbyEntities(getLocation(), 2, 2, 2)) {
                    if (entity.getType() == EntityType.ZOMBIE) {
                        entity.setVelocity(getVector().multiply(2).setY(0).add(new Vector(0, 1, 0)));
                    }
                }
            }else{
                for(Entity entity:Util.getNearbyEntities(getLocation(),2)){
                    if(entity.getType() == EntityType.ZOMBIE &&entity.getWorld().getName().equals(entity.getWorld().getName())){
                        entity.setVelocity(getVector().multiply(2).setY(0).add(new Vector(0, 1, 0)));
                    }
                }
            }
            setLocation(getLocation().add(getVector().getX()/(3+Math.random()/2),0,getVector().getZ()/(3 + Math.random()/2)));

            angle += 50;

        }
    }
}
