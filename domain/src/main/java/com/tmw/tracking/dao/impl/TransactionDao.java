package com.tmw.tracking.dao.impl;

import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Transaction;

import java.util.List;

/**
 * Created by pzhelnov on 1/25/2017.
 */
public interface TransactionDao {

    Transaction create(Transaction transaction);

    List<Transaction> search(SearchCriteria searchCriteria);

}
