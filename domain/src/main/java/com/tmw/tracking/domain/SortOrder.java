package com.tmw.tracking.domain;

/**
 * Created by ankultepin on 5/14/2015.
 * <p/>
 * Class represents order of sorting.
 */
public enum SortOrder {
    ASC("ASC"), DESC("DESC");

    private String order;

    SortOrder(String order) {
        this.order = order;
    }

    public String getSortOrder() {
        return order;
    }
}