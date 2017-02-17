package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.enums.RoleType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="tracking_role")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role implements Serializable{

    private static final long serialVersionUID = -3467279920723998768L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, name="role_name")
    private String roleName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role_type")
    private RoleType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String name) {
        this.roleName = name;
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }

    // ========================================================================
    /**
     * {@inheritDoc}
     * @see Object#hashCode()
     */
    public int hashCode(){
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        builder.append(getRoleName());
        builder.append(getType());
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
                .append(getRoleName(), obj.getRoleName())
                .append(getType(), obj.getType())
                .isEquals();
    }
}
