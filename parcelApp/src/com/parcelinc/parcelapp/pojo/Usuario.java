package com.parcelinc.parcelapp.pojo;

import java.io.Serializable;

public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String obs;

	public Usuario(Long id, String nome, String obs) {
		super();
		this.id = id;
		this.nome = nome;
		this.obs = obs;
	}

	public Usuario(Long id) {
		this(id, null, null);
	}

	public Usuario(String nome, String obs) {
		this(null, nome, obs);
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

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (id != null) {
			result = prime * result + id.hashCode();
		} else {
			result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Usuario)) {
			return false;
		}
		Usuario other = (Usuario) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		} else if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		return true;
	}
	
}
