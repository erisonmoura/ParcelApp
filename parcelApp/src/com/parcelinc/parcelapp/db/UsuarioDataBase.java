package com.parcelinc.parcelapp.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parcelinc.parcelapp.pojo.Usuario;

public class UsuarioDataBase implements DataBase<Usuario> {

	private static final UsuarioDataBase instance = new UsuarioDataBase();

	private SQLiteDatabase db;

	public static final String[] COLUMNS = new String[] { DBHelper.DATABASE_ID_FIELD,
			DBHelper.DATABASE_NAME_FIELD, DBHelper.DATABASE_OBS_FIELD };

	private UsuarioDataBase() {
		// Do nothing
	}

	/**
	 * Get UserDataBase instance
	 * 
	 * @param ctx
	 *            Context
	 * @return The UserDataBase instance.
	 */
	public static UsuarioDataBase getInstance(Context ctx) {
		if (instance.db == null || !instance.db.isOpen()) {
			instance.db = new DBHelper(ctx).getWritableDatabase();
		}
		return instance;
	}

	@Override
	public long insert(Usuario usuario) {
		long retValue = -1;

		ContentValues cv = new ContentValues();
		cv.put(DBHelper.DATABASE_NAME_FIELD, usuario.getNome());

		cv.put(DBHelper.DATABASE_OBS_FIELD, usuario.getObs());

		try {
			db.beginTransaction();
			retValue = db.insert(DBHelper.TBL_USUARIOS, null, cv);
			if (retValue != -1) {
				usuario.setId(Long.valueOf(retValue));
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}
		return retValue;
	}

	@Override
	public long update(Usuario usuario) {
		long retValue = -1;

		ContentValues values = new ContentValues();
		values.put(DBHelper.DATABASE_ID_FIELD, usuario.getId());

		try {
			db.beginTransaction();
			retValue = db.update(DBHelper.TBL_USUARIOS, values,
					DBHelper.DATABASE_ID_FIELD + "=?",
					new String[] { String.valueOf(usuario.getId()) });
			if (retValue != -1) {
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}

		return retValue;

	}

	@Override
	public void remove(Usuario usuario) {
		db.beginTransaction();
		try {
			db.delete(DBHelper.TBL_USUARIOS, DBHelper.DATABASE_ID_FIELD + "=?",
					new String[] { String.valueOf(usuario.getId()) });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}
	
	@Override
	public Usuario get(long id) {
		Usuario usuario = null;

		Cursor c = db.query(DBHelper.TBL_USUARIOS, COLUMNS,
				DBHelper.DATABASE_ID_FIELD + "=?",
				new String[] { String.format("%d", id) }, null, null, null);

		c.moveToFirst();
		if (!c.isAfterLast()) {
			usuario = fillUsuario(c);
		}

		return usuario;
	}

	public List<Usuario> getList() {
		List<Usuario> list = new ArrayList<Usuario>();

		Cursor c = db.query(DBHelper.TBL_USUARIOS, COLUMNS, null, null, null,
				null, COLUMNS[1]);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			Usuario usuario = fillUsuario(c);
			list.add(usuario);
			c.moveToNext();
		}

		return list;
	}

	private Usuario fillUsuario(Cursor c) {
		Long id = c.getLong(0);
		String nome = c.getString(1);
		String obs = c.getString(2);

		return new Usuario(id, nome, obs);
	}

	
}
