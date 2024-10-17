package com.huongbien.dao;

import java.util.List;

public abstract class Base_DAO<T> {
    public abstract boolean add(T object);
    public abstract boolean update(T object);

    public abstract List<T> get();
    public abstract T get(String id);
}
