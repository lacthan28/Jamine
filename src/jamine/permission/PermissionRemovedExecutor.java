package jamine.permission;

/**
 * Created by lacthan28 on 6/6/2017 - 5:34 PM.
 */
public interface PermissionRemovedExecutor {
    /**
     * @param attachment PermissionAttachment
     * @return void
     */
    public void attachmentRemoved(PermissionAttachment attachment);
}
