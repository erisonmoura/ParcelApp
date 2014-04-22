package com.parcelinc.parcelapp.activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.ContaDatabase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.util.Util;

public class SelecaoConta extends Activity {

	private static final int OPR_USER = 1;
	private static final int OPR_AUTO = 2;
	private static final int OPR_ALT = 3;
	
	private Context contexto;

	private ContaDatabase dbConta;
	private LinearLayout layoutItens;
	
	
	private class OnClickExcuir implements OnClickListener {

		private Conta conta;

		public OnClickExcuir(Conta conta) {
			this.conta = conta;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			getDbConta().remove(conta);
			carregarContas();
		}
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
		setContentView(R.layout.conta_selecao);

		contexto = this;
		layoutItens = (LinearLayout) findViewById(R.id.layoutItens);

		if (!carregarContas()) {
			novaConta(OPR_AUTO);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case OPR_USER:
		case OPR_AUTO:
		case OPR_ALT:
			if (resultCode == RESULT_FIRST_USER) {
				carregarContas();
			} else if (requestCode == OPR_AUTO) {
				finish();
			}
			break;

		default:
			// Não precisa realizar operação
			break;
		}
	}
	
	public void novaConta(View view) {
		novaConta(OPR_USER);
	}
	
	public void alterarConta(View view) {
		novaConta(OPR_ALT, (Conta) view.getTag());
	}

	public void excluirConta(View view) {
		Conta contaParaExclusao = (Conta) view.getTag();
		confirmarExclusao(contaParaExclusao);
	}
	
	public void selecionarConta(View view) {
		selecionarConta((Conta) view.getTag());
	}

	private void selecionarConta(Conta item) {
		Intent it = new Intent(contexto, OperacaoConta.class);
		it.putExtra(OperacaoConta.PARAM_CONTA, item);

		startActivity(it);
	}

	private boolean carregarContas() {
		layoutItens.removeAllViews();
		List<Conta> lista = listarContas();
		
		if (lista.size() > 0) {
			carregarContas(lista);
			return true;
		} else {
			return false;
		}
	}
	
	private void carregarContas(List<Conta> lista) {
		for (Conta conta : lista) {
	        View linha = LayoutInflater.from(contexto).inflate(R.layout.linha_conta, layoutItens, false);
	        linha.setTag(conta);

	        ImageView btnEdit = (ImageView) linha.findViewById(R.id.imgEdit);
	        btnEdit.setTag(conta);

	        ImageView btnTrash = (ImageView) linha.findViewById(R.id.imgTrash);
	        btnTrash.setTag(conta);

	        TextView txtConta = (TextView) linha.findViewById(R.id.txtRowConta);
			Util.setTextUnderline(txtConta, conta.getNome());

	        layoutItens.addView(linha);
		}
	}
	
	private void confirmarExclusao(Conta conta) {
		OnClickExcuir onclick = new OnClickExcuir(conta);
		
		String msg = getString(R.string.msg_confirmar_exclusao, " a conta " + conta);

		AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle(R.string.title_confirmar).setMessage(msg)
				.setPositiveButton("Sim", onclick)
				.setNegativeButton("Não", null).create();
		alert.show();
	}
	
	private void novaConta(int operacao, Conta conta) {
		Intent it = new Intent(contexto, ManterConta.class);
		if (conta != null) {
			it.putExtra(ManterConta.PARAM_CONTA, conta);
		}
		startActivityForResult(it, operacao);
	}

	private void novaConta(int operacao) {
		novaConta(operacao, null);
	}
	
	private List<Conta> listarContas() {
		return ContaDatabase.getInstance(contexto).getList();
	}

}
