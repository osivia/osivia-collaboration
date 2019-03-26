package org.osivia.services.workspace.portlet.model;

import java.util.Objects;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LocalGroupMember {

    /** Identifier. */
    private String uid;
    /** Display name. */
    private String displayName;
    /** Avatar URL. */
    private String avatar;
    /** Extra informations. */
    private String extra;
    /** Selected indicator. */
    private boolean selected;


    /**
     * Constructor.
     */
    public LocalGroupMember() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LocalGroupMember other = (LocalGroupMember) obj;
        return Objects.equals(uid, other.uid);
    }


    /**
     * Getter for uid.
     * 
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Setter for uid.
     * 
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Getter for displayName.
     * 
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter for displayName.
     * 
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter for avatar.
     * 
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Setter for avatar.
     * 
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Getter for extra.
     * 
     * @return the extra
     */
    public String getExtra() {
        return extra;
    }

    /**
     * Setter for extra.
     * 
     * @param extra the extra to set
     */
    public void setExtra(String extra) {
        this.extra = extra;
    }

    /**
     * Getter for selected.
     * 
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Setter for selected.
     * 
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
