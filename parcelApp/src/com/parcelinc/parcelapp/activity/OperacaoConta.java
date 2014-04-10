package com.parcelinc.parcelapp.activity;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.DateUtil;
import com.parcelinc.parcelapp.pojo.Conta;

public class OperacaoConta extends Activity {

	public static final String PARAM_CONTA = "conta";

	Context contexto;
	Conta conta;
	
	TextView txtConta;

	Spinner spnMes;
	EditText edtAno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conta_operacao);

		Intent it = getIntent();
		conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
		
		txtConta = (TextView) findViewById(R.id.txtNomeConta);
		txtConta.setText(conta.getNome());
		
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR_OF_DAY);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		int mes = cal.get(Calendar.MONTH);
		int ano = cal.get(Calendar.YEAR);

		edtAno = (EditText) findViewById(R.id.edtAno);
		edtAno.setText(String.valueOf(ano));

		spnMes = (Spinner) findViewById(R.id.spnMes);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listaNomesMeses());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnMes.setAdapter(adapter);
		
		spnMes.setSelection(mes);
	}

	private List<String> listaNomesMeses() {
		List<String> lista = new ArrayList<String>();

		String[] array = DateFormatSymbols.getInstance().getMonths();
		for (int i = 0; i < array.length; i++) {
			String mes = array[i];
			if (!"".equals(mes)) {
				lista.add(mes);
			}
		}
		
		return lista;
	}
	
	private String getFiltro() {
		int mes = spnMes.getSelectedItemPosition();
		String ano = edtAno.getText().toString();

		return DateUtil.getFilterMonth(ano, mes);
	}
	
	public void showDespesa(View view) {
		Intent it = new Intent(contexto, ListarDespesa.class);
		it.putExtra(ListarDespesa.PARAM_CONTA, conta);
		it.putExtra(ListarDespesa.PARAM_MES_REF, getFiltro());

		startActivity(it);
	}

	public void showFechamento(View view) {
		Intent it = new Intent(contexto, RelatorioFechamento.class);
		it.putExtra(RelatorioFechamento.PARAM_CONTA, conta);
		it.putExtra(RelatorioFechamento.PARAM_MES_REF, getFiltro());

		startActivity(it);
	}

}
