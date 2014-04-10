package com.parcelinc.parcelapp.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parcelinc.parcelapp.pojo.Despesa;
import com.parcelinc.parcelapp.pojo.Pagamento;

public class DespesaDataBase implements DataBase<Despesa> {

	private static final DespesaDataBase instance = new DespesaDataBase();

	private SQLiteDatabase db;

	private DespesaDataBase() {
		// Do nothing
	}

	/**
	 * Get ContaDataBase instance
	 * 
	 * @param ctx
	 *            Context
	 * @return The ContaDataBase instance.
	 */
	public static DespesaDataBase getInstance(Context ctx) {
		if (instance.db == null || !instance.db.isOpen()) {
			instance.db = new DBHelper(ctx).getWritableDatabase();
		}
		return instance;
	}

	@Override
	public long insert(Despesa despesa) {
		long retValue = -1;

		ContentValues values = new ContentValues();
		values.put(DBHelper.DATABASE_NAME_FIELD, despesa.getNome());
		values.put(DBHelper.DATABASE_ID_CONTA, despesa.getConta().getId());
		

		try {
			db.beginTransaction();
			retValue = db.insert(DBHelper.TBL_DESPESAS, null, values);
			if (retValue != -1) {
				despesa.setId(Long.valueOf(retValue));
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}

		return retValue;
	}
	
	public long insert(Despesa despesa, int qntd, double valor, long idUsuario, Date dtPagamento) {
		long idDespesa = insert(despesa);

		Calendar cal = Calendar.getInstance();
		cal.setTime(dtPagamento);

		ContentValues values = new ContentValues();
		try {
			db.beginTransaction();
			for (int i = 0; i < qntd; i++) {
				values.clear();

				values.put(DBHelper.DATABASE_ID_DESPESA, idDespesa);
				values.put(DBHelper.DATABASE_ID_USUARIO, idUsuario);
				values.put(DBHelper.DATABASE_VALUE_FIELD, valor);
				values.put(DBHelper.DATABASE_DATE_FIELD, DateUtil.getDate(cal));

				cal.add(Calendar.MONTH, 1);
				
				db.insert(DBHelper.TBL_PAGAMENTOS, null, values);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
		return idDespesa;
	}
	
	@Override
	public long update(Despesa despesa) {
		long retValue = -1;

		ContentValues values = new ContentValues();
		values.put(DBHelper.DATABASE_NAME_FIELD, despesa.getNome());

		try {
			db.beginTransaction();
			retValue = db.update(DBHelper.TBL_CONTAS, values,
					DBHelper.DATABASE_ID_FIELD + "=?",
					new String[] { String.valueOf(despesa.getId()) });
			if (retValue != -1) {
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}

		return retValue;
	}

	@Override
	public void remove(Despesa despesa) {
		db.beginTransaction();
		try {
			db.delete(DBHelper.TBL_CONTAS, DBHelper.DATABASE_ID_FIELD + "=?",
					new String[] { String.valueOf(despesa.getId()) });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	@Override
	public Despesa get(long id) {
		Despesa despesa = null;
		String[] columns = new String[] { DBHelper.DATABASE_ID_FIELD,
				DBHelper.DATABASE_NAME_FIELD };

		Cursor c = db.query(DBHelper.TBL_DESPESAS, columns,
				DBHelper.DATABASE_ID_FIELD + "=?",
				new String[] { String.format("%d", id) }, null, null,
				DBHelper.DATABASE_ID_FIELD);

		c.moveToFirst();
		if (!c.isAfterLast()) {
			despesa = fillDespesa(c);
			
			Map<Long, List<Long>> mapa = listarRelacao(despesa.getId());
			for (Long idPagamento : mapa.get(despesa.getId())) {
				despesa.addPagamento(new Pagamento(idPagamento));
			}
		}

		return despesa;
	}

	public List<Despesa> getList() {
		List<Despesa> list = new ArrayList<Despesa>();
		String[] cols = new String[] { DBHelper.DATABASE_ID_FIELD,
				DBHelper.DATABASE_NAME_FIELD };
		Cursor cr = db.query(DBHelper.TBL_DESPESAS, cols, null, null, null,
				null, DBHelper.DATABASE_NAME_FIELD);

		Map<Long, List<Long>> mapa = listarRelacao(null);
		
		cr.moveToFirst();
		while (!cr.isAfterLast()) {
			Despesa despesa = fillDespesa(cr);
			for (Long idPagamento : mapa.get(despesa.getId())) {
				despesa.addPagamento(new Pagamento(idPagamento));
			}
			
			list.add(despesa);
			cr.moveToNext();
		}
		return list;
	}
	
	private Map<Long, List<Long>> listarRelacao(Long idDespesa) {
		Map<Long, List<Long>> mapa = new HashMap<Long, List<Long>>();
		
		String[] cols = new String[] { DBHelper.DATABASE_ID_DESPESA, DBHelper.DATABASE_ID_FIELD };
		String where = null;
		String[] params = null;
		if (idDespesa == null) {
			where = DBHelper.DATABASE_ID_DESPESA + "=?";
			params = new String[] { String.format("%d", idDespesa) };
		}

		Cursor cr = db.query(DBHelper.TBL_PAGAMENTOS, cols, where, params, null,
				null, DBHelper.DATABASE_DATE_FIELD);

		cr.moveToFirst();
		while (!cr.isAfterLast()) {
			Long idDespesaTmp = cr.getLong(0);
			Long idPagamentoTmp = cr.getLong(1);
			
			List<Long> lista = mapa.get(idDespesaTmp);
			if (lista == null) {
				lista = new ArrayList<Long>();
			}

			if (!lista.contains(idPagamentoTmp)) {
				lista.add(idPagamentoTmp);
			}
			
			mapa.put(idDespesaTmp, lista);

			cr.moveToNext();
		}
		
		return mapa;
	}

	public List<Despesa> getFilter(Long idConta, String filtroMes) {
		filtroMes = filtroMes + DateUtil.SEPARATOR;

		
		// TODO filtrar despesa 
		return null;
	}
	
	private Despesa fillDespesa(Cursor c) {
		Long id = c.getLong(c.getColumnIndex(DBHelper.DATABASE_ID_FIELD));
		String nome = c.getString(c
				.getColumnIndex(DBHelper.DATABASE_NAME_FIELD));

		return new Despesa(id, nome, null, null);
	}

}