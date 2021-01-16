package me.zachary.playerworldedit.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.zachary.playerworldedit.Playerworldedit;
import me.zachary.playerworldedit.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class RightClickListeners implements Listener {
    private Playerworldedit plugin;

    public RightClickListeners(Playerworldedit plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getHand() == EquipmentSlot.OFF_HAND || event.getItem() == null){
            return;
        }
        NBTItem item = new NBTItem(event.getItem());
        if(!item.getBoolean("Is_A_Axe_Playerworldedit")){
            return;
        }
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            plugin.pos1.put(player.getUniqueId(), event.getClickedBlock().getLocation());
            player.sendMessage(ChatUtils.color("&6You have successful set position 1"));
            event.setCancelled(true);
        }
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            plugin.pos2.put(player.getUniqueId(), event.getClickedBlock().getLocation());
            player.sendMessage(ChatUtils.color("&6You have successful set position 2"));
        }
    }
}
