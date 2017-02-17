package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by pzhelnov on 1/25/2017.
 */
@Entity
@Table(name="terminal")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Terminal {
    private static final long serialVersionUID = -6886848877574564547L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name can't be null")
    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = true, name = "address")
    private String address;

    @Column(nullable = true, name = "city")
    private String city;

    @Column(nullable = true, name = "country")
    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
