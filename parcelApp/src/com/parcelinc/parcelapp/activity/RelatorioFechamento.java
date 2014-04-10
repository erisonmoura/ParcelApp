package com.parcelinc.parcelapp.activity;
import com.parcelinc.parcelapp.R;

import android.app.Activity;
import android.os.Bundle;


public class RelatorioFechamento extends Activity {

	public static final String PARAM_CONTA = "conta";
	public static final String PARAM_MES_REF = "mes_ref";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relatorio_fechamento);
	}

}
