package com.tmw.tracking.domain;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class SearchCriteria {

    private List<String> orderStatuses;
    private String nameOrNumber;
    private Boolean priority;
    private Integer offset;
    private Integer length;
    private Long storeId;
    private SearchingSortCriteria sortCriteria;

    public List<String> getOrderStatuses() {
        return orderStatuses;
    }

    public void setOrderStatuses(List<String> status) {
        this.orderStatuses = status;
    }

    public String getNameOrNumber() {
        return nameOrNumber;
    }

    public void setNameOrNumber(String nameOrNumber) {
        this.nameOrNumber = nameOrNumber;
    }

    public Boolean getOverdue() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public SearchingSortCriteria getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(SearchingSortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

}
