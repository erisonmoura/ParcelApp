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

	// TODO Tratar apenas a lista de IDs dos Pagamentos
	
	private Long id;
	private String nome;
	private Conta conta;
	private List<Pagamento> pagamentos = new ArrayList<Pagamento>(0);

	public Despesa(Long id, String nome, Conta conta, List<Pagamento> pagamentos) {
		super();
		this.id = id;
		this.nome = nome;
		this.conta = conta;
		setPagamentos(pagamentos);
	}

	public Despesa(String nome, Conta conta, List<Pagamento> pagamentos) {
		this(null, nome, conta, pagamentos);

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

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public List<Pagamento> getPagamentos() {
		return Collections.unmodifiableList(pagamentos);
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos.clear();
		addAllPagamento(pagamentos);
	}

	public boolean addAllPagamento(List<Pagamento> pagamentos) {
		return (pagamentos != null) ? this.pagamentos.addAll(pagamentos) : true;
	}

	public boolean addPagamento(Pagamento pagamento) {
		return this.pagamentos.add(pagamento);
	}

}
