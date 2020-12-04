package plugintoolsapi;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Logger {

    private static Logger logger = null;

    private JavaPlugin plugin;
    private boolean debug;

    private Logger(JavaPlugin plugin){

        this.plugin = plugin;

    }

    /**
     * Get singleton instance of logger (or create new if null).
     * @param plugin to log to
     * @return the logger
     */
    public static Logger getInstance(JavaPlugin plugin){

        if (logger != null) return logger;
        else return new Logger(plugin);

    }

    /**
     * Log an error.
     * @param msg to log
     * @return the logger
     */
    public Logger warn(String msg){

        this.plugin.getLogger().warning(ChatColor.translateAlternateColorCodes('&', "&4" + msg));

        return this;
    }

    /**
     * Log a message.
     * @param msg to log
     * @return the logger
     */
    public Logger log(String msg){

        this.plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));

        return this;
    }

    /**
     * Check if debug mode is enabled on the logger.
     * @return true if debug enabled
     */
    public boolean debug(){

        return this.debug;

    }

    /**
     * Print a message if debug mode is enabled (allows color codes with '&')
     * @param msg to print
     * @return the logger
     */
    public Logger debug(String msg){

        if (debug) log(msg);

        return this;
    }

    /**
     * Set the debug mode.
     * @param enabled true to enable messages
     * @return the logger
     */
    public Logger debug(boolean enabled){

        debug = enabled;

        return this;
    }

}
