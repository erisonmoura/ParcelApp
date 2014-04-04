package com.parcelinc.parcelapp.pojo;



import java.io.Serializable;
import java.util.List;

public class Despesa implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private Conta conta;
	private List<Pagamento> pagamentos;

	public Despesa(Long id, String nome, Conta conta, List<Pagamento> pagamentos) {
		super();
		this.id = id;
		this.nome = nome;
		this.conta = conta;
		this.pagamentos = pagamentos;
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
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

}
