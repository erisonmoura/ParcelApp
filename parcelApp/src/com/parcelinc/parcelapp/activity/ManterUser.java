package com.parcelinc.parcelapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.UsuarioDataBase;
import com.parcelinc.parcelapp.pojo.Usuario;

public class ManterUser extends Activity {

	public static final String PARAM_USER = "usuario";

	EditText edtNome;
	EditText edtObs;

	Usuario usuario;
	
	Context contexto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_manter);
		
		contexto = this;

		edtNome = (EditText) findViewById(R.id.edtUsuario);
		edtObs = (EditText) findViewById(R.id.edtObservacao);

		Intent it = getIntent();
		if (it.hasExtra(PARAM_USER)) {
			usuario = (Usuario) it.getSerializableExtra(PARAM_USER);
			if (usuario != null) {
				edtNome.setText(usuario.getNome());
				edtObs.setText(usuario.getObs());
			}
		}
	}

	public void salvar(View view) {
		String nome = edtNome.getText().toString();
		String obs = edtObs.getText().toString();

		if ("".equals(nome)) {
			edtNome.requestFocus();
			Toast.makeText(contexto, R.string.msg_valid_required, Toast.LENGTH_LONG).show();
			return;
		}

		// TODO validar duplicidade de nome
		
		UsuarioDataBase db = UsuarioDataBase.getInstance(contexto);
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
