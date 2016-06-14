package org.osivia.services.workspace.portlet.model;

/**
 * Record java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class Record {

    /** Identifier. */
    private String id;
    /** Type. */
    private RecordType type;
    /** Display name. */
    private String displayName;
    /** Avatar URL. */
    private String avatar;
    /** Extra informations. */
    private String extra;


    /**
     * Constructor.
     */
    public Record() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Record)) {
            return false;
        }
        Record other = (Record) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }


    /**
     * Getter for id.
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter for id.
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for type.
     *
     * @return the type
     */
    public RecordType getType() {
        return this.type;
    }

    /**
     * Setter for type.
     *
     * @param type the type to set
     */
    public void setType(RecordType type) {
        this.type = type;
    }

    /**
     * Getter for displayName.
     * @return the displayName
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Setter for displayName.
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
        return this.avatar;
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
        return this.extra;
    }

    /**
     * Setter for extra.
     *
     * @param extra the extra to set
     */
    public void setExtra(String extra) {
        this.extra = extra;
    }

}
