package com.parcelinc.parcelapp.activity;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.PagamentoDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Fechamento;
import com.parcelinc.parcelapp.pojo.Pagamento;
import com.parcelinc.parcelapp.pojo.Usuario;
import com.parcelinc.parcelapp.util.Util;


public class RelatorioFechamento extends Activity {

	public static final String PARAM_CONTA = "conta";
	public static final String PARAM_MES_REF = "mes_ref";
	
	Context contexto;

	TextView txtConta;
	TableLayout tabela;

	Conta conta;
	String filtro;

	PagamentoDataBase dbPagamento;

	public PagamentoDataBase getDbPagamento() {
		if (dbPagamento == null) {
			dbPagamento = PagamentoDataBase.getInstance(contexto);
		}
		return dbPagamento;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relatorio_fechamento);

		contexto = this;

		tabela = (TableLayout) findViewById(R.id.tblDespesa);

		Intent it = getIntent();

		conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
		filtro = it.getStringExtra(PARAM_MES_REF);

		txtConta = (TextView) findViewById(R.id.txtFiltroFechamento);
		txtConta.setText(getString(R.string.lbl_fechamento, conta.getNome()));

		carregarPagamentos();
	}

	private void carregarPagamentos() {
		Fechamento<Usuario> fechamento = Fechamento.paraUsuario();

		List<Pagamento> lista = getDbPagamento().getList(null, filtro, null);
		for (Pagamento pagamento : lista) {
			// Se a despesa for da conta selecionada
			if (conta.getId().equals(pagamento.getDespesa().getIdConta())) {
				fechamento.addPagamento(pagamento.getUsuario(), pagamento);
			}
		}

		fechamento.fechar();
		
		for (Usuario usuario : fechamento.getChaves()) {
			View linha = LayoutInflater.from(contexto).inflate(
					R.layout.linha_fechamento, tabela, false);

	        TextView txt = (TextView) linha.findViewById(R.id.txtRowUser);
	        txt.setText(usuario.getNome());

	        txt = (TextView) linha.findViewById(R.id.txtRowPgto);
	        Util.setTextUnderline(txt, String.format("%.3f", fechamento.getTotalPagamento(usuario)));
			txt.setTag(fechamento.getPagamentos(usuario));
			//txt.setOnClickListener(clickNome); // TODO implementar click

			txt = (TextView) linha.findViewById(R.id.txtRowSaldo);
			txt.setText(String.format("%.3f", fechamento.getSaldo(usuario)));

			tabela.addView(linha);
		}
	}
	
}
