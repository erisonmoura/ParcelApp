package com.parcelinc.parcelapp.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pojo para agregação de dados para relatório
 * 
 * @author 03323540409
 * 
 * @param <K>
 *            Item para agregação de Pagamentos (Usuário ou Despesa)
 */
public class Fechamento<K> {

	private Map<K, List<Pagamento>> mapa;
	private Map<K, Valores> valores;

	private static Fechamento<Usuario> fechamentoUsuario = new Fechamento<Usuario>();
	private static Fechamento<Despesa> fechamentoDespesa = new Fechamento<Despesa>();

	private Fechamento() {
		mapa = new HashMap<K, List<Pagamento>>();
	}

	public static Fechamento<Usuario> paraUsuario() {
		return fechamentoUsuario;
	}

	public static Fechamento<Despesa> paraDespesa() {
		return fechamentoDespesa;
	}

	private class Valores {
		private double pagamentos;
		private double saldo;
	}
	
	public List<K> getChaves() {
		return new ArrayList<K>(mapa.keySet());
	}

	public List<Pagamento> getPagamentos(K key) {
		return new ArrayList<Pagamento>(mapa.get(key));
	}

	public boolean addPagamento(K key, Pagamento pagamento) {
		if (valores != null) {
			throw new IllegalStateException();
		}

		List<Pagamento> lista = mapa.get(key);
		if (lista == null) {
			lista = new ArrayList<Pagamento>();
			mapa.put(key, lista);
		}
		return lista.add(pagamento);
	}

	public void fechar() {
		valores = new HashMap<K, Valores>();
		int qntd = 0;
		double total = 0.0;
		for (K key : getChaves()) {
			Valores valor = new Valores();
			for (Pagamento pagamento : getPagamentos(key)) {
				valor.pagamentos += pagamento.getValor();
			}
			total += valor.pagamentos;
			qntd++;
			valores.put(key, valor);
		}
		
		double valorDevido = total / (double)qntd;
		
		for (K key : getChaves()) {
			Valores valor = valores.get(key);
			valor.saldo = valorDevido - valor.pagamentos;
		}
	}
	
	public double getTotalPagamento(K key) {
		if (valores == null) {
			return 0.0;
		} else {
			return valores.get(key).pagamentos;
		}
	}
	
	public double getSaldo(K key) {
		if (valores == null) {
			return 0.0;
		} else {
			return valores.get(key).saldo;
		}
	}

}
