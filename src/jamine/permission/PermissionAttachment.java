package jamine.permission;

import jamine.plugin.Plugin;

/**
 * Created by lacthan28 on 6/6/2017 - 5:35 PM.
 */
public class PermissionAttachment {
    /**
     * @var PermissionRemovedExecutor
     */
    private PermissionRemovedExecutor removed = null;

    /**
     * @var bool[]
     */
    private boolean[] permissions = new boolean[50];

    /** @var Permissible */
    private Permissible permissible;

    /** @var Plugin */
    private plugin;

    /**
     * @param Plugin      plugin
     * @param Permissible permissible
     * @throws PluginException
     */
    public void PermissionAttachment(Plugin plugin, Permissible permissible) {
        if (!plugin.isEnabled()) {
            throw new PluginException("Plugin " + plugin.getDescription().getName() + " is disabled");
        }

        this->permissible = permissible;
        this->plugin = plugin;
    }

    /**
     * @return Plugin
     */
    public function getPlugin() {
        return this->plugin;
    }

    /**
     * @param PermissionRemovedExecutor ex
     */
    public function setRemovalCallback(PermissionRemovedExecutor ex) {
        this->removed = ex;
    }

    /**
     * @return PermissionRemovedExecutor
     */
    public function getRemovalCallback() {
        return this->removed;
    }

    /**
     * @return Permissible
     */
    public function getPermissible() {
        return this->permissible;
    }

    /**
     * @return bool[]
     */
    public function getPermissions() {
        return this->permissions;
    }

    public function clearPermissions() {
        this->permissions = [];
        this->permissible -> recalculatePermissions();
    }

    /**
     * @param bool[] permissions
     */
    public function setPermissions(array permissions) {
        foreach(permissions as key = > value){
            this->permissions[key] = (bool) value;
        }
        this->permissible -> recalculatePermissions();
    }

    /**
     * @param string[] permissions
     */
    public function unsetPermissions(array permissions) {
        foreach(permissions as node) {
            unset(this->permissions[node]);
        }
        this->permissible -> recalculatePermissions();
    }

    /**
     * @param string|Permission name
     * @param bool              value
     */
    public function setPermission(name, value) {
        name = name instanceof Permission ? name -> getName() : name;
        if (isset(this->permissions[name])){
            if (this->permissions[name] == = value){
                return;
            }
            unset(this->permissions[name]); //Fixes children getting overwritten
        }
        this->permissions[name] = value;
        this->permissible -> recalculatePermissions();
    }

    /**
     * @param string|Permission name
     */
    public function unsetPermission(name) {
        name = name instanceof Permission ? name -> getName() : name;
        if (isset(this->permissions[name])){
            unset(this->permissions[name]);
            this->permissible -> recalculatePermissions();
        }
    }

    /**
     * @return void
     */
    public function remove() {
        this->permissible -> removeAttachment(this);
    }
}
