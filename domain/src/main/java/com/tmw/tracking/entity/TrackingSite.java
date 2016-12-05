package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.enums.RoleType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "tracking", name="tracking_site")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingSite implements Serializable{

    private static final long serialVersionUID = -3467279920723998768L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, name="get_url")
    private String getUrl;

    @Column(unique = true, nullable = false, name="post_url")
    private String postUrl;

    @Column(nullable = false, name="success_element")
    private String successElement;

    @Column(name="failure_element")
    private String failureElement;

    @Column(name="enclose")
    private String enclose;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGetUrl() {
        return getUrl;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getSuccessElement() {
        return successElement;
    }

    public void setSuccessElement(String successElement) {
        this.successElement = successElement;
    }

    public String getFailureElement() {
        return failureElement;
    }

    public void setFailureElement(String failureElement) {
        this.failureElement = failureElement;
    }

    public String getEnclose() {
        return enclose;
    }

    public void setEnclose(String enclose) {
        this.enclose = enclose;
    }

    // ========================================================================
    /**
     * {@inheritDoc}
     * @see Object#hashCode()
     */
    public int hashCode(){
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        return builder.toHashCode();
    }

    /**
     * {@inheritDoc}
     * @see Object#equals(Object)
     */
    public boolean equals(final Object object){
        if (!(object instanceof Role))
            return false;
        if (this == object)
            return true;
        final Role obj = (Role) object;

        return new EqualsBuilder()
                .append(getId(), obj.getId())
                .isEquals();
    }
}
