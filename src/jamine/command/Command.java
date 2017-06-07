package jamine.command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import jamine.Player;
import jamine.Server;
import jamine.event.TextContainer;
import jamine.event.TimingsHandler;
import jamine.permission.Permissible;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lacthan28 on 6/6/2017 - 5:48 PM.
 */
abstract class Command implements Cloneable {
    public static final int MAX = 50;
    public static final char SLASH = '/';
    private static final Pattern COMPILE = Pattern.compile("<permission>", Pattern.LITERAL);
    /**
     * @var array
     */
    private static HashMap<String, String[]> defaultDataTemplate;

    /**
     * @var string
     */
    private String name;
    /**
     * @var array
     */
    protected HashMap<String, String[]> commandData;

    /**
     * @var string
     */
    private String nextLabel;

    /**
     * @var string
     */
    private String label;

    /**
     * @var string[]
     */
    private String[] aliases = new String[MAX];

    /**
     * @var string[]
     */
    private String[] activeAliases = new String[MAX];

    /**
     * @var CommandMap
     */
    private CommandMap commandMap;

    /**
     * @var string
     */
    protected String description = "";

    /**
     * @var string
     */
    protected String usageMessage;

    /**
     * @var string
     */
    private String permissionMessage;

    /**
     * @var TimingsHandler
     */
    public TimingsHandler timings;

    /**
     * @param name         string
     * @param description  string
     * @param usageMessage string
     * @param aliases      string[]
     */
    public Command(String name, String description, String usageMessage, String[] aliases) throws FileNotFoundException {
        this.commandData = Command.generateDefaultData();
        this.name = this.nextLabel = this.label = name;
        this.setDescription(description);
        this.usageMessage = (usageMessage == null) ? (SLASH + name) : usageMessage;
        this.setAliases(aliases);
        this.timings = new TimingsHandler("** Command: " + name, null);
    }

    /**
     * Returns an array containing command data
     *
     * @return array
     */
    public final HashMap<String, String[]> getDefaultCommandData() {
        return this.commandData;
    }

    /**
     * Generates modified command data for the specified player
     * for AvailableCommandsPacket.
     *
     * @param player Player
     * @return array
     */
    public final HashMap<String, String[]> generateCustomCommandData(Player player) {
        //TODO: fix command permission filtering on join
        /*if(!this.testPermissionSilent(player)){
            return null;
		}*/
        HashMap<String, String[]> customData = this.commandData;
        customData.put("aliases", this.getAliases());
        /*foreach(customData["overloads"] as overloadName => overload){
            if(isset(overload["jaminePermission"]) and !player.hasPermission(overload["jaminePermission"])){
				unset(customData["overloads"][overloadName]);
			}
		}*/
        return customData;
    }

    /**
     * @return array
     */
    public final String[] getOverloads() {
        return this.commandData.get("overloads");
    }

    /**
     * @param sender       CommandSender
     * @param commandLabel string
     * @param args         string[]
     * @return mixed
     */
    public abstract void execute(CommandSender sender, String commandLabel, String[] args);

    /**
     * @return string
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return string
     */
    public final String getPermission() {
        return Arrays.toString(this.commandData.get("jaminePermission"));
    }


    /**
     * @param permission string|null
     */
    public final void setPermission(String permission) {
        if (permission != null) {
            this.commandData.put("jaminePermission", new String[]{permission});
        } else {
            this.commandData.remove("jaminePermission");
        }
    }

    /**
     * @param target CommandSender
     * @return bool
     */
    public final boolean testPermission(CommandSender target) {
        if (this.testPermissionSilent(target)) {
            return true;
        }

        if (this.permissionMessage == null) {
            target.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
        } else if (this.permissionMessage.isEmpty()) {
            target.sendMessage(COMPILE.matcher(this.permissionMessage).replaceAll(Matcher.quoteReplacement(this.getPermission())));
        }

        return false;
    }

