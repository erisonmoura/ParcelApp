//package com.parcelinc.parcelapp.db;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.parcelinc.parcelapp.pojo.Conta;
//import com.parcelinc.parcelapp.pojo.Usuario;
//
//public class ContaDatabase implements DataBase<Conta> {
//
//	private static final ContaDatabase instance = new ContaDatabase();
//
//	private SQLiteDatabase db;
//
//	public ContaDatabase() {
//		// Do nothing
//	}
//
//	/**
//	 * Get UserDataBase instance
//	 * 
//	 * @param ctx
//	 *            Context
//	 * @return The UserDataBase instance.
//	 */
//	public static DataBase<Conta> getInstance(Context ctx) {
//		if (instance.db == null || !instance.db.isOpen()) {
//			instance.db = new DBHelper(ctx).getWritableDatabase();
//		}
//		return instance;
//	}
//
//	@Override
//	public long insert(Conta conta) {
//		long retValue = -1;
//
//		ContentValues cv = new ContentValues();
//		cv.put(DBHelper.DATABASE_NAME_FIELD, conta.getNome());
//
//		try {
//			db.beginTransaction();
//			retValue = db.insert(DBHelper.TBL_CONTAS, null, cv);
//			if (retValue != -1) {
//				db.setTransactionSuccessful();
//			} else {
//				conta.setId(Long.valueOf(retValue));
//			}
//		} finally {
//			db.endTransaction();
//		}
//
//		try {
//			db.beginTransaction();
//			for (int i = 0; i < conta.getUsuarios().size(); i++) {
//				cv.clear();
//				cv.put(DBHelper.DATABASE_ID_USUARIO, conta.getUsuarios().get(i).getId());
//				cv.put(DBHelper.DATABASE_ID_CONTA, retValue);
//
//				db.insert(DBHelper.TBL_CONTAS_USUARIOS, null, cv);
//			}
//			db.setTransactionSuccessful();
//		} finally {
//			db.endTransaction();
//		}
//
//		return retValue;
//	}
//
//	@Override
//	public void remove(String nome) {
//		db.beginTransaction();
//		try {
//			db.delete(DBHelper.TBL_USUARIOS, DBHelper.DATABASE_NAME_FIELD
//					+ "=?", new String[] { nome });
//			db.setTransactionSuccessful();
//		} finally {
//			db.endTransaction();
//		}
//
//	}
//
//	public List<Conta> getList() {
//		List<Conta> list = new ArrayList<Conta>();
//		String[] columns = new String[] { DBHelper.DATABASE_ID_FIELD,
//				DBHelper.DATABASE_NAME_FIELD };
//
//		Cursor c = db.query(DBHelper.TBL_CONTAS, columns, null, null, null,
//				null, DBHelper.DATABASE_DATE_FIELD);
//
//		c.moveToFirst();
//		while (!c.isAfterLast()) {
//			Usuario usuario = fillConta(c);
//			list.add(usuario);
//			c.moveToNext();
//		}
//
//		return list;
//	}
////FIXME continuar alteracoes
//	public String[] getArrayList() {
//		List<Usuario> usuarios = getList();
//		String[] result = new String[usuarios.size()];
//
//		for (int i = 0; i < result.length; i++) {
//			result[i] = usuarios.get(i).getNome();
//		}
//		return result;
//	}
//
//	private Conta fillConta(Cursor c) {
//		Long id = c.getLong(c.getColumnIndex(DBHelper.DATABASE_ID_FIELD));
//		String nome = c.getString(c
//				.getColumnIndex(DBHelper.DATABASE_NAME_FIELD));
//		String obs = c.getString(c.getColumnIndex(DBHelper.DATABASE_OBS_FIELD));
//
//		return new Conta(id, nome, obs);
//	}
//
//	@Override
//	public List<Usuario> getList(String data1, String data2) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
