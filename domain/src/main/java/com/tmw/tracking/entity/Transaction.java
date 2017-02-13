package com.tmw.tracking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by pzhelnov on 1/25/2017.
 */
@Entity
@Table(schema = "tracking", name="tracking_transaction")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    private static final long serialVersionUID = -6886848877574564547L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="terminal", nullable=false, updatable = true)
    private Terminal terminal;

    @NotNull(message = "Tracking line can't be null")
    @Column(nullable = false, name = "tracking_line")
    private String trackingLine;

    @Column(nullable = true, name = "transaction_1c")
    private String transaction1c;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<TransactionDetails> transactionDetails;

    @Column(nullable = false, name = "transaction_date")
    private Date transactionDate;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public String getTrackingLine() {
        return trackingLine;
    }

    public void setTrackingLine(String trackingLine) {
        this.trackingLine = trackingLine;
    }

    public String getTransaction1c() {
        return transaction1c;
    }

    public void setTransaction1c(String transaction1c) {
        this.transaction1c = transaction1c;
    }

    public List<TransactionDetails> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(List<TransactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
