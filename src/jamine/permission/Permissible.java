package jamine.permission;

import jamine.plugin.Plugin;

/**
 * Created by lacthan28 on 6/6/2017 - 5:41 PM.
 */
public interface Permissible extends ServerOperator {
    /**
     * Checks if this instance has a permission overridden
     *
     * @param name string|Permission
     * @return bool
     */
    public boolean isPermissionSet(String name);

    /**
     * Returns the permission value if overridden, or the default value if not
     *
     * @param name String|Permission
     * @return mixed
     */
    public boolean hasPermission(String name);

    /**
     * @param plugin Plugin
     * @param name   string
     * @param value  bool
     * @return PermissionAttachment
     */
    public static void addAttachment(Plugin plugin, String name, boolean value) {
        Permissible.addAttachment(plugin, null, false);
    }

    /**
     * @param attachment PermissionAttachment
     * @return void
     */
    public void removeAttachment(PermissionAttachment attachment);


    /**
     * @return void
     */
    public void recalculatePermissions();

    /**
     * @return Permission[]
     */
    public Permission[] getEffectivePermissions();
}
