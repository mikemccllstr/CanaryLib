package net.canarymod.api;

import java.util.ArrayList;

import net.canarymod.api.entity.Player;
import net.canarymod.api.world.WorldManager;

/**
 * CanaryMod Server.<br>
 * This handles communication to the server and provides a couple of useful
 * information
 * 
 * @author Chris
 * @author Jos Kuijpers
 * 
 */
public interface Server {
    /**
     * Get the current host name for this server
     * 
     * @return
     */
    public String getHostname();

    /**
     * Get the amount of players that are online
     * 
     * @return
     */
    public int getNumPlayersOnline();

    /**
     * Get the number of max. allowed players
     * 
     * @return
     */
    public int getMaxPlayers();

    /**
     * Get a list of names from players that are currently online
     * 
     * @return
     */
    public String[] getPlayerNameList();
    
    /**
     * Get a list of online players
     * @return
     */
    public ArrayList<Player> getPlayerList();

    /**
     * Get the default world name
     * 
     * @return
     */
    public String getDefaultWorldName();

    /**
     * Get the world container
     * 
     * @return
     */
    public WorldManager getWorldManager();

    /**
     * Use a MC vanilla console command
     * 
     * @param command
     */
    public void consoleCommand(String command);

    /**
     * Use a MC vanilla console command as the given player (Ingame vanilla
     * commands)
     * 
     * @param command
     * @param player
     */
    public void consoleCommand(String command, Player player);

    /**
     * Set a timer in the built-in vanilla Timer System.
     * 
     * @param uniqueName
     *            Name to identify your timer
     * @param time
     *            Time in milliseconds.
     */
    public void setTimer(String uniqueName, int time);

    /**
     * Check if a registered Timer has expired already
     * 
     * @param uniqueName
     *            The unique name of the timer you're looking for
     * @return
     */
    public boolean isTimerExpired(String uniqueName);
    
    /**
     * Match a player name or a part of a player name to an existing online
     * player
     * 
     * @param player
     * @return a reference to an online player or null if no match was found
     */
    public Player matchPlayer(String player);
    
    /**
     * Like matchPlayer, this returns a player according to a name String.
     * However, no matching is performed here so you need to pass the exact
     * player name
     * 
     * @param player
     * @return a reference to an online player or null if there is no player
     *         with the given name
     */
    public Player getPlayer(String player);
    
    /**
     * Send (broadcast) the given messsage to ALL players on the server,
     * in all worlds and dimensions.
     * @param message
     */
    public void broadcastMessage(String message);
}
