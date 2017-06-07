package jamine;

import jamine.permission.ServerOperator;

/**
 * Created by lacthan28 on 06/06/2017 - 5:22 PM.
 */
interface IPlayer extends ServerOperator {
    /**
     * @return bool
     */
    public boolean isOnline();

    /**
     * @return string
     */
    public String getName();

    /**
     * @return bool
     */
    public boolean isBanned();

    /**
     * @param banned
     */
    public void setBanned(boolean banned);

    /**
     * @return bool
     */
    public boolean isWhitelisted();

    /**
     * @param value
     */
    public void setWhitelisted(boolean value);

    /**
     * @return Player|null
     */
    public void getPlayer();

    /**
     * @return int|double
     */
    public double getFirstPlayed();

    /**
     * @return int|double
     */
    public double getLastPlayed();

    /**
     * @return mixed
     */
    public void hasPlayedBefore();
}
