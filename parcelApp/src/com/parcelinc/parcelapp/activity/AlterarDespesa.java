package com.parcelinc.parcelapp.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.PagamentoDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Despesa;
import com.parcelinc.parcelapp.pojo.Pagamento;
import com.parcelinc.parcelapp.util.Util;

public class AlterarDespesa extends Activity {

	public static final String PARAM_CONTA = "conta";
	public static final String PARAM_MES_REF = "mes_ref";
	public static final String PARAM_DESPESA = "despesa";

	private static final int OPR_NOVO_PGTO = 1;
	private static final int OPR_ALTERAR_PGTO = 2;

	private Context contexto;

	private CheckBox chkFiltrar;
	private TextView txtDespesa;
	private TableLayout tabela;

	private Conta conta;
	private String filtro;
	private Despesa despesa;

	public PagamentoDataBase getPagamentoDB() {
		return PagamentoDataBase.getInstance(contexto);
	}

	private class OnClickExcuir implements OnClickListener {

		private Pagamento pagamento;

		public OnClickExcuir(Pagamento pagamento) {
			this.pagamento = pagamento;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			getPagamentoDB().remove(pagamento);
			carregarPagamentos();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despesa_alterar);

		contexto = this;

		tabela = (TableLayout) findViewById(R.id.tblPagamento);

		Intent it = getIntent();

		conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
		filtro = it.getStringExtra(PARAM_MES_REF);
		despesa = (Despesa) it.getSerializableExtra(PARAM_DESPESA);

		chkFiltrar = (CheckBox) findViewById(R.id.chkFiltrarMes);
		chkFiltrar.setChecked(true);
		
		txtDespesa = (TextView) findViewById(R.id.txtTituloPagamento);
		txtDespesa.setText(getString(R.string.lbl_pagamento_despesa, despesa.getNome()));
		

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
		
		List<Pagamento> lista = getPagamentoDB().getList(conta.getId(), filtroMes, null, despesa.getId());
		if (lista == null || lista.isEmpty()) {
			Toast.makeText(contexto, R.string.msg_sem_dados, Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		for (Pagamento pagamento : lista) {
			View linha = LayoutInflater.from(contexto).inflate(
					R.layout.linha_pagamento, tabela, false);

	        ImageView btnEdit = (ImageView) linha.findViewById(R.id.imgEdit);
	        btnEdit.setTag(pagamento);

	        ImageView btnTrash = (ImageView) linha.findViewById(R.id.imgTrash);
	        btnTrash.setTag(pagamento);

	        TextView txtData = (TextView) linha.findViewById(R.id.txtRowData);
	        String data = pagamento.dataToString();
	        txtData.setText(data.substring(0, 5));

			TextView txtUser = (TextView) linha.findViewById(R.id.txtRowUser);
			txtUser.setText(pagamento.getUsuario().getNome());

			TextView txtValor = (TextView) linha.findViewById(R.id.txtRowValor);
			txtValor.setText(Util.doubleToString(pagamento.getValor()));

			tabela.addView(linha);
		}
	}
	
	public void alterarPagamento(View view) {
		Pagamento pagamento = (Pagamento) view.getTag();
		Intent it = new Intent(contexto, ManterDespesa.class);
		it.putExtra(ManterDespesa.PARAM_CONTA, conta);
		it.putExtra(ManterDespesa.PARAM_PAGAMENTO, pagamento);

		startActivityForResult(it, OPR_ALTERAR_PGTO);
	}

	public void excluirPagamento(View view) {
		Pagamento pagamento = (Pagamento) view.getTag();
		confirmarExclusao(pagamento);
	}

	private void confirmarExclusao(Pagamento pagamento) {
		OnClickExcuir onclick = new OnClickExcuir(pagamento);

		StringBuilder sb = new StringBuilder(" o pagamento de ");
		sb.append(Util.doubleToString(pagamento.getValor()));
		sb.append(" em ").append(pagamento.dataToString());
		
		String msg = getString(R.string.msg_confirmar_exclusao, sb.toString());

		AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle(R.string.title_confirmar).setMessage(msg)
				.setPositiveButton("Sim", onclick)
				.setNegativeButton("Não", null).create();
		alert.show();
	}

	public void novoPagamento(View view) {
		Intent it = new Intent(contexto, ManterDespesa.class);
		it.putExtra(ManterDespesa.PARAM_CONTA, conta);
		it.putExtra(ManterDespesa.PARAM_DESPESA, despesa);

		startActivityForResult(it, OPR_NOVO_PGTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case OPR_NOVO_PGTO:
		case OPR_ALTERAR_PGTO:
			if (resultCode == RESULT_FIRST_USER) {
				carregarPagamentos();
			}
			break;

		default:
			// Não precisa realizar operação
			break;
		}
	}

}
