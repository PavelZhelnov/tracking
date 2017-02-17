package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmw.tracking.entity.enums.TrackingStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
@Table(name="transaction_workflow")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionWorkflow {

    private static final long serialVersionUID = -6886848877574564547L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "type can't be null")
    @Enumerated
    @Column(nullable = false, name = "status")
    private TrackingStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tracking_transaction", nullable=false, updatable = true)
    private Transaction trackingTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrackingStatus getStatus() {
        return status;
    }

    public void setStatus(TrackingStatus status) {
        this.status = status;
    }

    public Transaction getTrackingTransaction() {
        return trackingTransaction;
    }

    public void setTrackingTransaction(Transaction trackingTransaction) {
        this.trackingTransaction = trackingTransaction;
    }
}
