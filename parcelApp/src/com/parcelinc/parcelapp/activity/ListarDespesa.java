package com.parcelinc.parcelapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.DespesaDataBase;
import com.parcelinc.parcelapp.db.PagamentoDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Despesa;

public class ListarDespesa extends Activity {

	public static final String PARAM_CONTA = "conta";
	public static final String PARAM_MES_REF = "mes_ref";

	Context contexto;

	TextView txtConta;
	TableLayout tabela;
	List<Despesa> listaDespesa;

	Conta conta;
	String filtro;
	
	DespesaDataBase dbDespesa;
	PagamentoDataBase dbPagamento;

	public DespesaDataBase getDbDespesa() {
		if (dbDespesa == null) {
			dbDespesa = DespesaDataBase.getInstance(contexto);
		}
		return dbDespesa;
	}
	
	public PagamentoDataBase getDbPagamento() {
		if (dbPagamento == null) {
			dbPagamento = PagamentoDataBase.getInstance(contexto);
		}
		return dbPagamento;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despesa_listar);

		contexto = this;

		tabela = (TableLayout) findViewById(R.id.tblDespesa);

		Intent it = getIntent();

		listaDespesa = new ArrayList<Despesa>();

		conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
		filtro = it.getStringExtra(PARAM_MES_REF);

		txtConta = (TextView) findViewById(R.id.txtTituloListaDespesa);
		txtConta.setText(getString(R.string.lbl_despesa_conta, conta.getNome()));

		// usuariosConta = new ArrayList<Usuario>(conta.getUsuarios());

		carregarDespesas();
	}
	
	private void carregarDespesas() {
		List<Despesa> lista = getDbDespesa().getList(conta.getId(), filtro);
		if (lista.isEmpty()) {
			Toast.makeText(contexto, R.string.msg_sem_dados, Toast.LENGTH_LONG).show();
			return;
		}

		for (Despesa despesa : lista) {
	        View linha = LayoutInflater.from(contexto).inflate(R.layout.linha_despesa, tabela, false);
	        linha.setTag(despesa);

	        TextView txtDespesa = (TextView) linha.findViewById(R.id.txtRowDespesa);
	        txtDespesa.setText(despesa.getNome());

	        TextView txtQuant = (TextView) linha.findViewById(R.id.txtRowQuant);
	        txtQuant.setText(getQuantidades(despesa));

	        tabela.addView(linha);
		}
	}
	
	private String getQuantidades(Despesa despesa) {
		StringBuilder sb = new StringBuilder();
		
		List<Long> idsPagamento = getDbDespesa().getListaPagamentos(despesa.getId(), filtro);
		// Esta contagem só funcionará se a lista de Pagamentos vier ordenada por Data
		for (int i = 0; i < despesa.getPagamentos().size(); i++) {
			Long id = despesa.getPagamentos().get(i).getId();

			for (Long idPgto : idsPagamento) {
				if (id.equals(idPgto)) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(i+1);
				}
			}
		}
		
		sb.append("/");
		sb.append(despesa.getPagamentos().size());

		return sb.toString();
	}

}
