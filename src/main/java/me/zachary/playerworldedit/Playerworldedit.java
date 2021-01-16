package me.zachary.playerworldedit;

import me.zachary.playerworldedit.commands.PlayerworldeditCommand;
import me.zachary.playerworldedit.listeners.RightClickListeners;
import org.bukkit.Location;
import xyz.theprogramsrc.supercoreapi.spigot.SpigotPlugin;
import xyz.theprogramsrc.supercoreapi.spigot.commands.precreated.SuperCoreAPICommand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Playerworldedit extends SpigotPlugin {
    public static Map<UUID, Location> pos1 = new HashMap<UUID, Location>();
    public static Map<UUID, Location> pos2 = new HashMap<UUID, Location>();

    @Override
    public void onPluginEnable() {
        new PlayerworldeditCommand(this);
        new RightClickListeners(this);
    }

    @Override
    public void onPluginDisable() {

    }

    @Override
    public void onPluginLoad() {

    }

    @Override
    public boolean isPaid() {
        return false;
    }
}
