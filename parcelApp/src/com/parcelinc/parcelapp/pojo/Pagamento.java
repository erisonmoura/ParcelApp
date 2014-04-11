package com.parcelinc.parcelapp.pojo;


public class Pagamento {

	
	private Long id;
	private Despesa despesa;
	private String data;
	private Usuario usuario;
	private double valor;

	public Pagamento(Long id, Despesa despesa, String data, Usuario usuario,
			double valor) {
		super();
		this.id = id;
		this.despesa = despesa;
		this.data = data;
		this.usuario = usuario;
		this.valor = valor;
	}

	public Pagamento(Despesa despesa, String data, Usuario usuario, double valor) {
		this(null, despesa, data, usuario, valor);
	}
	public Pagamento(Long id){
		this(id, null, null, null, 0);
		
	}
	public Pagamento(String data, double valor){
		this(null, null, data, null, valor);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	

}
