package com.parcelinc.parcelapp.pojo;


public class Pagamento {

	private Long id;
	private Long idDespesa;
	private String data;
	private Long idUsuario;
	private double valor;

	public Pagamento(Long id, Long idDespesa, String data, Long idUsuario,
			double valor) {
		super();
		this.id = id;
		this.idDespesa = idDespesa;
		this.data = data;
		this.idUsuario = idUsuario;
		this.valor = valor;
	}

	public Pagamento(Long idDespesa, String data, Long idUsuario, double valor) {
		this(null, idDespesa, data, idUsuario, valor);
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

	public Long getIdDespesa() {
		return idDespesa;
	}

	public void setIdDespesa(Long idDespesa) {
		this.idDespesa = idDespesa;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

}
