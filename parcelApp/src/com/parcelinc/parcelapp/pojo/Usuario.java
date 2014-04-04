package com.parcelinc.parcelapp.pojo;

import java.io.Serializable;

public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	private  String nome;
	private String obs;
	
	
	public Usuario(String nome, String obs) {
		super();
		this.nome = nome;
		this.obs = obs;
	}
	public Usuario(long id, String nome, String obs) {
		super();
		this.id = id;
		this.nome = nome;
		this.obs = obs;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
		
}
