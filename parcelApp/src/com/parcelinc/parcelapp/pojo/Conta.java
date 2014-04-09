package com.parcelinc.parcelapp.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Conta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private List<Usuario> usuarios = new ArrayList<Usuario>(0);

	public Conta(Long id, String nome, List<Usuario> usuarios) {
		super();
		this.id = id;
		this.nome = nome;
		setUsuarios(usuarios);
	}

	public Conta(String nome, List<Usuario> usuarios) {
		this(null, nome, usuarios);
	}

	public Conta(Long id) {
		this(id, null, null);
	}

	public Conta(String nome) {
		this(null, nome, null);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Usuario> getUsuarios() {
		return Collections.unmodifiableList(usuarios);
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios.clear();
		addAllUsuario(usuarios);
	}

	public boolean addAllUsuario(List<Usuario> usuarios) {
		return (usuarios != null) ? this.usuarios.addAll(usuarios) : true;
	}

	public boolean addUsuario(Usuario usuario) {
		return this.usuarios.add(usuario);
	}
	
	public boolean removeUsuario(Usuario usuario) {
		return this.usuarios.remove(usuario);
	}

}
