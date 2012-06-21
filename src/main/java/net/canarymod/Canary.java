package net.canarymod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import net.canarymod.api.Server;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.bansystem.BanManager;
import net.canarymod.config.Configuration;
import net.canarymod.database.Database;
import net.canarymod.help.HelpManager;
import net.canarymod.hook.HookExecutor;
import net.canarymod.kit.KitProvider;
import net.canarymod.permissionsystem.PermissionManager;
import net.canarymod.plugin.PluginLoader;
import net.canarymod.serialize.Serializer;
import net.canarymod.user.UserAndGroupsProvider;
import net.canarymod.warp.WarpProvider;

/**
 * The interface to the brains of the bird! AKA Utils
 * 
 * @author Chris Ksoll
 * @author Jos Kuijpers
 * 
 */
public abstract class Canary {

    protected Server server;

    protected BanManager banManager;
    protected UserAndGroupsProvider userAndGroupsProvider;
    protected PermissionManager permissionLoader;
    protected WarpProvider warpProvider;
    protected KitProvider kitProvider;
    protected HookExecutor hookExecutor;
    protected Database database;
    protected PluginLoader loader;
    protected Configuration config;
    protected HelpManager helpManager;
    
    //Serializer Cache
    HashMap<String, Serializer<?>> serializers = new HashMap<String, Serializer<?>>();
    
    protected static Canary instance;
    
    /**
     * Get the ban System to manage bans
     * @return
     */
    public static BanManager bans() {
        return instance.banManager;
    }
    
    /**
     * Get the Groups provider to manage groups
     * @return
     */
    public static UserAndGroupsProvider usersAndGroups() {
        return instance.userAndGroupsProvider;
    }
    
    /**
     * Get the Warps provider to manage warps and homes
     * @return
     */
    public static WarpProvider warps() {
        return instance.warpProvider;
    }
    
    /**
     * Get the Kit Provider to manage kits
     * @return
     */
    public static KitProvider kits() {
        return instance.kitProvider;
    }
    
    /**
     * Get the Hook executor to fire hooks
     * @return
     */
    public static HookExecutor hooks() {
        return instance.hookExecutor;
    }
    
    /**
     * Get the database interface for managing system data and custom plugin data
     * @return
     */
    public static Database db() {
        return instance.database;
    }
    
    /**
     * Get the Plugin Loader to load, enable or disable plugins and manage 
     * plugin dependencies
     * @return
     */
    public static PluginLoader loader() {
        return instance.loader;
    }
    
    /**
     * Get the permission loader.
     * Note: As plugin author will rarely need to use this.
     * Use the PermissionProviders with Groups and players instead!
     * @return
     */
    public static PermissionManager permissionManager() {
        return instance.permissionLoader;
    }
    
    /**
     * Get the help manager, used to register and unregister help commands, and creating help visualizations
     * @return
     */
    public static HelpManager help() {
        return instance.helpManager;
    }
    
    /**
     * Get the canary instance
     * @return
     */
    public static Canary instance() {
        return instance;
    }
    
    /**
     * Set the canary instance
     * @param canary
     */
    public static void setCanary(Canary canary) {
        instance = canary;
    }

    /**
     * Set the server instance for this Canary
     * @param server
     */
    public static void setServer(Server server) {
        instance.server = server;
    }
    
    /**
     * Get the Server for managing server related stuff
     * @return
     */
    public static Server getServer() {
        return instance.server;
    }
    
    /**
     * Get the unix timestamp for the current time
     * 
     * @return
     */
    public static long getUnixTimestamp() {
        return (System.currentTimeMillis() / 1000L);
    }

    /**
     * Parse number of seconds for the given time and TimeUnit String<br>
     * Example: long 1 String HOUR will give you number of seconds in 1 hour.<br>
     * This is used to work with unix timestamps.
     * 
     * @param time
     * @param timeUnit
     *            MINUTES, HOURS, DAYS, WEEKS, MONTHS
     * @return
     */
    public static long parseTime(long time, String timeUnit) {

        if (timeUnit.toLowerCase().startsWith("minute")) {
            time *= 60;
        } else if (timeUnit.toLowerCase().startsWith("hour")) {
            time *= 3600;
        } else if (timeUnit.toLowerCase().startsWith("day")) {
            time *= 86400;
        } else if (timeUnit.toLowerCase().startsWith("week")) {
            time *= 604800;
        } else if (timeUnit.toLowerCase().startsWith("month")) {
            time *= 2629743;
        }
        return time;
    }

    /**
     * Parse number of seconds for the given time and TimeUnit<br>
     * Example: long 1 String {@link TimeUnit#HOURS} will give you number of
     * seconds in 1 hour.<br>
     * This is used to work with unix timestamps.
     * 
     * @param time
     * @param unit
     * @return
     */
    public static long parseTime(long time, TimeUnit unit) {
        return unit.convert(time, TimeUnit.SECONDS);
    }
    
    /**
     * Glue together a String array to a normal string
     * @param toGlue
     * @param start
     * @param divider The glue between the elements of the array
     * @return
     */
    public static String glueString(String[] toGlue, int start, String divider) {
        StringBuilder builder = new StringBuilder();

        for (int i = start; i < toGlue.length; i++) {
            if (i != start)
                builder.append(divider);
            builder.append(toGlue[i]);
        }
        return builder.toString();
    }
    
    /**
     * Splits a string into an array at the given separator, without removing empty ones like .split() does.
     * @author Jos Kuijpers
     * @param in The string to split
     * @param seperator The string to split at
     * @return an array containing all components
     */
    public static String[] realSplit(String in, String seperator) {
        String[] res = {};
        ArrayList<String> items = new ArrayList<String>();
        
        int pos = 0;
        int last = 0;
        while((pos = in.indexOf(seperator, last)) != -1) {
            items.add(in.substring(last, pos));
            last = pos+seperator.length();
        }
        items.add(in.substring(last));
        
        return items.toArray(res);
    }
    
    @SuppressWarnings("unchecked") //TODO: refactor to not use this :S
    public static String serialize(Object object) {
        if(object instanceof Item) {
            Serializer<Item> ser = (Serializer<Item>) instance.serializers.get("Item");
            return ser.serialize((Item)object);

        }
        else if(object instanceof Block) {
            Serializer<Block> ser = (Serializer<Block>) instance.serializers.get("Block");
            return ser.serialize((Block)object);
        }
        else {
            return null;
        }
    }
    
    /**
     * Accepts a String with data and the class it should
     * deserialize into.
     * @param data
     * @param type Block or Item as String
     */
    public static Object deserialize(String data, String type) {
        Serializer<?> ser = instance.serializers.get(type);
        return ser.deserialize(data);
        
    }
    
    /**
     * Add your own serializer to to the list of serializers
     * @param template The class that should be processed. If you made a serializer for a 
     * class called Foo, this should be Foo.getClass()
     * @param serializer An instance of your serializer
     */
    public static void addSerializer(String lookupName, Serializer<?> serializer) {
        Logman.logInfo("Adding a new Serializer: "+lookupName);
        instance.serializers.put(lookupName, serializer);
    }
}