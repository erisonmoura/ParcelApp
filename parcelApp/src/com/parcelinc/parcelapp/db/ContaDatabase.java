package com.parcelinc.parcelapp.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parcelinc.parcelapp.pojo.Conta;

public class ContaDatabase implements DataBase<Conta> {

	private static final ContaDatabase instance = new ContaDatabase();

	private SQLiteDatabase db;

	public static final String[] COLUMNS = new String[] {
			DBHelper.DATABASE_ID_FIELD, DBHelper.DATABASE_NAME_FIELD };

	private ContaDatabase() {
		// Do nothing
	}

	/**
	 * Get ContaDataBase instance
	 * 
	 * @param ctx
	 *            Context
	 * @return The ContaDataBase instance.
	 */
	public static ContaDatabase getInstance(Context ctx) {
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
			for (int i = 0; i < conta.getIdsUsuario().size(); i++) {
				cv.clear();
				cv.put(DBHelper.DATABASE_ID_USUARIO, conta.getIdsUsuario().get(i));
				cv.put(DBHelper.DATABASE_ID_CONTA, retValue);

				db.insert(DBHelper.TBL_CONTAS_USUARIOS, null, cv);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return retValue;
	}

	// TODO implementar update do relacionamento contas-usuarios
	@Override
	public long update(Conta conta) {
		long retValue = -1;

		ContentValues values = new ContentValues();
		values.put(DBHelper.DATABASE_ID_FIELD, conta.getId());

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

	@Override
	public Conta get(long id) {
		Conta conta = null;
		Cursor c = db.query(DBHelper.TBL_CONTAS, COLUMNS,
				DBHelper.DATABASE_ID_FIELD + "=?",
				new String[] { String.format("%d", id) }, null, null, null);

		c.moveToFirst();
		if (!c.isAfterLast()) {
			conta = fillConta(c);

			Map<Long, List<Long>> mapa = listarRelacao(conta.getId());
			for (Long idUsuario : mapa.get(conta.getId())) {
				conta.addIdUsuario(idUsuario);
			}
		}

		return conta;
	}

	public List<Conta> getList() {
		List<Conta> list = new ArrayList<Conta>();
		Cursor cr = db.query(DBHelper.TBL_CONTAS, COLUMNS, null, null, null,
				null, DBHelper.DATABASE_NAME_FIELD);

		Map<Long, List<Long>> mapa = listarRelacao(null);

		cr.moveToFirst();
		while (!cr.isAfterLast()) {
			Conta conta = fillConta(cr);
			for (Long idUsuario : mapa.get(conta.getId())) {
				conta.addIdUsuario(idUsuario);
			}

			list.add(conta);
			cr.moveToNext();
		}
		return list;
	}

	private Map<Long, List<Long>> listarRelacao(Long idConta) {
		Map<Long, List<Long>> mapa = new HashMap<Long, List<Long>>();

		String[] cols = new String[] { DBHelper.DATABASE_ID_CONTA,
				DBHelper.DATABASE_ID_USUARIO };
		String where = null;
		String[] params = null;
		if (idConta != null) {
			where = DBHelper.DATABASE_ID_CONTA + "=?";
			params = new String[] { String.format("%d", idConta) };
		}

		Cursor cr = db.query(DBHelper.TBL_CONTAS_USUARIOS, cols, where, params,
				null, null, DBHelper.DATABASE_ID_CONTA);

		cr.moveToFirst();
		while (!cr.isAfterLast()) {
			Long idContaTmp = cr.getLong(0);
			Long idUserTmp = cr.getLong(1);
			
			List<Long> lista = mapa.get(idContaTmp);
			if (lista == null) {
				lista = new ArrayList<Long>();
			}

			if (!lista.contains(idUserTmp)) {
				lista.add(idUserTmp);
			}
			
			mapa.put(idContaTmp, lista);

			cr.moveToNext();
		}

		return mapa;
	}

	private Conta fillConta(Cursor c) {
		Long id = c.getLong(0);
		String nome = c.getString(1);

		return new Conta(id, nome, null);
	}

}
