package com.parcelinc.parcelapp.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.DespesaDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Despesa;
import com.parcelinc.parcelapp.pojo.Usuario;
import com.parcelinc.parcelapp.util.OnClickDate;
import com.parcelinc.parcelapp.util.Util;

public class NovaDespesa extends Activity {

	public static final String PARAM_CONTA = "conta";

	private Context contexto;
	
	private CheckBox chkRepeat;
	private LinearLayout layoutRepeat;
	
	private SparseArray<EditText> campos;
	private Spinner spnUser;
	private TextView txtData;
	
	private OnClickDate clickDate;

	private Conta conta;
	
	private DespesaDataBase getDespesaDB() {
		return DespesaDataBase.getInstance(contexto);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despesa_nova);
		
		contexto = this;

		conta = (Conta) getIntent().getSerializableExtra(PARAM_CONTA);
		
		chkRepeat = (CheckBox) findViewById(R.id.chkRecorrente);
		layoutRepeat = (LinearLayout) findViewById(R.id.layoutRecorrente);
		
		spnUser = (Spinner) findViewById(R.id.spnUser);
		txtData = (TextView) findViewById(R.id.txtData);

		clickRecorrente(chkRepeat);
		
		List<Usuario> usuariosValidos = Util.consultarUsuariosValidos(conta, contexto);
		
		ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(this,
		          android.R.layout.simple_spinner_item, usuariosValidos);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnUser.setAdapter(adapter);


		campos = new SparseArray<EditText>();

		EditText campo = (EditText) findViewById(R.id.edtNome);
		campos.append(R.id.edtNome, campo);

		campo = (EditText) findViewById(R.id.edtValor);
		campos.append(R.id.edtValor, campo);
		
		campo = (EditText) findViewById(R.id.edtQntd);
		campos.append(R.id.edtQntd, campo);
		
		campo = (EditText) findViewById(R.id.edtData);
		campos.append(R.id.edtData, campo);

		clickDate = new OnClickDate(contexto, campo);

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

	public void salvarDespesa(View view) {
		String nome = campos.get(R.id.edtNome).getText().toString();

		String valorStr = campos.get(R.id.edtValor).getText().toString();
		double valor = Util.stringToDouble(valorStr);
		
		Usuario user = (Usuario) spnUser.getSelectedItem();
		int qntd = 1;
		if (chkRepeat.isChecked()) {
			String qntdStr = campos.get(R.id.edtQntd).getText().toString();
			qntd = Integer.valueOf(qntdStr);
		}
		
		Despesa despesa = new Despesa(nome, conta.getId(), null);
		
		long result = getDespesaDB().insert(despesa, qntd, valor, user.getId(), clickDate.getCalendar());
		if (result != -1) {
			setResult(RESULT_FIRST_USER);
			finish();
		}
	}

}
