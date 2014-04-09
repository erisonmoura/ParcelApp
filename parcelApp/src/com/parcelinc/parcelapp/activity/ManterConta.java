package com.parcelinc.parcelapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.ContaDatabase;
import com.parcelinc.parcelapp.db.UsuarioDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Usuario;

public class ManterConta extends Activity {

	public static final String PARAM_CONTA = "conta";

	private static final int OPR_USER = 1;
	private static final int OPR_AUTO = 2;
	private static final int OPR_ALT = 3;

	private Conta conta;

	private Context contexto;

	UsuarioDataBase dbUser;
	ContaDatabase dbConta;
	
	private List<Usuario> usuariosConta;

	private EditText edtNome;
	private LinearLayout layoutItens;

	public UsuarioDataBase getDbUser() {
		if (dbUser == null) {
			dbUser = UsuarioDataBase.getInstance(contexto);
		}
		return dbUser;
	}
	
	public ContaDatabase getDbConta() {
		if (dbConta == null) {
			dbConta = ContaDatabase.getInstance(contexto);
		}
		return dbConta;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conta_manter);

		contexto = this;

		edtNome = (EditText) findViewById(R.id.edtConta);
		layoutItens = (LinearLayout) findViewById(R.id.layoutItens);

		Intent it = getIntent();

		usuariosConta = new ArrayList<Usuario>();
		if (it.hasExtra(PARAM_CONTA)) {
			conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
			if (conta != null) {
				edtNome.setText(conta.getNome());
				usuariosConta = new ArrayList<Usuario>(conta.getUsuarios());
			}
		}

		if (!carregarUsuarios()) {
			novoUsuario(OPR_AUTO);
			Toast.makeText(contexto, "Olha aqui", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean carregarUsuarios() {
		List<Usuario> lista = listarUsuarios();

		if (lista.size() > 0) {
			carregarUsuarios(lista);
			return true;
		} else {
			return false;
		}
	}
	
	private void carregarUsuarios(List<Usuario> lista) {
		OnCheckedChangeListener onCheckChange = new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					usuariosConta.add((Usuario) buttonView.getTag()); 
				} else {
					usuariosConta.remove((Usuario) buttonView.getTag());
				}
			}
		};

		layoutItens.removeAllViews();
		for (Usuario usuario : lista) {
	        View linha = LayoutInflater.from(contexto).inflate(R.layout.linha_usuario, layoutItens, false);
	        ImageView btnEdit = (ImageView) linha.findViewById(R.id.imgEdit);
	        btnEdit.setTag(usuario);

	        ImageView btnTrash = (ImageView) linha.findViewById(R.id.imgTrash);
	        btnTrash.setTag(usuario);

	        TextView txtUser = (TextView) linha.findViewById(R.id.txtRowUser);
	        txtUser.setText(usuario.getNome());

	        ToggleButton tglBtn = (ToggleButton) linha.findViewById(R.id.tglBtnCheck);
	        tglBtn.setChecked(usuariosConta.contains(usuario));
	        tglBtn.setTag(usuario);
	        tglBtn.setOnCheckedChangeListener(onCheckChange);

	        layoutItens.addView(linha);
		}
	}

	public void novoUsuario(View view) {
		novoUsuario(OPR_USER);
	}
	
	public void alterarUsuario(View view) {
		novoUsuario(OPR_ALT, (Usuario) view.getTag());
	}

	private Usuario usuarioParaExclusao;
	
	public void excluirUsuario(View view) {
		usuarioParaExclusao = (Usuario) view.getTag();
		// TODO exibir Dialogo
	}
	
	public void confirmarExclusao(View view) {
		getDbUser().remove(usuarioParaExclusao);
		carregarUsuarios();
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
		return getDbUser().getList();
	}

}
