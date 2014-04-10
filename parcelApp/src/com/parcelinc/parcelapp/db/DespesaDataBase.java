package com.parcelinc.parcelapp.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parcelinc.parcelapp.pojo.Despesa;
import com.parcelinc.parcelapp.pojo.Pagamento;

public class DespesaDataBase implements DataBase<Despesa> {

	private static final DespesaDataBase instance = new DespesaDataBase();

	private SQLiteDatabase db;

	public DespesaDataBase() {
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
			
			Map<Long, Set<Long>> mapa = listarRelacao(despesa.getId());
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

		Map<Long, Set<Long>> mapa = listarRelacao(null);
		
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
	
	private Map<Long, Set<Long>> listarRelacao(Long idDespesa) {
		Map<Long, Set<Long>> mapa = new HashMap<Long, Set<Long>>();
		
		String[] cols = new String[] { DBHelper.DATABASE_ID_DESPESA };
		String where = null;
		String[] params = null;
		if (idDespesa == null) {
			where = DBHelper.DATABASE_ID_DESPESA + "=?";
			params = new String[] { String.format("%d", idDespesa) };
		}

		Cursor cr = db.query(DBHelper.TBL_PAGAMENTOS, cols, where, params, null,
				null, DBHelper.DATABASE_ID_DESPESA);

		cr.moveToFirst();
		while (!cr.isAfterLast()) {
			Set<Long> conjunto = mapa.get(cr.getLong(0));
			if (conjunto == null) {
				conjunto = new HashSet<Long>();
			}
			conjunto.add(cr.getLong(1));
			
			mapa.put(cr.getLong(0), conjunto);

			cr.moveToNext();
		}
		
		return mapa;
	}

	
	public String[] getArrayList() {
		List<Despesa> despesa = getList();
		String[] result = new String[despesa.size()];

		for (int i = 0; i < result.length; i++) {
			result[i] = despesa.get(i).getNome();
		}
		return result;
	}

	private Despesa fillDespesa(Cursor c) {
		Long id = c.getLong(c.getColumnIndex(DBHelper.DATABASE_ID_FIELD));
		String nome = c.getString(c
				.getColumnIndex(DBHelper.DATABASE_NAME_FIELD));

		return new Despesa(id, nome, null, null);
	}

}
