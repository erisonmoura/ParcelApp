package com.parcelinc.parcelapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parcelinc.parcelapp.R;

public class ManterUser extends Activity {

	EditText edtNome;
	EditText edtObs;

	Object usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_manter);
		
		edtNome = (EditText) findViewById(R.id.edtUsuario);
		edtObs = (EditText) findViewById(R.id.edtObservacao);
		
		if (usuario != null) {
			
		}
	}

	public void salvarUsuario(View view) {
		if (usuario == null) {
			usuario = new Object();
			// TODO SALVAR USUARIO
		} else {
			// TODO ALTERAR USUARIO
		}
		
		setResult(RESULT_FIRST_USER);
	}

}
