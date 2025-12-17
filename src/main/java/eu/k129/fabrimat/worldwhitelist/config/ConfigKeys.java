package eu.k129.fabrimat.worldwhitelist.config;

/**
 * Constants for configuration keys used throughout the plugin
 */
public final class ConfigKeys {

    // Config paths
    public static final String DEFAULT_WORLD = "default_world";
    public static final String WHITELIST_WORLDS = "whitelist_worlds";
    public static final String USERS_PREFIX = "users.";
    public static final String MESSAGE_PREFIX = "message.";
    public static final String MESSAGE_PREFIX_KEY = "message.prefix";

    // Message keys
    public static final String MSG_NO_PERMISSION = "no_permission";
    public static final String MSG_NO_ACCESS_WORLD = "no_access_world";
    public static final String MSG_CONFIG_RELOADED = "config_reloaded";
    public static final String MSG_WHITELIST_ENABLED = "whitelist_enabled";
    public static final String MSG_WHITELIST_ALREADY_ENABLED = "whitelist_already_enabled";
    public static final String MSG_WHITELIST_DISABLED = "whitelist_disabled";
    public static final String MSG_WHITELIST_ALREADY_DISABLED = "whitelist_already_disabled";
    public static final String MSG_USER_ADDED = "user_added";
    public static final String MSG_USER_ALREADY_ADDED = "user_already_added";
    public static final String MSG_USER_REMOVED = "user_removed";
    public static final String MSG_USER_ALREADY_REMOVED = "user_already_removed";
    public static final String MSG_USER_NOT_SPECIFIED = "user_not_specified";
    public static final String MSG_PLAYER_NOT_FOUND = "player_not_found";
    public static final String MSG_WHITELIST_LIST = "whitelist_list";
    public static final String MSG_WORLD_NOT_VALID = "world_not_valid";
    public static final String MSG_COMMAND_NOT_VALID = "command_not_valid";

    // Permission nodes
    public static final String PERM_ADMIN_RELOAD = "worldwhitelist.admin.reload";
    public static final String PERM_WORLD_PREFIX = "worldwhitelist.world.";
    public static final String PERM_BYPASS_SUFFIX = ".bypass";
    public static final String PERM_ON_SUFFIX = ".on";
    public static final String PERM_OFF_SUFFIX = ".off";
    public static final String PERM_ADD_SUFFIX = ".add";
    public static final String PERM_REMOVE_SUFFIX = ".remove";
    public static final String PERM_LIST_SUFFIX = ".list";

    // Default values
    public static final String DEFAULT_WORLD_NAME = "world";
    public static final String DEFAULT_MESSAGE_PREFIX = "&7[&6WorldWhitelist&7]&r ";

    private ConfigKeys() {
        // Prevent instantiation
        throw new AssertionError("Cannot instantiate constants class");
    }
}
