package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by pzhelnov on 1/23/2017.
 */
@Entity
@Table(name="container_type")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerType implements Serializable {
    private static final long serialVersionUID = -6886848877574564547L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "type can't be null")
    @Column(nullable = false, name = "type")
    private String type;

    @Column(nullable = true, name = "length")
    private Integer length;


    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
