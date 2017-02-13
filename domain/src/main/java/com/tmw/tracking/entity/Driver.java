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
@Table(schema = "tracking", name="driver")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Driver {
    private static final long serialVersionUID = -6886848877574564547L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "First Name can't be null")
    @Column(nullable = false, name = "first_name")
    private String firstName;

    @NotNull(message = "Last Name can't be null")
    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = true, name = "trailer_number")
    private String trailerNumber;

    @Column(nullable = true, name = "tractorNumber")
    private String tractorNumber;

    @Column(nullable = true, name = "mobile")
    private String mobile;


    @NotNull(message = "Mobile can't be null")
    @Column(nullable = false, name = "length")
    private Integer length;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public String getTractorNumber() {
        return tractorNumber;
    }

    public void setTractorNumber(String tractorNumber) {
        this.tractorNumber = tractorNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
