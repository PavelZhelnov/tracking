package com.tmw.tracking.domain;

/**
 * Class represents sort criteria for search.
 * <p/>
 * Created by ankultepin on 5/14/2015.
 */
public class SearchingSortCriteria {
    private SortOrder lastName;
    private SortOrder dueDateTime;

    public SortOrder getLastName() {
        return lastName;
    }

    public SortOrder getDueDateTime() {
        return dueDateTime;
    }

    public void setLastName(SortOrder lastName) {
        this.lastName = lastName;
    }

    public void setDueDateTime(SortOrder dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
}