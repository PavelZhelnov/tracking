package com.tmw.tracking.dao.impl;

import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Transaction;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by pzhelnov on 2/8/2017.
 */
public class TransactionDaoImpl implements TransactionDao {

    private EntityManager entityManager;


    @Inject
    public TransactionDaoImpl(
            final EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    @Override
    public Transaction create(Transaction transaction) {
        transaction.setTransactionDate(new Date());
        entityManager.persist(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> search(SearchCriteria searchCriteria) {

        String where = " where transactionDate > : transactionDate ";
        if (searchCriteria.getDriver() !=null) {
            where += " and det.driver = :driver ";
        }
        final TypedQuery<Transaction> query = entityManager.createQuery("" +
                "select t from Transaction t " +
                "join t.transactionDetails det " +
                "join det.driver d " +
                where, Transaction.class);
        if (searchCriteria.getDriver() != null) {
            query.setParameter("driver", searchCriteria.getDriver());
        }
        List<Transaction> transactionList = query.getResultList();
        return transactionList;
    }
}
