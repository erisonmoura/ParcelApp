package com.parcelinc.parcelapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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

	private List<Long> idsUsuarioConta;

	private EditText edtNome;
	private LinearLayout layoutItens;

	private class OnCheckedChange implements OnCheckedChangeListener {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				idsUsuarioConta.add((Long) buttonView.getTag()); 
			} else {
				idsUsuarioConta.remove((Long) buttonView.getTag());
			}
		}
	}

	private class OnClickExcuir implements OnClickListener {

		private Usuario usuario;

		public OnClickExcuir(Usuario usuario) {
			this.usuario = usuario;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			getUserDB().remove(usuario);
			carregarUsuarios();
		}
	}

	private UsuarioDataBase getUserDB() {
		return UsuarioDataBase.getInstance(contexto);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conta_manter);

		contexto = this;

		edtNome = (EditText) findViewById(R.id.edtConta);
		layoutItens = (LinearLayout) findViewById(R.id.layoutItens);

		Intent it = getIntent();

		idsUsuarioConta = new ArrayList<Long>();
		if (it.hasExtra(PARAM_CONTA)) {
			conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
			if (conta != null) {
				edtNome.setText(conta.getNome());
				idsUsuarioConta = new ArrayList<Long>(conta.getIdsUsuario());
			}
		}

		if (!carregarUsuarios()) {
			showWelcome();
		}
	}
	
	private void showWelcome() {
		OnClickListener onclick = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				novoUsuario(OPR_AUTO);
			}
		};
		
		String appName = getString(R.string.app_name); 
		String msg = getString(R.string.msg_welcome, appName);

		AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle(R.string.title_welcome).setMessage(msg)
				.setNeutralButton("Ok", onclick).create();
		alert.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case OPR_USER:
		case OPR_AUTO:
		case OPR_ALT:
			if (resultCode == RESULT_FIRST_USER) {
				carregarUsuarios();
			} else if (requestCode == OPR_AUTO) {
				finish();
			}
			break;

		default:
			// Não precisa realizar operação
			break;
		}
	}
	
	private boolean carregarUsuarios() {
		layoutItens.removeAllViews();
		List<Usuario> lista = listarUsuarios();

		if (lista.size() > 0) {
			carregarUsuarios(lista);
			return true;
		} else {
			return false;
		}
	}
	
	private void carregarUsuarios(List<Usuario> lista) {
		OnCheckedChangeListener onCheckChange = new OnCheckedChange();

		for (Usuario usuario : lista) {
	        View linha = LayoutInflater.from(contexto).inflate(R.layout.linha_usuario, layoutItens, false);
	        ImageView btnEdit = (ImageView) linha.findViewById(R.id.imgEdit);
	        btnEdit.setTag(usuario);

	        ImageView btnTrash = (ImageView) linha.findViewById(R.id.imgTrash);
	        btnTrash.setTag(usuario);

	        TextView txtUser = (TextView) linha.findViewById(R.id.txtRowUser);
	        txtUser.setText(usuario.getNome());

	        ToggleButton tglBtn = (ToggleButton) linha.findViewById(R.id.tglBtnCheck);
	        tglBtn.setChecked(idsUsuarioConta.contains(usuario.getId()));
	        tglBtn.setTag(usuario.getId());
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

	public void excluirUsuario(View view) {
		Usuario usuarioParaExclusao = (Usuario) view.getTag();
		confirmarExclusao(usuarioParaExclusao);
	}

	public void salvarConta(View view) {
		String nome = edtNome.getText().toString().trim();
		if ("".equals(nome)) {
			edtNome.requestFocus();
			Toast.makeText(contexto, R.string.msg_valid_required, Toast.LENGTH_LONG).show();
			return;
		}
		
		if (idsUsuarioConta.isEmpty()) {
			Toast.makeText(contexto, R.string.msg_valid_conta_seluser, Toast.LENGTH_LONG).show();
			return;
		}

		ContaDatabase db = ContaDatabase.getInstance(contexto);
		
		// TODO validar duplicidade de nome
		
		if (conta == null) {
			conta = new Conta(nome, idsUsuarioConta);
			db.insert(conta);
		} else {
			conta.setNome(nome);
			conta.setIdsUsuarios(idsUsuarioConta);
			db.update(conta);
		}

		setResult(RESULT_FIRST_USER);
		finish();
	}
	
	private void confirmarExclusao(Usuario usuario) {
		OnClickExcuir onclick = new OnClickExcuir(usuario);
		
		String msg = getString(R.string.msg_confirmar_exclusao, " o usuário " + usuario);

		AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle(R.string.title_confirmar).setMessage(msg)
				.setPositiveButton("Sim", onclick)
				.setNegativeButton("Não", null).create();
		alert.show();
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
		return getUserDB().getList();
	}

}
