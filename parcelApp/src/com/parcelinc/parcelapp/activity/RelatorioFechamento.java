package com.parcelinc.parcelapp.activity;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
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
	
	private Context contexto;

	private TextView txtConta;
	private TableLayout tabela;

	private Conta conta;
	private String filtro;

	public PagamentoDataBase getPagamentoDB() {
		return PagamentoDataBase.getInstance(contexto);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relatorio_fechamento);

		contexto = this;

		tabela = (TableLayout) findViewById(R.id.tblFechamento);

		Intent it = getIntent();

		conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
		filtro = it.getStringExtra(PARAM_MES_REF);

		txtConta = (TextView) findViewById(R.id.txtFiltroFechamento);
		txtConta.setText(getString(R.string.lbl_fechamento, conta.getNome()));

		carregarPagamentos();
	}

	private void carregarPagamentos() {
		Fechamento<Usuario> fechamento = Fechamento.paraUsuario();

		List<Pagamento> lista = getPagamentoDB().getList(conta.getId(), filtro, null, null);
		for (Pagamento pagamento : lista) {
			fechamento.addPagamento(pagamento.getUsuario(), pagamento);
		}

		fechamento.fechar();
		
		for (Usuario usuario : fechamento.getChaves()) {
			View linha = LayoutInflater.from(contexto).inflate(
					R.layout.linha_fechamento, tabela, false);

			TableRow row = (TableRow) linha.findViewById(R.id.tableRowFechamento);
			row.setTag(fechamento.getPagamentos(usuario).get(0));
			
	        TextView txt = (TextView) linha.findViewById(R.id.txtRowUser);
	        txt.setText(usuario.getNome());

	        txt = (TextView) linha.findViewById(R.id.txtRowPgto);
	        Util.setTextUnderline(txt, Util.doubleToString(fechamento.getTotalPagamento(usuario)));

			txt = (TextView) linha.findViewById(R.id.txtRowSaldo);
			txt.setText(Util.doubleToString(fechamento.getSaldo(usuario)));

			tabela.addView(linha);
		}
	}
	
	public void detalharFechamento(View view) {
		Pagamento pagamento = (Pagamento) view.getTag();
		Intent it = new Intent(contexto, RelatorioDetalhe.class);
		it.putExtra(RelatorioDetalhe.PARAM_PGTO, pagamento);
		it.putExtra(RelatorioDetalhe.PARAM_MES_REF, filtro);

		startActivity(it);
	}
	
}
