package jamine.command;

import java.util.List;

/**
 * Created by lacthan28 on 6/6/2017 - 7:25 PM.
 */
public interface CommandMap {
    /**
     * @param fallbackPrefix string
     * @param commands       Command[]
     */
    public void registerAll(String fallbackPrefix, List<Command> commands);

    /**
     * @param fallbackPrefix string
     * @param command        Command
     * @param label          string
     */
    public void register(String fallbackPrefix, Command command, String label);

    /**
     * @param sender  CommandSender
     * @param cmdLine string
     * @return bool
     */
    public boolean dispatch(CommandSender sender, String cmdLine);

    /**
     * @return void
     */
    public void clearCommands();

    /**
     * @param name string
     * @return Command
     */
    public Command getCommand(String name);
}
