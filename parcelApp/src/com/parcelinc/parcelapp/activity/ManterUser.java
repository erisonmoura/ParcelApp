package com.parcelinc.parcelapp.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.UsuarioDataBase;
import com.parcelinc.parcelapp.pojo.Usuario;

public class ManterUser extends Activity {

	EditText edtNome;
	EditText edtObs;

	Usuario usuario;
	UsuarioDataBase db;
	
	Context contexto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_manter);
		
		contexto = this;

		edtNome = (EditText) findViewById(R.id.edtUsuario);
		edtObs = (EditText) findViewById(R.id.edtObservacao);
		
		if (usuario != null) {
			edtNome.setText(usuario.getNome());
			edtObs.setText(usuario.getObs());
		}
	}

	public void salvarUsuario(View view) {
		db = UsuarioDataBase.getInstance(contexto);
		String nome = edtNome.getText().toString();
		String obs = edtObs.getText().toString();
		if (usuario == null) {
			usuario = new Usuario(nome, obs);
			db.insert(usuario);
		} else {
			usuario.setNome(nome);
			usuario.setObs(obs);
			db.update(usuario);
		}

		setResult(RESULT_FIRST_USER);
		finish();
	}

}
