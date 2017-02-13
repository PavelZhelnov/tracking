package com.tmw.tracking.domain;

import com.tmw.tracking.entity.Driver;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchCriteria {

    private Driver driver;
    private String terminal;
    private String date;
    private SearchingSortCriteria sortCriteria;

    public SearchingSortCriteria getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(SearchingSortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
