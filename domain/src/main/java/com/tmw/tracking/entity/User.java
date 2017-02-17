package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.enums.RoleType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "tracking_user")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    private static final long serialVersionUID = 8872183864654190431L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Unique ID cannot be null")
    @Column(nullable = false, unique = true)
    private String email;
    @NotNull(message = "Password")
    @Column(nullable = false)
    private String password;
    @NotNull(message = "First Name cannot be null")
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @NotNull(message = "Last cannot be null")
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Type(type = "yes_no")
    private boolean active = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    private Date lastUpdated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tracking_user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@CollectionId(columns = @Column(name = "id"), type = @Type(type = "long"))
    private java.util.Collection<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean hasRole(RoleType roleType) {
        if (roles != null) {
            for (final Role role : roles) {
                if (roleType == role.getType())
                    return true;
            }
        }
        return false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

// ========================================================================

    /**
     * {@inheritDoc}
     *
     * @see Object#hashCode()
     */
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        builder.append(getEmail());
        return builder.toHashCode();
    }


    @Override
    public String toString() {
        return email;
    }

    /**
     * {@inheritDoc}
     *
     * @see Object#equals(Object)
     */
    public boolean equals(final Object object) {
        if (!(object instanceof User))
            return false;
        if (this == object)
            return true;
        final User obj = (User) object;

        return new EqualsBuilder()
                .append(getId(), obj.getId())
                .append(getEmail(), obj.getEmail())
                .isEquals();
    }
}