package com.tmw.tracking.dao.impl;

import com.tmw.tracking.entity.Driver;

import java.util.List;

public interface DriverDao {

    Driver getById(final Long id);

    Driver getByMobile(String mobile);

    List<Driver> getDriversByName(final String name);

    Driver create(final Driver driver);

    Driver update(final Driver driver);

    void delete(final Driver driver);

    List<Driver> getAll();

}