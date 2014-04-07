package com.parcelinc.parcelapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Usuario;

public class ManterConta extends Activity {

	Conta conta;

	Context contexto;
	
	EditText edtNome;
	LinearLayout layoutItens;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conta_manter);
		
		contexto = this;
		
		edtNome = (EditText) findViewById(R.id.edtConta);
		layoutItens = (LinearLayout) findViewById(R.id.layoutItens);

		
		
		if (conta != null) {
			edtNome.setText(conta.getNome());
			
			// TODO Varrer Usuários para marcar na lista
		}
	}


	private List<Usuario> listarUsuarios() {
		//TODO Realizar consulta ao banco
		return new ArrayList<Usuario>(0);
	}

}