    /**
     * @param target CommandSender
     * @return bool
     */
    public final boolean testPermissionSilent(CommandSender target) {
        String perm = this.getPermission();
        if ((perm == null) || (perm.isEmpty())) {
            return true;
        }

        for (String permission : perm.split(";")) {
            if (target.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return string
     */
    public final String getLabel() {
        return this.label;
    }

    public final boolean setLabel(String name) {
        this.nextLabel = name;
        if (!this.isRegistered()) {
            this.timings = new TimingsHandler("** Command: " + name, null);
            this.label = name;

            return true;
        }

        return false;
    }

    /**
     * Registers the command into a Command map
     *
     * @param commandMap CommandMap
     * @return bool
     */
    public final boolean register(CommandMap commandMap) {
        if (this.allowChangesFrom(commandMap)) {
            this.commandMap = commandMap;

            return true;
        }

        return false;
    }

    /**
     * @param commandMap CommandMap
     * @return bool
     */
    public final boolean unregister(CommandMap commandMap) {
        if (this.allowChangesFrom(commandMap)) {
            this.commandMap = null;
            this.activeAliases = this.commandData.get("aliases");
            this.label = this.nextLabel;

            return true;
        }

        return false;
    }

    /**
     * @param commandMap CommandMap
     * @return bool
     */
    private boolean allowChangesFrom(CommandMap commandMap) {
        return (this.commandMap == null) || (Objects.equals(this.commandMap, commandMap));
    }

    /**
     * @return bool
     */
    public final boolean isRegistered() {
        return this.commandMap != null;
    }

    /**
     * @return string[]
     */
    public final String[] getAliases() {
        return this.activeAliases;
    }

    /**
     * @return string
     */
    public final String getPermissionMessage() {
        return this.permissionMessage;
    }

    /**
     * @return string
     */
    public final String getDescription() {
        return Arrays.toString(this.commandData.get("description"));
    }

    /**
     * @return string
     */
    public final String getUsage() {
        return this.usageMessage;
    }

    /**
     * @param aliases string[]
     * @param aliases
     */
    public final void setAliases(String[] aliases) {
        this.commandData.put("aliases", aliases);
        if (!this.isRegistered()) {
            this.activeAliases = aliases;
        }
    }

    /**
     * @param description string
     * @param description
     */
    public final void setDescription(String description) {
        this.commandData.put("description", new String[]{this.description});
    }

    /**
     * @param permissionMessage string
     */
    public final void setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
    }

    /**
     * @param usage string
     */
    public final void setUsage(String usage) {
        this.usageMessage = usage;
    }

    public static HashMap<String, String[]> json_decode(String s) throws FileNotFoundException {
        File f = new File(s);
        FileReader is = new FileReader(f);
        JsonReader reader = new JsonReader(is);
        return new Gson().fromJson(reader, new TypeToken<HashMap<String, String[]>>() {
        }.getType());
    }

    /**
     * @return array
     */
    public static HashMap<String, String[]> generateDefaultData() throws FileNotFoundException {
        String path = Server.getInstance().getFilePath() + "src/jamine/resources/command_default.json";
        if (Command.defaultDataTemplate == null) {
            Command.defaultDataTemplate = json_decode(path);
        }
        return Command.defaultDataTemplate;
    }

    /**
     * @param source       CommandSender
     * @param message      string
     * @param sendToSource bool
     */
    public static void broadcastCommandMessage(CommandSender source, String message, boolean sendToSource) {
        Permissible[] users = source.getServer().getPluginManager().getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
        result = new TranslationContainer("chat.type.admin",[source.getName(), message]);
        colored = new TranslationContainer(TextFormat.GRAY.TextFormat.ITALIC."%chat.type.admin",[
                source.getName(), message]);

        if (sendToSource == true and !(source instanceof ConsoleCommandSender)){
            source.sendMessage(message);
        }

        foreach(users as user) {
            if (user instanceof CommandSender) {
                if (user instanceof ConsoleCommandSender) {
                    user.sendMessage(result);
                } else if (user != source) {
                    user.sendMessage(colored);
                }
            }
        }
    }


    public final TextContainer clone(TextContainer text) {

        text = null;
        try {
            text = (TextContainer) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
        }
        return text;
    }


    /**
     * @param source       CommandSender
     * @param message      TextContainer
     * @param sendToSource bool
     */
    public void broadcastCommandMessage(CommandSender source, TextContainer message, boolean sendToSource) {
        if (message instanceof TextContainer) {
            TextContainer m = this.clone(message);
            String result = '[' + source.getName() + ": " + (source.getServer().getLanguage().get(m.getText()) != m.getText() ? "%" : "") + m.getText() + "]";


            users = source.getServer().getPluginManager().
                    getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
            colored = TextFormat.GRAY.TextFormat.ITALIC.result;

            m.setText(result);
            result = clone m;
            m.setText(colored);
            colored = clone m;
        }

        if (sendToSource == true and !(source instanceof ConsoleCommandSender)){
            source.sendMessage(message);
        }

        foreach(users as user) {
            if (user instanceof CommandSender) {
                if (user instanceof ConsoleCommandSender) {
                    user.sendMessage(result);
                } else if (user != source) {
                    user.sendMessage(colored);
                }
            }
        }
    }

    /**
     * @return string
     */
    public void __toString() {
        return this.name;
    }
}
