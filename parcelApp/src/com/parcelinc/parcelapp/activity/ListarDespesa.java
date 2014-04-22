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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.DespesaDataBase;
import com.parcelinc.parcelapp.db.PagamentoDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Despesa;
import com.parcelinc.parcelapp.util.Util;

public class ListarDespesa extends Activity {

	public static final String PARAM_CONTA = "conta";
	public static final String PARAM_MES_REF = "mes_ref";

	private static final int OPR_NOVA_DESPESA = 1;
	private static final int OPR_ALTERAR_DESPESA = 2;

	Context contexto;

	TextView txtConta;
	TableLayout tabela;

	Conta conta;
	String filtro;

	DespesaDataBase dbDespesa;
	PagamentoDataBase dbPagamento;

	private DespesaDataBase geDespesaDB() {
		return DespesaDataBase.getInstance(contexto);
	}

	private class OnClickExcuir implements OnClickListener {

		private Despesa despesa;

		public OnClickExcuir(Despesa despesa) {
			this.despesa = despesa;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			geDespesaDB().remove(despesa);
			carregarDespesas();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despesa_listar);

		contexto = this;

		tabela = (TableLayout) findViewById(R.id.tblDespesa);

		Intent it = getIntent();

		conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
		filtro = it.getStringExtra(PARAM_MES_REF);

		txtConta = (TextView) findViewById(R.id.txtTituloListaDespesa);
		txtConta.setText(getString(R.string.lbl_despesa_conta, conta.getNome()));

		carregarDespesas();
	}

	private class OnClickNomeDespesa implements android.view.View.OnClickListener {

		private TextView txt;
		private Despesa despesa;
		private EditText edt;

		@Override
		public void onClick(View v) {
			txt = (TextView) v;
			despesa = (Despesa) txt.getTag();

			edt = new EditText(contexto);
			edt.setHint(R.string.lbl_nome);
			edt.setText(despesa.getNome());

			AlertDialog dialogo = new AlertDialog.Builder(contexto)
					.setTitle(R.string.lbl_despesa).setView(edt)
					.setPositiveButton("Confirmar", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							despesa.setNome(edt.getText().toString());
							Util.setTextUnderline(txt, despesa.getNome());
						}
					}).setNegativeButton("Cancelar", null).create();

			dialogo.show();
		}
	}

	private void carregarDespesas() {
		List<Despesa> lista = geDespesaDB().getList(conta.getId(), filtro);
		if (lista == null || lista.isEmpty()) {
			Toast.makeText(contexto, R.string.msg_sem_dados, Toast.LENGTH_LONG)
					.show();
			return;
		}

		OnClickNomeDespesa clickNome = new OnClickNomeDespesa();

		for (Despesa despesa : lista) {
			View linha = LayoutInflater.from(contexto).inflate(
					R.layout.linha_despesa, tabela, false);

	        ImageView btnEdit = (ImageView) linha.findViewById(R.id.imgEdit);
	        btnEdit.setTag(despesa);

	        ImageView btnTrash = (ImageView) linha.findViewById(R.id.imgTrash);
	        btnTrash.setTag(despesa);

	        TextView txtDsp = (TextView) linha.findViewById(R.id.txtRowDespesa);
			Util.setTextUnderline(txtDsp, despesa.getNome());
			txtDsp.setTag(despesa);
			txtDsp.setOnClickListener(clickNome);

			TextView txtQuant = (TextView) linha.findViewById(R.id.txtRowQuant);
			txtQuant.setText(getQuantidades(despesa));

			tabela.addView(linha);
		}
	}
	
	private String getQuantidades(Despesa despesa) {
		StringBuilder sb = new StringBuilder();

		List<Long> idsPagamento = geDespesaDB().getListaPagamentos(
				despesa.getId(), filtro);
		// Esta contagem só funcionará se a lista de Pagamentos vier ordenada
		// por Data
		for (int i = 0; i < despesa.getIdsPagamento().size(); i++) {
			Long id = despesa.getIdsPagamento().get(i);

			for (Long idPgto : idsPagamento) {
				if (id.equals(idPgto)) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(i + 1);
				}
			}
		}

		sb.append("/");
		sb.append(despesa.getIdsPagamento().size());

		return sb.toString();
	}

	public void alterarDespesa(View view) {
		Despesa despesa = (Despesa) view.getTag();
		Intent it = new Intent(contexto, AlterarDespesa.class);
		it.putExtra(AlterarDespesa.PARAM_CONTA, conta);
		it.putExtra(AlterarDespesa.PARAM_MES_REF, filtro);
		it.putExtra(AlterarDespesa.PARAM_DESPESA, despesa);

		startActivityForResult(it, OPR_ALTERAR_DESPESA);
	}

	public void novaDespesa(View view) {
		Intent it = new Intent(contexto, NovaDespesa.class);
		it.putExtra(NovaDespesa.PARAM_CONTA, conta);

		startActivityForResult(it, OPR_NOVA_DESPESA);
	}

	public void excluirDespesa(View view) {
		Despesa despesa = (Despesa) view.getTag();
		confirmarExclusao(despesa);
	}

	private void confirmarExclusao(Despesa despesa) {
		OnClickExcuir onclick = new OnClickExcuir(despesa);

		String msg = getString(R.string.msg_confirmar_exclusao, " a despesa "
				+ despesa);

		AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle(R.string.title_confirmar).setMessage(msg)
				.setPositiveButton("Sim", onclick)
				.setNegativeButton("Não", null).create();
		alert.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case OPR_NOVA_DESPESA:
		case OPR_ALTERAR_DESPESA:
			if (resultCode == RESULT_FIRST_USER) {
				carregarDespesas();
			}
			break;

		default:
			// Não precisa realizar operação
			break;
		}
	}

}
