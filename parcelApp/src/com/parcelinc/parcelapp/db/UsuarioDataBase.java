package com.parcelinc.parcelapp.db;



import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parcelinc.parcelapp.pojo.Usuario;

public class UsuarioDataBase implements DataBase<Usuario>{
	
	private static final UsuarioDataBase instance = new UsuarioDataBase();
	
	private SQLiteDatabase db;
	
	public UsuarioDataBase() {
		// Do nothing 
	}
	
	/**
	 * Get UserDataBase instance
	 * 
	 * @param ctx Context
	 * @return The UserDataBase instance.
	 */
	public static DataBase<Usuario> getInstance(Context ctx) {
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
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}
		return retValue;
	}

	@Override
	public void remove(String nome) {
		db.beginTransaction();
		try {
			db.delete(DBHelper.TBL_USUARIOS, DBHelper.DATABASE_NAME_FIELD + "=?",
					new String[] { nome });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		
	}
	
	public List<Usuario> getList() {
		List<Usuario> list = new ArrayList<Usuario>();
		String[] columns = new String[] { DBHelper.DATABASE_ID_FIELD,
				DBHelper.DATABASE_NAME_FIELD};
		

		Cursor c = db.query(DBHelper.TBL_USUARIOS, columns, null, null, null, null, DBHelper.DATABASE_DATE_FIELD);
        
        c.moveToFirst();
        while (!c.isAfterLast()) {
			Usuario usuario = fillUsuario(c);
			list.add(usuario);
			c.moveToNext(); 
		}	
		
		return list;
	}
	
	
	
	public String[] getArrayList() {
		List<Usuario> usuarios = getList();
		String[] result = new String[usuarios.size()];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = usuarios.get(i).getNome();
		}
		return result;
	}

	private Usuario fillUsuario(Cursor c) {
		Long id = c.getLong(c.getColumnIndex(DBHelper.DATABASE_ID_FIELD));
		String nome = c.getString(c.getColumnIndex(DBHelper.DATABASE_NAME_FIELD));
		String obs = c.getString(c.getColumnIndex(DBHelper.DATABASE_OBS_FIELD));
				
		return new Usuario(id, nome, obs);
	}

	@Override
	public List<Usuario> getList(String data1, String data2) {
		// TODO Auto-generated method stub
		return null;
	}
}
