package com.parcelinc.parcelapp.db;

import java.util.List;

public interface DataBase<T> {

	public long insert(T object);

	public void remove(String nome);
	
	public List<T> getList();
	
	public List<T> getList(String data1, String data2);
}