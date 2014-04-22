package com.parcelinc.parcelapp.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.PagamentoDataBase;
import com.parcelinc.parcelapp.pojo.Pagamento;
import com.parcelinc.parcelapp.util.Util;

public class RelatorioDetalhe extends Activity {

	public static final String PARAM_MES_REF = "mes_ref";
	public static final String PARAM_PGTO = "pagamento";
	
	private Context contexto;

	private CheckBox chkFiltrar;
	private TextView txtDetalhe;
	private TableLayout tabela;

	private Pagamento pagamento;
	private String filtro;

	public PagamentoDataBase getPagamentoDB() {
		return PagamentoDataBase.getInstance(contexto);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relatorio_detalhe);

		contexto = this;

		tabela = (TableLayout) findViewById(R.id.tblPagamento);

		Intent it = getIntent();

		filtro = it.getStringExtra(PARAM_MES_REF);
		pagamento = (Pagamento) it.getSerializableExtra(PARAM_PGTO);

		chkFiltrar = (CheckBox) findViewById(R.id.chkFiltrarMes);
		chkFiltrar.setChecked(true);

		txtDetalhe = (TextView) findViewById(R.id.txtTituloPagamento);
		txtDetalhe.setText(getString(R.string.lbl_pagamento_despesa, pagamento.getUsuario().getNome()));

		carregarPagamentos();
	}
	
	public void ajustarFiltro(View view) {
		carregarPagamentos();
	}

	private void carregarPagamentos() {
		tabela.removeViews(1, tabela.getChildCount()-1);

		String filtroMes = null;
		if (chkFiltrar.isChecked()) {
			filtroMes = filtro;
		}
		
		List<Pagamento> lista = getPagamentoDB().getList(
				pagamento.getDespesa().getIdConta(), filtroMes,
				pagamento.getUsuario().getId(), null);
		if (lista == null || lista.isEmpty()) {
			Toast.makeText(contexto, R.string.msg_sem_dados, Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		for (Pagamento pagamento : lista) {
			View linha = LayoutInflater.from(contexto).inflate(
					R.layout.linha_detalhe, tabela, false);

	        TextView txtData = (TextView) linha.findViewById(R.id.txtRowData);
	        String data = pagamento.dataToString();
	        txtData.setText(data.substring(0, 5));

			TextView txtUser = (TextView) linha.findViewById(R.id.txtRowDespesa);
			txtUser.setText(pagamento.getDespesa().getNome());

			TextView txtValor = (TextView) linha.findViewById(R.id.txtRowValor);
			txtValor.setText(Util.doubleToString(pagamento.getValor()));

			tabela.addView(linha);
		}
	}
	
}
