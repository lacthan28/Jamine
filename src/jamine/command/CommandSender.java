package jamine.command;

import jamine.Server;
import jamine.permission.Permissible;

/**
 * Created by lacthan28 on 6/6/2017 - 5:47 PM.
 */
public interface CommandSender extends Permissible {
    /**
     * @param message string
     */
    public void sendMessage(String message);

    /**
     * @return \jamine\Server
     */
    public Server getServer();

    /**
     * @return string
     */
    public String getName();
}
