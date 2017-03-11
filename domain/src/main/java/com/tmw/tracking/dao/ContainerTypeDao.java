package com.tmw.tracking.dao;

import com.tmw.tracking.entity.ContainerType;

import java.util.List;

/**
 * Created by pzhelnov on 3/6/2017.
 */
public interface ContainerTypeDao {

    ContainerType getById(Long id);

    List<ContainerType> getAll();

    ContainerType update(ContainerType containerType);

    void delete(ContainerType containerType);

}
