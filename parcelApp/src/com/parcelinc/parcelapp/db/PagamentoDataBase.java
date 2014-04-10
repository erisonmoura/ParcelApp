package com.parcelinc.parcelapp.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parcelinc.parcelapp.pojo.Pagamento;

public class PagamentoDataBase implements DataBase<Pagamento> {

	private static final PagamentoDataBase instance = new PagamentoDataBase();

	private SQLiteDatabase db;

	public static final String[] COLUMNS = new String[] {
			DBHelper.DATABASE_ID_FIELD, DBHelper.DATABASE_DATE_FIELD,
			DBHelper.DATABASE_VALUE_FIELD, DBHelper.DATABASE_ID_USUARIO,
			DBHelper.DATABASE_ID_DESPESA };

	private PagamentoDataBase() {
		// Do nothing
	}

	/**
	 * Get PagamentoDataBase instance
	 * 
	 * @param ctx
	 *            Context
	 * @return The PagamentoDataBase instance.
	 */
	public static PagamentoDataBase getInstance(Context ctx) {
		if (instance.db == null || !instance.db.isOpen()) {
			instance.db = new DBHelper(ctx).getWritableDatabase();
		}
		return instance;
	}

	@Override
	public long insert(Pagamento pagamento) {
		long retValue = -1;

		ContentValues values = new ContentValues();
		values.put(DBHelper.DATABASE_DATE_FIELD, pagamento.getData());
		values.put(DBHelper.DATABASE_VALUE_FIELD, pagamento.getValor());
		values.put(DBHelper.DATABASE_ID_USUARIO, pagamento.getIdUsuario());
		values.put(DBHelper.DATABASE_ID_DESPESA, pagamento.getIdDespesa());

		try {
			db.beginTransaction();
			retValue = db.insert(DBHelper.TBL_PAGAMENTOS, null, values);
			if (retValue != -1) {
				pagamento.setId(Long.valueOf(retValue));
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}
		return retValue;
	}

	@Override
	public long update(Pagamento pagamento) {
		long retValue = -1;

		ContentValues values = new ContentValues();
		values.put(DBHelper.DATABASE_DATE_FIELD, pagamento.getData());
		values.put(DBHelper.DATABASE_VALUE_FIELD, pagamento.getValor());
		values.put(DBHelper.DATABASE_ID_USUARIO, pagamento.getIdUsuario());
		values.put(DBHelper.DATABASE_ID_DESPESA, pagamento.getIdDespesa());

		try {
			db.beginTransaction();
			retValue = db.update(DBHelper.TBL_CONTAS, values,
					DBHelper.DATABASE_ID_FIELD + "=?",
					new String[] { String.valueOf(pagamento.getId()) });
			if (retValue != -1) {
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}

		return retValue;
	}

	@Override
	public void remove(Pagamento pagamento) {
		db.beginTransaction();
		try {
			db.delete(DBHelper.TBL_PAGAMENTOS, DBHelper.DATABASE_ID_FIELD
					+ "=?", new String[] { String.valueOf(pagamento.getId()) });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	@Override
	public Pagamento get(long id) {

		Cursor c = db.query(DBHelper.TBL_PAGAMENTOS, PagamentoDataBase.COLUMNS,
				DBHelper.DATABASE_ID_FIELD + "=?",
				new String[] { String.format("%d", id) }, null, null,
				DBHelper.DATABASE_ID_FIELD);

		c.moveToFirst();
		Pagamento pagamento = null;
		if (!c.isAfterLast()) {
			pagamento = fillPagamento(c);

		}

		return pagamento;
	}

	public List<Pagamento> getList() {
		List<Pagamento> list = new ArrayList<Pagamento>();

		Cursor c = db.query(DBHelper.TBL_PAGAMENTOS, PagamentoDataBase.COLUMNS,
				null, null, null, null, DBHelper.DATABASE_NAME_FIELD);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			Pagamento pagamento = fillPagamento(c);
			list.add(pagamento);
			c.moveToNext();
		}
		return list;
	}	

	private Pagamento fillPagamento(Cursor c) {
		Long id = c.getLong(0);
		Long idDespesa = c.getLong(1);
		String data = c.getString(2);
		Long idUsuario = c.getLong(3);
		double valor = c.getDouble(4);

		return new Pagamento(id, idDespesa, data, idUsuario, valor);
	}

}