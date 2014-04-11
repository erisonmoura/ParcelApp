package com.parcelinc.parcelapp.db;

import java.util.List;

public interface DataBase<T> {

	public long insert(T object);

	public long update(T object);

	public void remove(T object);

	public List<T> getList();

	public T get(long id);

}