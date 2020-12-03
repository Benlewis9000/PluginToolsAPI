package plugintoolsapi;

import org.bukkit.plugin.java.JavaPlugin;

public class Utils {

    /**
     * In event if critical failure, stop the plugin.
     * @param plugin to stop
     */
    public static void criticalFailure(JavaPlugin plugin){

        plugin.getLogger().warning("Critical failure, stopping plugin.");
        plugin.getServer().getPluginManager().disablePlugin(plugin);

    }

}
