package jamine.plugin;

import jamine.command.CommandExecutor;

/**
 * Created by lacthan28 on 6/6/2017 - 5:45 PM.
 */
public interface Plugin extends CommandExecutor {
    /**
     * Called when the plugin is loaded, before calling onEnable()
     */
    public void onLoad();

    /**
     * Called when the plugin is enabled
     */
    public void onEnable();

    public boolean isEnabled();

    /**
     * Called when the plugin is disabled
     * Use this to free open things and finish actions
     */
    public void onDisable();

    public boolean isDisabled();

    /**
     * Gets the plugin's data folder to save files and configuration.
     * This directory name has a trailing slash.
     */
    public void getDataFolder();

    /**
     * @return PluginDescription
     */
    public void getDescription();

    /**
     * Gets an embedded resource in the plugin file.
     *
     * @param filename string
     */
    public void getResource(String filename);

    /**
     * Saves an embedded resource to its relative location in the data folder
     *
     * @param filename string
     * @param replace  bool
     */
    public static void saveResource(String filename, boolean replace) {
        Plugin.saveResource(filename, false);
    }

    /**
     * Returns all the resources packaged with the plugin
     */
    public void getResources();

    /**
     * @return \\pocketmine\\utils\\Config
     */
    public void getConfig();

    public void saveConfig();

    public void saveDefaultConfig();

    public void reloadConfig();

    /**
     * @return \\pocketmine\\Server
     */
    public void getServer();

    public void getName();

    /**
     * @return PluginLogger
     */
    public void getLogger();

    /**
     * @return PluginLoader
     */
    public void getPluginLoader();
}
