package com.parcelinc.parcelapp.activity;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.parcelinc.parcelapp.R;

public class OperacaoConta extends Activity {

	Date dataFiltro;

	Spinner spnMes;
	EditText edtAno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conta_operacao);

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
		spnMes.setAdapter(adapter);
		
		spnMes.setSelection(mes);
	}

	private String[] listaNomesMeses() {
		return DateFormatSymbols.getInstance().getMonths();
	}

}
