package com.parcelinc.parcelapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.pojo.Conta;

public class SelecaoConta extends Activity {

	private static final int OPR_USER = 1;
	private static final int OPR_AUTO = 2;
	private static final int OPR_ALT = 3;
	
	private Context contexto;
	private ListView listaContas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conta_selecao);

		contexto = this;
		listaContas = (ListView) findViewById(R.id.lstContas);
		listaContas.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Implementar exibição de menu suspenso
				return false;
			}

		});

		List<Conta> lista = listarContas();
		
		if (lista.size() > 0) {
			carregarContas(lista);
		} else {
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
				carregarContas(listarContas());
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
	
	public void selecionarConta(View view) {
		Object item = listaContas.getSelectedItem();

		Intent it = new Intent(contexto, OperacaoConta.class);
		// FIXME Preparar e passar a Conta selecionada

		startActivity(it);
	}
	
	private void carregarContas(List<Conta> lista) {
		
		ArrayAdapter<Conta> adapter = new ArrayAdapter<Conta>(contexto,
				android.R.layout.simple_list_item_single_choice, lista);
		
		listaContas.setAdapter(adapter);
	}
	
	private void novaConta(int operacao) {
		Intent it = new Intent(contexto, ManterConta.class);
		startActivityForResult(it, operacao);
	}
	
	private List<Conta> listarContas() {
		//TODO Realizar consulta ao banco
		return new ArrayList<Conta>(0);
	}

}
