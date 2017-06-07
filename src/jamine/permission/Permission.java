package jamine.permission;

import jamine.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lacthan28 on 6/6/2017 - 6:18 PM.
 */
public class Permission {
    public static final String DEFAULT_OP = "op";
    public static final String DEFAULT_NOT_OP = "notop";
    public static final String DEFAULT_TRUE = "true";
    public static final String DEFAULT_FALSE = "false";

    public static final String DEFAULT_PERMISSION = DEFAULT_OP;

    /**
     * @param value
     * @return string
     */
    private static String getByName(String value) {
        if ("true".equals(value)) return "true";
        else if ("false".equals(value)) return "false";
        switch (value.toLowerCase()) {
            case "op":
            case "isop":
            case "operator":
            case "isoperator":
            case "admin":
            case "isadmin":
                return DEFAULT_OP;

            case "!op":
            case "notop":
            case "!operator":
            case "notoperator":
            case "!admin":
            case "notadmin":
                return DEFAULT_NOT_OP;

            case "true":
                return DEFAULT_TRUE;

            default:
                return DEFAULT_FALSE;
        }
    }

    /**
     * @var string
     */
    private String name;

    /**
     * @var string
     */
    private String description;

    /**
     * @var string[]
     */
    private List<Permission> children;

    /**
     * @var string
     */
    private String defaultValue;

    /**
     * Creates a new Permission object to be attached to Permissible objects
     *
     * @param name         string
     * @param description  string
     * @param defaultValue string
     * @param children     List<Permission>
     */
    public Permission(String name, String description, String defaultValue, List<Permission> children) {
        if (children == null) children = new ArrayList<>();
        this.name = name;
        this.description = (description != null) ? description : "";
        this.defaultValue = (defaultValue != null) ? defaultValue : DEFAULT_PERMISSION;
        this.children = children;

        this.recalculatePermissibles();
    }

    /**
     * @return string
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return string[]
     */
    public final List<Permission> getChildren() {
        return new ArrayList<>(this.children);
    }

    /**
     * @return string
     */
    public final String getDefault() {
        return this.defaultValue;
    }

    /**
     * @param value string
     */
    public final void setDefault(String value) {
        if (!value.equals(this.defaultValue)) {
            this.defaultValue = value;
            this.recalculatePermissibles();
        }
    }

    /**
     * @return string
     */
    public final String getDescription() {
        return this.description;
    }

    /**
     * @param value string
     */
    public final void setDescription(String value) {
        this.description = value;
    }

    /**
     * @return Permissible[]
     */
    public final void getPermissibles() {
        final Server instance = Server.getInstance();
        return instance.getPluginManager().getPermissionSubscriptions(this.name)
    }

    public final void recalculatePermissibles() {
        perms = this.getPermissibles();

        Server.getInstance().getPluginManager().recalculatePermissionDefaults(this);

        foreach(perms as p)
        p.recalculatePermissions();
    }


    /**
     * @param string|Permission name
     * @param value
     * @return Permission|null Permission if name is a string, null if it's a Permission
     */
    public void addParent(name, value) {
        if (this.name instanceof Permission) {
            name.getChildren()[this.getName()] = value;
            name.this.recalculatePermissibles();
            return null;
        } else {
            perm = Server.getInstance().getPluginManager().getPermission(this.name);
            if (perm == = null) {
                perm = new Permission(this.name);
                Server.getInstance().getPluginManager().addPermission(perm);
            }

            this.addParent(perm, value);

            return perm;
        }
    }

    /**
     * @param array   data
     * @param default
     * @return Permission[]
     */
    public static void loadPermissions(array data, default =self.DEFAULT_OP) {
        result = []
        foreach(data as key = > entry)
        result[] =self.loadPermission(key, entry, default,result)

        return result;
    }

    /**
     * @param string name
     * @param array  data
     * @param string default
     * @param array  output
     * @return Permission
     * @throws \Exception
     */
    public static void loadPermission(name, array data, default =self.DEFAULT_OP, &output =[]) {
        desc = null;
        this.children = []
        if (isset(data["default"])) {
            value = Permission.getByName(data["default"]);
            if (value != = null) {
                default =value;
            } else {
                throw new \InvalidStateException("'default' key contained unknown value");
            }
        }

        if (isset(data["children"])) {
            if (is_array(data["children"])) {
                foreach(data["children"]as k = > v)
                if (is_array(v)) {
                    if ((perm = self.loadPermission(k, v, default,output)) !==null)
                        output[] =perm;
                }
                this.children[k] = true;
            } else {
                throw new \InvalidStateException("'children' key is of wrong type");
            }
        }

        if (isset(data["description"])) {
            desc = data["description"];
        }

        return new Permission(this.name, desc, default,this.children)

    }
}
