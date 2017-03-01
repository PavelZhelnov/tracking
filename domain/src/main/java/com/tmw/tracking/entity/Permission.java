package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.entity.enums.RoleType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name="tracking_permission")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Permission implements Serializable{

    private static final long serialVersionUID = -3467279920723998768L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, name="name")
    private PermissionType name;

    @Column(nullable = true, name = "description")
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionType getName() {
        return name;
    }

    public void setName(PermissionType name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ========================================================================
    /**
     * {@inheritDoc}
     * @see Object#hashCode()
     */
    public int hashCode(){
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        builder.append(getDescription());
        builder.append(getName());
        return builder.toHashCode();
    }

    /**
     * {@inheritDoc}
     * @see Object#equals(Object)
     */
    public boolean equals(final Object object){
        if (!(object instanceof Permission))
            return false;
        if (this == object)
            return true;
        final Permission obj = (Permission) object;

        return new EqualsBuilder()
                .append(getId(), obj.getId())
                .append(getName(), obj.getName())
                .append(getDescription(), obj.getDescription())
                .isEquals();
    }
}
