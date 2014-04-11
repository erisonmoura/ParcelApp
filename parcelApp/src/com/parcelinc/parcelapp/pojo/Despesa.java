package com.parcelinc.parcelapp.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Despesa implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Long id;
	private String nome;
	private Long idConta;
	private List<Long> idsPagamento = new ArrayList<Long>(0);

	public Despesa(Long id, String nome, Long idConta, List<Long> idsPagamento) {
		super();
		this.id = id;
		this.nome = nome;
		this.idConta = idConta;
		setIdsPagamento(idsPagamento);
	}

	public Despesa(String nome, Long idConta, List<Long> idsPagamento) {
		this(null, nome, idConta, idsPagamento);
	}

	@Override
	public String toString() {
		return getNome();
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

	public Long getIdConta() {
		return idConta;
	}

	public void setIdConta(Long idConta) {
		this.idConta = idConta;
	}

	public List<Long> getIdsPagamento() {
		return Collections.unmodifiableList(idsPagamento);
	}

	public void setIdsPagamento(List<Long> idsPagamento) {
		this.idsPagamento.clear();
		addAllIdsPagamento(idsPagamento);
	}

	public boolean addAllIdsPagamento(List<Long> idsPagamento) {
		return (idsPagamento != null) ? this.idsPagamento.addAll(idsPagamento) : true;
	}

	public boolean addIdPagamento(Long idPagamento) {
		return this.idsPagamento.add(idPagamento);
	}

}
