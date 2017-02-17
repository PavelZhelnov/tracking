package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by pzhelnov on 1/25/2017.
 */
@Entity
@Table(name="transaction_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDetails {

    private static final long serialVersionUID = -6886848877574564547L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tracking_transaction", nullable=false, updatable = true)
    private Transaction trackingTransaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="driver", nullable=false, updatable = true)
    private Driver driver;

    @Column(nullable = true, name = "number")
    private String number;


    @Column(nullable = true, name = "weight")
    private Double weight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getTrackingTransaction() {
        return trackingTransaction;
    }

    public void setTrackingTransaction(Transaction trackingTransaction) {
        this.trackingTransaction = trackingTransaction;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
