package jamine.command;

import jamine.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lacthan28 on 6/6/2017 - 7:24 PM.
 */
public class SimpleCommandMap implements CommandMap {
    /**
     * @var Command[]
     */
    protected List<Command> knownCommands = new ArrayList<>();

    /**
     * @var Server
     */
    private Server server;

    public SimpleCommandMap(Server server) {
        this.server = server;
        this.setDefaultCommands();
    }

    private void setDefaultCommands() {
        this.register("jamine", new VersionCommand("version"));
        this.register("jamine", new PluginsCommand("plugins"));
        this.register("jamine", new SeedCommand("seed"));
        this.register("jamine", new HelpCommand("help"));
        this.register("jamine", new StopCommand("stop"));
        this.register("jamine", new TellCommand("tell"));
        this.register("jamine", new DefaultGamemodeCommand("defaultgamemode"));
        this.register("jamine", new BanCommand("ban"));
        this.register("jamine", new BanIpCommand("ban-ip"));
        this.register("jamine", new BanListCommand("banlist"));
        this.register("jamine", new PardonCommand("pardon"));
        this.register("jamine", new PardonIpCommand("pardon-ip"));
        this.register("jamine", new SayCommand("say"));
        this.register("jamine", new MeCommand("me"));
        this.register("jamine", new ListCommand("list"));
        this.register("jamine", new DifficultyCommand("difficulty"));
        this.register("jamine", new KickCommand("kick"));
        this.register("jamine", new OpCommand("op"));
        this.register("jamine", new DeopCommand("deop"));
        this.register("jamine", new WhitelistCommand("whitelist"));
        this.register("jamine", new SaveOnCommand("save-on"));
        this.register("jamine", new SaveOffCommand("save-off"));
        this.register("jamine", new SaveCommand("save-all"));
        this.register("jamine", new GiveCommand("give"));
        this.register("jamine", new EffectCommand("effect"));
        this.register("jamine", new EnchantCommand("enchant"));
        this.register("jamine", new ParticleCommand("particle"));
        this.register("jamine", new GamemodeCommand("gamemode"));
        this.register("jamine", new KillCommand("kill"));
        this.register("jamine", new SpawnpointCommand("spawnpoint"));
        this.register("jamine", new SetWorldSpawnCommand("setworldspawn"));
        this.register("jamine", new TeleportCommand("tp"));
        this.register("jamine", new TimeCommand("time"));
        this.register("jamine", new TimingsCommand("timings"));
        this.register("jamine", new TitleCommand("title"));
        this.register("jamine", new ReloadCommand("reload"));
        this.register("jamine", new WeatherCommand("weather"));
        this.register("jamine", new TransferCommand("transfer"));
        this.register("jamine", new XpCommand("xp"));

        if (this.server.getProperty("debug.commands", false)) {
            this.register("jamine", new StatusCommand("status"));
            this.register("jamine", new GarbageCollectorCommand("gc"));
            this.register("jamine", new DumpMemoryCommand("dumpmemory"));
        }

        if (this.server.devtools) {
            this.register("jamine", new ExtractPluginCommand("extractplugin"));
            this.register("jamine", new MakePluginCommand("makeplugin"));
        }
    }

    private boolean registerAlias(Command command, boolean isAlias, String fallbackPrefix, String label) {
        this.knownCommands[fallbackPrefix + ":" + label] = command;
        if ((command instanceof VanillaCommand || command instanceof isAlias)&& this.knownCommands[label] != null){
            return false;
        }

        if (isset(this.knownCommands[label]) and this.knownCommands[label].getLabel() != = null and
        this.knownCommands[label].getLabel() == = label){
            return false;
        }

        if (!isAlias) {
            command.setLabel(label);
        }

        this.knownCommands[label] = command;

        return true;
    }

