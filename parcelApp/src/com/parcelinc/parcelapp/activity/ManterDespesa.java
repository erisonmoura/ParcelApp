package com.parcelinc.parcelapp.activity;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parcelinc.parcelapp.R;
import com.parcelinc.parcelapp.db.DateUtil;
import com.parcelinc.parcelapp.db.PagamentoDataBase;
import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Despesa;
import com.parcelinc.parcelapp.pojo.Pagamento;
import com.parcelinc.parcelapp.pojo.Usuario;
import com.parcelinc.parcelapp.util.OnClickDate;
import com.parcelinc.parcelapp.util.Util;

public class ManterDespesa extends Activity {

	public static final String PARAM_CONTA = "conta";
	public static final String PARAM_DESPESA = "despesa";
	public static final String PARAM_PAGAMENTO = "pagamento";

	private Context contexto;
	
	private Spinner spnUser;
	private EditText edtValor;
	
	private OnClickDate clickDate;

	private Conta conta;
	private Despesa despesa;
	private Pagamento pagamento;
	
	private PagamentoDataBase getPagamentoDB() {
		return PagamentoDataBase.getInstance(contexto);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.despesa_manter);
		
		contexto = this;

		Intent it = getIntent();
		conta = (Conta) it.getSerializableExtra(PARAM_CONTA);
		if (it.hasExtra(PARAM_DESPESA)) {
			despesa = (Despesa) it.getSerializableExtra(PARAM_DESPESA);
		} else if (it.hasExtra(PARAM_PAGAMENTO)) {
			pagamento = (Pagamento) it.getSerializableExtra(PARAM_PAGAMENTO);
		}
		
		spnUser = (Spinner) findViewById(R.id.spnUser);

		List<Usuario> usuariosValidos = Util.consultarUsuariosValidos(conta, contexto);
		
		ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(this,
		          android.R.layout.simple_spinner_item, usuariosValidos);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnUser.setAdapter(adapter);

		edtValor = (EditText) findViewById(R.id.edtValor);

		EditText edtNome = (EditText) findViewById(R.id.edtNome);

		EditText edtData = (EditText) findViewById(R.id.edtData);
		clickDate = new OnClickDate(contexto, edtData);

		if (despesa != null) {
			edtNome.setText(despesa.getNome());
		} else if (pagamento != null) {
			edtNome.setText(pagamento.getDespesa().getNome());
			spnUser.setSelection(usuariosValidos.indexOf(pagamento.getUsuario()));
			edtValor.setText(Util.doubleToString(pagamento.getValor()));
			
			Calendar cal = DateUtil.toCalendar(pagamento.getData());
			clickDate.setCalendar(cal);
		}

	}

	public void salvarPagamento(View view) {
		String valorStr = edtValor.getText().toString();
		double valor = 0.0d;
		try {
			valor = Util.stringToDouble(valorStr);
		} catch (Exception e) {
			edtValor.requestFocus();
			Toast.makeText(contexto, R.string.msg_valid_value, Toast.LENGTH_LONG).show();
			return;
		}
		
		Usuario usuario = (Usuario) spnUser.getSelectedItem();
		String data = DateUtil.getDate(clickDate.getCalendar()); 

		long result = -1;
		if (pagamento == null) {
			pagamento = new Pagamento(null, despesa, data, usuario, valor);
			
			result = getPagamentoDB().insert(pagamento);
		} else {
			pagamento.setValor(valor);
			pagamento.setData(data);
			pagamento.setUsuario(usuario);
			
			result = getPagamentoDB().update(pagamento);
		}
		
		if (result != -1) {
			setResult(RESULT_FIRST_USER);
			finish();
		}
	}

}
