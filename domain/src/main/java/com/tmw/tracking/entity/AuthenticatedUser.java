package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "authenticated_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticatedUser implements Serializable {

    private static final long serialVersionUID = 688840065627799824L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "expired_date")
    private Date expired;

    // ------------------------------------------------------------------------


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
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
        if (!(object instanceof AuthenticatedUser))
            return false;
        if (this == object)
            return true;
        final AuthenticatedUser obj = (AuthenticatedUser) object;

        return new EqualsBuilder()
                .append(getId(), obj.getId())
                .append(getToken(), obj.getToken())
                .isEquals();
    }
}
