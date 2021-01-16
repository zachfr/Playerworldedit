package me.zachary.playerworldedit.commands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.zachary.playerworldedit.Playerworldedit;
import me.zachary.playerworldedit.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.theprogramsrc.supercoreapi.spigot.commands.CommandResult;
import xyz.theprogramsrc.supercoreapi.spigot.commands.SpigotCommand;
import xyz.theprogramsrc.supercoreapi.spigot.utils.SpigotConsole;

import java.util.ArrayList;
import java.util.List;

public class PlayerworldeditCommand extends SpigotCommand {
    private Playerworldedit plugin;

    public PlayerworldeditCommand(Playerworldedit plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCommand() {
        return "playerworldedit";
    }

    @Override
    public CommandResult onPlayerExecute(Player player, String[] strings) {
        if(strings[0].equalsIgnoreCase("give")){
            if(!player.hasPermission("playerworldedit.give")){
                player.sendMessage(ChatUtils.color("&cYou don't have permission to execute this command."));
                return CommandResult.COMPLETED;
            }
            Player target = null;
            if(strings.length >= 2)
                target = Bukkit.getPlayer(strings[1]);
            ItemStack itemAxe = new ItemStack(Material.IRON_AXE);
            ItemMeta itemAxeMeta = itemAxe.getItemMeta();
            itemAxeMeta.setDisplayName(ChatUtils.color("&6&lPlayer world edit wand"));
            itemAxeMeta.setLore(getLore());
            itemAxe.setItemMeta(itemAxeMeta);
            NBTItem nbtAxe = new NBTItem(itemAxe);
            nbtAxe.setBoolean("Is_A_Axe_Playerworldedit", true);
            if(target != null && strings.length >= 2){
                player.sendMessage(ChatUtils.color("&6You have successful give a world edit wand to " + target.getName()));
                target.getInventory().addItem(nbtAxe.getItem());
                target.sendMessage(ChatUtils.color("&6You have receive a world edit wand."));
                return CommandResult.COMPLETED;
            }else if(strings.length >= 2){
                player.sendMessage(ChatUtils.color("&cThe player was not found or is not connected."));
                return CommandResult.COMPLETED;
            }
            player.getInventory().addItem(nbtAxe.getItem());
            player.sendMessage(ChatUtils.color("&6You have successful receive a world edit wand."));
        }else if(strings[0].equalsIgnoreCase("set")){
            if(!player.hasPermission("playerworldedit.set")){
                player.sendMessage(ChatUtils.color("&cYou don't have permission to execute this command."));
                return CommandResult.COMPLETED;
            }
            Location min = plugin.pos1.get(player.getUniqueId());
            Location max = plugin.pos2.get(player.getUniqueId());
            if(min == null || max == null){
                player.sendMessage(ChatUtils.color("&cPlease set position 1 (Left-Click on block) or position 2 (Right-Click on block)."));
                return CommandResult.COMPLETED;
            }
            if(strings[1] == null){
                player.sendMessage(ChatUtils.color("&cPlease enter a block name to set in region."));
                return CommandResult.COMPLETED;
            }
            Material blockToPlace = Material.matchMaterial(strings[1].toUpperCase());
            Integer item = 0;
            for(int x = Math.max(max.getBlockX() - 1, min.getBlockX() + 1); x >= Math.min(min.getBlockX() + 1, max.getBlockX() - 1); x--) {
                for(int y = Math.max(max.getBlockY(), min.getBlockY()); y >= Math.min(min.getBlockY(), max.getBlockY()); y--) {
                    for(int z = Math.max(max.getBlockZ() - 1, min.getBlockZ() + 1); z >= Math.min(min.getBlockZ() + 1, max.getBlockZ() - 1); z--) {
                        World world = min.getWorld();
                        Block block = world.getBlockAt(x, y, z);
                        if(blockToPlace != block.getType())
                            item++;
                    }
                }
            }
            Integer iteminventory = 0;
            for (ItemStack i : player.getInventory().getContents()){
                if(i != null)
                    if(i.isSimilar(new ItemStack(blockToPlace)))
                        iteminventory += i.getAmount();
            }
            if(iteminventory < item){
                player.sendMessage(ChatUtils.color("&cSorry, you don't have enough block to do that. %InInventoryBlock%/%NeedBlock% %BlockName%").replace("%InInventoryBlock%", String.valueOf(iteminventory)).replace("%NeedBlock%", String.valueOf(item)).replace("%BlockName%", blockToPlace.toString().toLowerCase()));
                return CommandResult.COMPLETED;
            }
            World world = min.getWorld();
            for(int x = Math.max(max.getBlockX(), min.getBlockX()); x >= Math.min(min.getBlockX(), max.getBlockX()); x--) {
                for(int y = Math.max(max.getBlockY(), min.getBlockY()); y >= Math.min(min.getBlockY(), max.getBlockY()); y--) {
                    for(int z = Math.max(max.getBlockZ(), min.getBlockZ()); z >= Math.min(min.getBlockZ(), max.getBlockZ()); z--) {
                        Block block = world.getBlockAt(x, y, z);
                        if(blockToPlace != block.getType()){
                            block.setType(blockToPlace);
                            ItemStack itemRemove = new ItemStack(blockToPlace);
                            player.getInventory().removeItem(itemRemove);
                        }
                    }
                }
            }

        }
        return CommandResult.COMPLETED;
    }

    private List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatUtils.color("&eLeft-Click on block to set position 1"));
        lore.add(ChatUtils.color("&eRight-Click on block to set position 2"));
        lore.add(ChatUtils.color("&eOnce you set the 2 positions you can use /playerworldedit set <BlockName>"));
        lore.add(ChatUtils.color("&euse /playerworldedit set <BlockName>"));
        lore.add(ChatUtils.color("&eIt's take block in your inventory"));
        return lore;
    }

    @Override
    public CommandResult onConsoleExecute(SpigotConsole spigotConsole, String[] strings) {
        return CommandResult.COMPLETED;
    }

    @Override
    public List<String> getCommandComplete(Player player, String alias, String[] args) {
        List<String> args1 = new ArrayList<>();
        List<String> args2 = new ArrayList<>();
        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        Bukkit.getServer().getOnlinePlayers().toArray(players);
        if(args[0].equals("give")){
            for (Player value : players) {
                args2.add(value.getName());
            }
            return args2;
        }else if(args[0].equals("set")){
            try{
                for (Material i : Material.values()) {
                    if(i.isBlock() && i.name().startsWith(args[1].toUpperCase()))
                        args2.add(i.name());
                }
                return args2;
            }catch (Exception ignored){}
        }
        if(player.hasPermission("playerworldedit.give")) args1.add("give");
        if(player.hasPermission("playerworldedit.set")) args1.add("set");
        return args1;
    }
}
