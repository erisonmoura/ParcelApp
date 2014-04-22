package com.parcelinc.parcelapp.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parcelinc.parcelapp.pojo.Despesa;
import com.parcelinc.parcelapp.pojo.Pagamento;
import com.parcelinc.parcelapp.pojo.Usuario;
import com.parcelinc.parcelapp.util.Util;

public class PagamentoDataBase implements DataBase<Pagamento> {

	private static final PagamentoDataBase instance = new PagamentoDataBase();

	private static final long ZERO = 0L;

	private Context ctx;
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
		instance.ctx = ctx; 
		if (instance.db == null || !instance.db.isOpen()) {
			instance.db = new DBHelper(ctx).getWritableDatabase();
		}
		return instance;
	}
	
	private DataBase<Despesa> getDespesaDB(Context ctx) {
		return DespesaDataBase.getInstance(ctx);
	}
	
	private DataBase<Usuario> getUsuarioDB(Context ctx) {
		return UsuarioDataBase.getInstance(ctx);
	}

	@Override
	public long insert(Pagamento pagamento) {
		long retValue = -1;

		ContentValues values = new ContentValues();
		values.put(DBHelper.DATABASE_DATE_FIELD, pagamento.getData());
		values.put(DBHelper.DATABASE_VALUE_FIELD, pagamento.getValor());
		values.put(DBHelper.DATABASE_ID_USUARIO, pagamento.getUsuario().getId());
		values.put(DBHelper.DATABASE_ID_DESPESA, pagamento.getDespesa().getId());

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
		values.put(DBHelper.DATABASE_ID_USUARIO, pagamento.getUsuario().getId());

		try {
			db.beginTransaction();
			retValue = db.update(DBHelper.TBL_PAGAMENTOS, values,
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

	public List<Pagamento> getList(Long idConta, String filtroMes,
			Long idUsuario, Long idDespesa) {
		List<Pagamento> list = new ArrayList<Pagamento>();

		String pagamentoTbl = DBHelper.TBL_PAGAMENTOS;
		String despesaTbl = DBHelper.TBL_DESPESAS;
		String usuarioTbl = DBHelper.TBL_USUARIOS;

		List<String> params = new ArrayList<String>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ").append(pagamentoTbl).append(".").append(COLUMNS[0]);
		sql.append(", ").append(pagamentoTbl).append(".").append(COLUMNS[1]);
		sql.append(", ").append(pagamentoTbl).append(".").append(COLUMNS[2]);
		sql.append(", ").append(pagamentoTbl).append(".").append(COLUMNS[3]);
		sql.append(", ").append(pagamentoTbl).append(".").append(COLUMNS[4]);
		sql.append(" FROM ").append(pagamentoTbl);
		sql.append(" JOIN ").append(usuarioTbl).append(" ON ").append(pagamentoTbl).append(".").append(COLUMNS[3]);
		sql.append("=").append(usuarioTbl).append(".").append(DBHelper.DATABASE_ID_FIELD);
		sql.append(" JOIN ").append(despesaTbl).append(" ON ").append(pagamentoTbl).append(".").append(COLUMNS[4]);
		sql.append("=").append(despesaTbl).append(".").append(DBHelper.DATABASE_ID_FIELD);
		sql.append(" WHERE ").append(despesaTbl).append(".").append(DBHelper.DATABASE_ID_CONTA).append("=? ");
		
		params.add(String.format("%d", idConta));
		
		if (filtroMes != null) {
			params.add(Util.prepareFiltro(filtroMes));
			sql.append(" AND ").append(pagamentoTbl).append(".").append(COLUMNS[1]).append(" LIKE ? ");
		}
		if (idUsuario != null) {
			params.add(String.format("%d", idUsuario));
			sql.append(" AND ").append(pagamentoTbl).append(".").append(COLUMNS[3]).append("=? ");
		}
		if (idDespesa != null) {
			params.add(String.format("%d", idDespesa));
			sql.append(" AND ").append(pagamentoTbl).append(".").append(COLUMNS[4]).append("=? ");
		}
			
		sql.append(" ORDER BY ").append(pagamentoTbl).append(".").append(COLUMNS[1]).append(" DESC");
		sql.append(", ").append(usuarioTbl).append(".").append(DBHelper.DATABASE_NAME_FIELD);
		sql.append(", ").append(despesaTbl).append(".").append(DBHelper.DATABASE_NAME_FIELD);
		
		String[] paramArray = params.toArray(new String[params.size()]);
		
		Cursor cr = db.rawQuery(sql.toString(), paramArray);

		cr.moveToFirst();
		while (!cr.isAfterLast()) {
			Pagamento pagamento = fillPagamento(cr);

			list.add(pagamento);
			cr.moveToNext();
		}
		return list;
	}

	private Pagamento fillPagamento(Cursor c) {
		Long id = c.getLong(0);
		String data = c.getString(1);
		double valor = c.getDouble(2);
		Long idUsuario = c.getLong(3);
		Long idDespesa = c.getLong(4);

		Despesa despesa = null;
		if (idDespesa != null && idDespesa.longValue() != ZERO) {
			despesa = getDespesaDB(ctx).get(idDespesa);
		}
		Usuario usuario = null;
		if (idUsuario != null && idUsuario.longValue() != ZERO) {
			usuario = getUsuarioDB(ctx).get(idUsuario);
		}

		return new Pagamento(id, despesa, data, usuario, valor);
	}

}
