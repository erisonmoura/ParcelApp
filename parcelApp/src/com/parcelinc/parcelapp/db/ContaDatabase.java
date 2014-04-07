package com.parcelinc.parcelapp.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parcelinc.parcelapp.pojo.Conta;
import com.parcelinc.parcelapp.pojo.Usuario;

public class ContaDatabase implements DataBase<Conta> {

	private static final ContaDatabase instance = new ContaDatabase();

	private SQLiteDatabase db;

	public ContaDatabase() {
		// Do nothing
	}

	/**
	 * Get ContaDataBase instance
	 * 
	 * @param ctx
	 *            Context
	 * @return The ContaDataBase instance.
	 */
	public static DataBase<Conta> getInstance(Context ctx) {
		if (instance.db == null || !instance.db.isOpen()) {
			instance.db = new DBHelper(ctx).getWritableDatabase();
		}
		return instance;
	}

	@Override
	public long insert(Conta conta) {
		long retValue = -1;

		ContentValues cv = new ContentValues();
		cv.put(DBHelper.DATABASE_NAME_FIELD, conta.getNome());

		try {
			db.beginTransaction();
			retValue = db.insert(DBHelper.TBL_CONTAS, null, cv);
			if (retValue != -1) {
				conta.setId(Long.valueOf(retValue));
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}

		try {
			db.beginTransaction();
			for (int i = 0; i < conta.getUsuarios().size(); i++) {
				cv.clear();
				cv.put(DBHelper.DATABASE_ID_USUARIO, conta.getUsuarios().get(i)
						.getId());
				cv.put(DBHelper.DATABASE_ID_CONTA, retValue);

				db.insert(DBHelper.TBL_CONTAS_USUARIOS, null, cv);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return retValue;
	}

	@Override
	public long update(Conta conta) {
		long retValue = -1;

		ContentValues values = new ContentValues();
		values.put(DBHelper.DATABASE_ID_FIELD, conta.getNome());

		try {
			db.beginTransaction();
			retValue = db.update(DBHelper.TBL_CONTAS, values,
					DBHelper.DATABASE_ID_FIELD + "=?",
					new String[] { String.valueOf(conta.getId()) });
			if (retValue != -1) {
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}

		return retValue;
	}

	@Override
	public void remove(Conta conta) {
		db.beginTransaction();
		try {
			db.delete(DBHelper.TBL_CONTAS, DBHelper.DATABASE_ID_FIELD + "=?",
					new String[] { String.valueOf(conta.getId()) });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}
	// FIXME tratando apenas NOME e ID da conta. Resolver Obj usuario
	public List<Conta> getList() {
		List<Conta> list = new ArrayList<Conta>();
		String[] columns = new String[] { DBHelper.DATABASE_ID_FIELD,
				DBHelper.DATABASE_NAME_FIELD };

		Cursor c = db.query(DBHelper.TBL_CONTAS, columns, null, null, null,
				null, DBHelper.DATABASE_DATE_FIELD);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			Conta conta = fillConta(c);
			list.add(conta);
			c.moveToNext();
		}
		return list;
	}

	
	public String[] getArrayList() {
		List<Conta> conta = getList();
		String[] result = new String[conta.size()];

		for (int i = 0; i < result.length; i++) {
			result[i] = conta.get(i).getNome();
		}
		return result;
	}

	private Conta fillConta(Cursor c) {
		Long id = c.getLong(c.getColumnIndex(DBHelper.DATABASE_ID_FIELD));
		String nome = c.getString(c
				.getColumnIndex(DBHelper.DATABASE_NAME_FIELD));

		return new Conta(id, nome, null);
	}

}
