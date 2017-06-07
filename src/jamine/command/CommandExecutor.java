package jamine.command;

/**
 * Created by lacthan28 on 6/6/2017 - 5:45 PM.
 */
public interface CommandExecutor {
    /**
     * @param sender CommandSender
     * @param command Command
     * @param label string
     * @param args string[]
     *
     * @return bool
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args);

}
