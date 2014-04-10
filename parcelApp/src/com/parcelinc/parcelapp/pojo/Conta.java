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
	private List<Long> idsUsuario = new ArrayList<Long>(0);

	public Conta(Long id, String nome, List<Long> idsUsuario) {
		super();
		this.id = id;
		this.nome = nome;
		setIdsUsuarios(idsUsuario);
	}

	public Conta(String nome, List<Long> idsUsuario) {
		this(null, nome, idsUsuario);
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

	public List<Long> getIdsUsuario() {
		return Collections.unmodifiableList(idsUsuario);
	}

	public void setIdsUsuarios(List<Long> idsUsuario) {
		this.idsUsuario.clear();
		addAllIdsUsuario(idsUsuario);
	}

	@Override
	public String toString() {
		return getNome();
	}
	
	public boolean addAllIdsUsuario(List<Long> idsUsuario) {
		return (idsUsuario != null) ? this.idsUsuario.addAll(idsUsuario) : true;
	}

	public boolean addIdUsuario(Long idUsuario) {
		return this.idsUsuario.add(idUsuario);
	}
	
	public boolean removeIdUsuario(Long idUsuario) {
		return this.idsUsuario.remove(idUsuario);
	}

}
