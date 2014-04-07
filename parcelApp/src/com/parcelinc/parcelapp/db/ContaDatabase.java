package com.parcelinc.parcelapp.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	@Override
	public Conta get(long id) {
		Conta conta = null;
		String[] columns = new String[] { DBHelper.DATABASE_ID_FIELD,
				DBHelper.DATABASE_NAME_FIELD };

		Cursor c = db.query(DBHelper.TBL_CONTAS, columns,
				DBHelper.DATABASE_ID_FIELD + "=?",
				new String[] { String.format("%d", id) }, null, null,
				DBHelper.DATABASE_DATE_FIELD);

		c.moveToFirst();
		if (!c.isAfterLast()) {
			conta = fillConta(c);
			
			Map<Long, Set<Long>> mapa = listarRelacao(conta.getId());
			for (Long idUsuario : mapa.get(conta.getId())) {
				conta.addUsuario(new Usuario(idUsuario));
			}
		}

		return conta;
	}

	public List<Conta> getList() {
		List<Conta> list = new ArrayList<Conta>();
		String[] cols = new String[] { DBHelper.DATABASE_ID_FIELD,
				DBHelper.DATABASE_NAME_FIELD };
		Cursor cr = db.query(DBHelper.TBL_CONTAS, cols, null, null, null,
				null, DBHelper.DATABASE_NAME_FIELD);

		Map<Long, Set<Long>> mapa = listarRelacao(null);
		
		cr.moveToFirst();
		while (!cr.isAfterLast()) {
			Conta conta = fillConta(cr);
			for (Long idUsuario : mapa.get(conta.getId())) {
				conta.addUsuario(new Usuario(idUsuario));
			}
			
			list.add(conta);
			cr.moveToNext();
		}
		return list;
	}
	
	private Map<Long, Set<Long>> listarRelacao(Long idConta) {
		Map<Long, Set<Long>> mapa = new HashMap<Long, Set<Long>>();
		
		String[] cols = new String[] { DBHelper.DATABASE_ID_CONTA, DBHelper.DATABASE_ID_USUARIO };
		String where = null;
		String[] params = null;
		if (idConta == null) {
			where = DBHelper.DATABASE_ID_CONTA + "=?";
			params = new String[] { String.format("%d", idConta) };
		}

		Cursor cr = db.query(DBHelper.TBL_CONTAS_USUARIOS, cols, where, params, null,
				null, DBHelper.DATABASE_ID_CONTA);

		cr.moveToFirst();
		while (!cr.isAfterLast()) {
			Set<Long> conjunto = mapa.get(cr.getLong(0));
			if (conjunto == null) {
				conjunto = new HashSet<Long>();
			}
			conjunto.add(cr.getLong(1));

			cr.moveToNext();
		}
		
		return mapa;
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
