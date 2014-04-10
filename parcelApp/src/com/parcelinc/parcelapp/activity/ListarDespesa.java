package com.parcelinc.parcelapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Despesa;
import com.parcelinc.parcelapp.pojo.Usuario;

public class ListarDespesa extends Activity {

	public static final String PARAM_CONTA = "conta";
	public static final String PARAM_MES_REF = "mes_ref";
	
	Context contexto;

	TextView txtConta;
	TableLayout tabela;
	List<Despesa> listaDespesa;
	
	Conta conta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despesa_listar);

		contexto = this;

		tabela = (TableLayout) findViewById(R.id.tblDespesa);

		Intent it = getIntent();

		listaDespesa = new ArrayList<Despesa>();

		conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
		if (conta == null) {
		}
		
		txtConta = (TextView) findViewById(R.id.txtTituloListaDespesa);
		txtConta.setText(getString(R.string.lbl_despesa_conta, conta.getNome()));

		
		//usuariosConta = new ArrayList<Usuario>(conta.getUsuarios());


		//carregarDespesas();
	}

}
