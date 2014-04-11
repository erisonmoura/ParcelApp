package com.parcelinc.parcelapp.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.DateUtil;
import com.parcelinc.parcelapp.db.DespesaDataBase;
import com.parcelinc.parcelapp.db.PagamentoDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Despesa;
import com.parcelinc.parcelapp.pojo.Usuario;

public class ManterDespesa extends Activity {

	public static final String PARAM_CONTA = "conta";

	private Context contexto;
	
	private CheckBox chkRepeat;
	private LinearLayout layoutRepeat;
	
	private SparseArray<EditText> campos;
	private Spinner spnUser;
	private TextView txtData;

	private Conta conta;
	private Calendar calendario;
	
	private DespesaDataBase dbDespesa;
	private PagamentoDataBase dbPagamento;

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
		setContentView(R.layout.despesa_manter);
		
		contexto = this;

		chkRepeat = (CheckBox) findViewById(R.id.chkRecorrente);
		layoutRepeat = (LinearLayout) findViewById(R.id.layoutRecorrente);
		clickRecorrente(chkRepeat);
		
		spnUser = (Spinner) findViewById(R.id.spnUser);
		txtData = (TextView) findViewById(R.id.txtData);
		
		campos = new SparseArray<EditText>();

		EditText campo = (EditText) findViewById(R.id.edtNome);
		campos.append(R.id.edtNome, campo);

		campo = (EditText) findViewById(R.id.edtValor);
		campos.append(R.id.edtValor, campo);
		
		campo = (EditText) findViewById(R.id.edtQntd);
		campos.append(R.id.edtQntd, campo);
		
		campo = (EditText) findViewById(R.id.edtData);
		campos.append(R.id.edtData, campo);

		Calendar c = Calendar.getInstance();
		campo.setText(DateUtil.toString(c));

		conta = (Conta) getIntent().getSerializableExtra(PARAM_CONTA);

	}

	public void clickRecorrente(View view) {
		CheckBox check = (CheckBox) view;
		if (check.isChecked()) {
			layoutRepeat.setVisibility(View.VISIBLE);
			txtData.setText(getString(R.string.lbl_data_pagamento, " 1º"));
		} else {
			layoutRepeat.setVisibility(View.INVISIBLE);
			txtData.setText(getString(R.string.lbl_data_pagamento, ""));
		}
	}

	public void alterarData(View view) {
		// TODO Implementar Dialogo com preenchimento da data
	}
	
	public void salvarDespesa(View view) {
		String nome = campos.get(R.id.edtNome).getText().toString();

		String valorStr = campos.get(R.id.edtValor).getText().toString();
		// TODO Definir e utilizar um DecimalFormat adequado
		double valor = Double.valueOf(valorStr);
		
		Usuario user = (Usuario) spnUser.getSelectedItem();
		int qntd = 1;
		if (chkRepeat.isChecked()) {
			String qntdStr = campos.get(R.id.edtQntd).getText().toString();
			qntd = Integer.valueOf(qntdStr);
		}
		
		Despesa despesa = new Despesa(nome, conta.getId(), null);
		
		getDbDespesa().insert(despesa, qntd, valor, user.getId(), calendario);
	}

}
