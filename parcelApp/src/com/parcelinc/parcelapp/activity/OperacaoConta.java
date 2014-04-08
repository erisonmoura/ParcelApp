package com.parcelinc.parcelapp.activity;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parcelinc.parcelapp.R;

public class OperacaoConta extends Activity {

	public static final String PARAM_CONTA = "conta";
	
	Date dataFiltro;

	TextView txtConta;

	Spinner spnMes;
	EditText edtAno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conta_operacao);

		txtConta = (TextView) findViewById(R.id.txtNomeConta);
		
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR_OF_DAY);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		dataFiltro = cal.getTime();

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

	private String[] listaNomesMeses() {
		return DateFormatSymbols.getInstance().getMonths();
	}

}
