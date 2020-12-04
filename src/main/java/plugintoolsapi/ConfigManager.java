package plugintoolsapi;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ConfigManager {

    private final JavaPlugin plugin;
    private final Logger logger;

    private Map<String, FileConfiguration> configsMap;

    /**
     * Construct new config manager (should only have one per server)
     * // todo: force this with a manager factory that enforces a singleton pattern?
     * // todo: or even just a static method? no - need an object to hold a singleton without static context
     * @param plugin plugin
     * @param logger logger for plugin
     * @param fileNames Map where key is config filename, and boolean whether that config is critical
     */
    public ConfigManager(JavaPlugin plugin, Logger logger, Map<String, Boolean> fileNames){

        this.plugin = plugin;
        this.logger = logger;

        loadConfigs(fileNames);

    }

    /**
     * Get the config file corresponding to the filename given that is held by the manager.
     * @param fileName of config file
     * @return the config file
     * @throws NullPointerException if the config file could not be found
     */
    public FileConfiguration getConfig(String fileName) throws NullPointerException {

        if (configsMap.containsKey(fileName)) return configsMap.get(fileName);
        else throw new NullPointerException("ConfigManager could not find \"" + fileName + "\"!");

    }

    /**
     * Asynchronously save a config held by the manager to file.
     * @param fileName of file to save
     */
    public void saveConfig(String fileName) {

        if (configsMap.containsKey(fileName)) {

            // Convert config to string
            final String configString = configsMap.get(fileName).saveToString();

            // Schedule async task
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {

                PrintWriter printer = null;

                try {

                    // Attempt to print config file data to file
                    printer = new PrintWriter(new File(fileName));
                    printer.println(configString);

                }
                catch (IOException e){

                    System.out.println(e);

                }
                finally {

                    // Close printer
                    if (printer != null) {
                        printer.flush();
                        printer.close();
                    }

                }

            });

        }

    }

    /**
     * Load a FileConfiguration to the config manager (overwriting previous).
     * @param fileName name of file
     */
    public void loadConfig(String fileName){

        FileConfiguration config = new YamlConfiguration();

        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            logger.debug("File not found in getDataFolder.")
                    .debug("Checking resources...");
            file = new File(getClass().getResource(fileName).getFile());
        }
        if(!file.exists()) {
            logger.debug("File not found in resources.")
                    .debug("Creating new...");
            file = new File(fileName);
        }

        try {

            config.load(file);

        }
        // Exception handling - disable plugin if config is critical
        catch(InvalidConfigurationException e){

            logger.warn("Invalid configuration \"" + fileName + "\"!");
            Utils.criticalFailure(plugin);

        }
        catch(FileNotFoundException e){

            logger.log("Could not find file \"" + fileName + "\"! Attempting to create new...");


        }
        catch (IOException e){

            logger.log("Failed to read file \"" + fileName + "\"! (IOException)");
            Utils.criticalFailure(plugin);

        }

        configsMap.put(fileName, config);

    }

    /**
     * Reload all config files given.
     * @param fileNames Map where key is config filename, and boolean whether that config is critical
     */
    public void loadConfigs(Map<String, Boolean> fileNames){

        for (String fileName : fileNames.keySet()){

            loadConfig(fileName);

        }

    }

}
