package com.parcelinc.parcelapp.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.UsuarioDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Usuario;

public class ManterConta extends Activity {

	public static final String PARAM_CONTA = "conta";

	private static final int OPR_USER = 1;
	private static final int OPR_AUTO = 2;
	private static final int OPR_ALT = 3;

	private Conta conta;

	private ManterConta contexto;

	private List<Usuario> lista;

	private EditText edtNome;
	private LinearLayout layoutItens;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conta_manter);

		contexto = this;

		edtNome = (EditText) findViewById(R.id.edtConta);
		layoutItens = (LinearLayout) findViewById(R.id.layoutItens);

		List<Usuario> lista = listarUsuarios();

		if (lista.size() > 0) {
			carregarUsuarios(lista);
		} else {
			novoUsuario(OPR_AUTO);
		}

		Intent it = getIntent();

		if (it.hasExtra(PARAM_CONTA)) {
			conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
			if (conta != null) {
				edtNome.setText(conta.getNome());

				// TODO Varrer Usuários para marcar na lista
			}
		}
	}

	private void carregarUsuarios(List<Usuario> lista) {
		layoutItens.removeAllViews();
		this.lista = lista;
		
		
		for (Usuario usuario : lista) {
	        View linha = LayoutInflater.from(contexto).inflate(R.layout.linha_usuario, layoutItens, false);
	        TextView txtUser = (TextView) linha.findViewById(R.id.txtRowUser);
	        txtUser.setText(usuario.getNome());

	        ToggleButton tglBtn = (ToggleButton) linha.findViewById(R.id.tglBtnCheck);
	        tglBtn.setChecked(conta.getUsuarios().contains(usuario));
	        tglBtn.setTag(usuario);
	        tglBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						contexto.conta.addUsuario((Usuario) buttonView.getTag()); 
					} else {
						contexto.conta.removeUsuario((Usuario) buttonView.getTag());
					}
				}
			});

	        layoutItens.addView(linha);
		}
	}

	private void novoUsuario(int operacao, Usuario usuario) {
		Intent it = new Intent(contexto, ManterUser.class);
		if (usuario != null) {
			it.putExtra(ManterUser.PARAM_USER, usuario);
		}
		startActivityForResult(it, operacao);
	}

	private void novoUsuario(int operacao) {
		novoUsuario(operacao, null);
	}

	private List<Usuario> listarUsuarios() {
		return UsuarioDataBase.getInstance(contexto).getList();
	}

}
