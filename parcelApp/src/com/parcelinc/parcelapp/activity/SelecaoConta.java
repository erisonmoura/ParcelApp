package com.parcelinc.parcelapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parcelinc.parcelapp.R;

public class SelecaoConta extends Activity {

	ListView listaContas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conta_selecao);
		
		listaContas = (ListView) findViewById(R.id.lstContas);
		
//		ArrayAdapter = new ArrayAdapter<T>(this, android.R.layout.simple_list_item_single_choice, List<>);
//		
//		listaContas.setAdapter(adapter);
	}

	public void novaConta(View view) {
		
	}
	
}