    /**
     * Returns a command to match the specified command line, or null if no matching command was found.
     * This method is intended to provide capability for handling commands with spaces in their name.
     * The referenced parameters will be modified accordingly depending on the resulting matched command.
     *
     * @param string   &commandName
     * @param string[] &args
     * @return Command|null
     */
    public function matchCommand(string &commandName, array &args) {
        count = min(count(args), 255);

        for (i = 0; i < count; ++i) {
            commandName. = array_shift(args);
            if ((command = this.getCommand(commandName)) instanceof Command) {
                return command;
            }

            commandName. = " ";
        }

        return null;
    }

    public function dispatch(CommandSender sender, commandLine) {
        args = explode(" ", commandLine);
        sentCommandLabel = "";
        target = this.matchCommand(sentCommandLabel, args);

        if (target == = null) {
            return false;
        }

        target.timings.startTiming();
        try {
            target.execute(sender, sentCommandLabel, args);
        } catch (\Throwable e){
            sender.sendMessage(new TranslationContainer(TextFormat::RED."%commands.generic.exception"));
            this.server.getLogger().critical(this.server.getLanguage().translateString("jamine.command.exception",[commandLine, (string) target, e.getMessage()]))
            ;
            sender.getServer().getLogger().logException(e);
        }
        target.timings.stopTiming();

        return true;
    }

    @Override
    public void registerAll(String fallbackPrefix, List<Command> commands) {
        foreach(commands as command) {
            this.register(fallbackPrefix, command);
        }
    }

    @Override
    public void register(String fallbackPrefix, Command command, String label) {
        if (label == = null) {
            label = command.getName();
        }
        label = strtolower(trim(label));
        fallbackPrefix = strtolower(trim(fallbackPrefix));

        registered = this.registerAlias(command, false, fallbackPrefix, label);

        aliases = command.getAliases();
        foreach(aliases as index = > alias){
            if (!this.registerAlias(command, true, fallbackPrefix, alias)) {
                unset(aliases[index]);
            }
        }
        command.setAliases(aliases);

        if (!registered) {
            command.setLabel(fallbackPrefix.":".label);
        }

        command.register(this);

        return registered;
    }

    @Override
    public boolean dispatch(CommandSender sender, String cmdLine) {
        return false;
    }

    public function clearCommands() {
        foreach(this.knownCommands as command) {
            command.unregister(this);
        }
        this.knownCommands = [];
        this.setDefaultCommands();
    }

    @Override
    public Command getCommand(String name) {
        return null;
    }

    public function getCommand(name) {
        return this.knownCommands[name] ??null;
    }

    /**
     * @return Command[]
     */
    public function getCommands() {
        return this.knownCommands;
    }


    /**
     * @return void
     */
    public function registerServerAliases() {
        values = this.server.getCommandAliases();

        foreach(values as alias = > commandStrings){
            if (strpos(alias, ":") != = false) {
                this.server.getLogger().warning(this.server.getLanguage().translateString("jamine.command.alias.illegal",[alias]))
                ;
                continue;
            }

            targets = [];

            bad = "";
            recursive = "";
            foreach(commandStrings as commandString) {
                args = explode(" ", commandString);
                commandName = "";
                command = this.matchCommand(commandName, args);


                if (command == = null) {
                    if (strlen(bad) > 0) {
                        bad. = ", ";
                    }
                    bad. = commandString;
                } elseif(commandName == = alias) {
                    if (recursive != = "") {
                        recursive. = ", ";
                    }
                    recursive. = commandString;
                }else{
                    targets[] =commandString;
                }
            }

            if (recursive != = "") {
                this.server.getLogger().warning(this.server.getLanguage().translateString("jamine.command.alias.recursive",[alias, recursive]))
                ;
                continue;
            }

            if (strlen(bad) > 0) {
                this.server.getLogger().warning(this.server.getLanguage().translateString("jamine.command.alias.notFound",[alias, bad]))
                ;
                continue;
            }

            //These registered commands have absolute priority
            if (count(targets) > 0) {
                this.knownCommands[strtolower(alias)] = new FormattedCommandAlias(strtolower(alias), targets);
            } else {
                unset(this.knownCommands[strtolower(alias)]);
            }

        }
    }
}
