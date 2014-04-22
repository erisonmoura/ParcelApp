package com.parcelinc.parcelapp.pojo;

import java.io.Serializable;
import java.util.Calendar;

import com.parcelinc.parcelapp.db.DateUtil;
import com.parcelinc.parcelapp.util.Util;

public class Pagamento implements Serializable {

	private static final long serialVersionUID = Util.SERIAL_VERSION_UID;

	private Long id;
	private Despesa despesa;
	private String data;
	private Usuario usuario;
	private double valor;

	public Pagamento(Long id, Despesa despesa, String data, Usuario usuario,
			double valor) {
		this.id = id;
		this.despesa = despesa;
		this.data = data;
		this.usuario = usuario;
		this.valor = valor;
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
	
	public String dataToString() {
		Calendar c = DateUtil.toCalendar(getData());
		return DateUtil.toString(c);
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
