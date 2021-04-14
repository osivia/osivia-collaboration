package org.osivia.services.editor.image.portlet.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Attached image java-bean.
 *
 * @author Cédric Krommenhoek
 * @see Comparable
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AttachedImage implements Comparable<AttachedImage> {

    /**
     * Index.
     */
    private int index;
    /**
     * File name.
     */
    private String fileName;
    /**
     * URL.
     */
    private String url;


    /**
     * Constructor.
     */
    public AttachedImage() {
        super();
    }


    @Override
    public int compareTo(AttachedImage other) {
        return Integer.compare(this.index, other.index);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AttachedImage)) return false;

        AttachedImage that = (AttachedImage) o;

        return new EqualsBuilder().append(index, that.index).isEquals();
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(index).toHashCode();
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
