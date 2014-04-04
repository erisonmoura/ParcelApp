package com.parcelinc.parcelapp.pojo;



import java.io.Serializable;
import java.util.List;

public class Conta implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private List<Usuario> usuarios;

	public Conta(Long id, String nome, List<Usuario> usuarios) {
		super();
		this.id = id;
		this.nome = nome;
		this.usuarios = usuarios;
	}

	public Conta(String nome, List<Usuario> usuarios) {
		this(null, nome, usuarios);
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
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}
