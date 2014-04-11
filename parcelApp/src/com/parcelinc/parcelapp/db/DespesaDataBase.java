package com.parcelinc.parcelapp.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parcelinc.parcelapp.pojo.Despesa;

public class DespesaDataBase implements DataBase<Despesa> {

	private static final DespesaDataBase instance = new DespesaDataBase();

	private SQLiteDatabase db;

	public static final String[] COLUMNS = new String[] {
			DBHelper.DATABASE_ID_FIELD, DBHelper.DATABASE_NAME_FIELD,
			DBHelper.DATABASE_ID_CONTA };

	private DespesaDataBase() {
		// Do nothing
	}

	/**
	 * Get DespesaDataBase instance
	 * 
	 * @param ctx
	 *            Context
	 * @return The DespesaDatabase instance.
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
		values.put(DBHelper.DATABASE_ID_CONTA, despesa.getIdConta());

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

	public long insert(Despesa despesa, int qntd, double valor, long idUsuario, Calendar calPagamento) {
		long idDespesa = -1;

		ContentValues values = new ContentValues();
		try {
			db.beginTransaction();

			idDespesa = insert(despesa);

			for (int i = 0; i < qntd; i++) {
				values.clear();

				values.put(DBHelper.DATABASE_ID_DESPESA, idDespesa);
				values.put(DBHelper.DATABASE_ID_USUARIO, idUsuario);
				values.put(DBHelper.DATABASE_VALUE_FIELD, valor);
				values.put(DBHelper.DATABASE_DATE_FIELD, DateUtil.getDate(calPagamento));

				calPagamento.add(Calendar.MONTH, 1);

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

		Cursor c = db.query(DBHelper.TBL_DESPESAS, COLUMNS,
				DBHelper.DATABASE_ID_FIELD + "=?",
				new String[] { String.format("%d", id) }, null, null,
				DBHelper.DATABASE_ID_FIELD);

		c.moveToFirst();
		if (!c.isAfterLast()) {
			despesa = fillDespesa(c);
		}

		return despesa;
	}

	public List<Despesa> getList() {
		List<Despesa> list = new ArrayList<Despesa>();
		Cursor cr = db.query(DBHelper.TBL_DESPESAS, COLUMNS, null, null, null,
				null, DBHelper.DATABASE_NAME_FIELD);

		Map<Long, List<Long>> mapa = listarRelacao(null, null);

		cr.moveToFirst();
		while (!cr.isAfterLast()) {
			Despesa despesa = fillDespesa(cr, mapa);

			list.add(despesa);
			cr.moveToNext();
		}
		return list;
	}

	/**
	 * Retorna a lista dos IDs de Pagamento de uma Despesa no mês de referência
	 * 
	 * @param idDespesa
	 *            ID da Despesa para filtrar
	 * @param filtroMes
	 *            Ano e Mês para filtro, conforme
	 *            {@link DateUtil#getFilterMonth(String, int)}
	 * @return Lista de IDs de Pagamento
	 */
	public List<Long> getListaPagamentos(Long idDespesa, String filtroMes) {
		Map<Long, List<Long>> mapa = listarRelacao(idDespesa, filtroMes);
		return mapa.get(idDespesa);
	}

	private Map<Long, List<Long>> listarRelacao(Long idDespesa, String filtroMes) {
		Map<Long, List<Long>> mapa = new HashMap<Long, List<Long>>();

		String[] cols = new String[] { DBHelper.DATABASE_ID_DESPESA,
				DBHelper.DATABASE_ID_FIELD };
		StringBuilder where = new StringBuilder();
		List<String> params = new ArrayList<String>();

		if (idDespesa != null) {
			where.append(DBHelper.DATABASE_ID_DESPESA).append(" = ?");
			params.add(String.format("%d", idDespesa));
		}

		if (filtroMes != null && "".equals(filtroMes.trim())) {
			if (where.length() > 0) {
				where.append(" and ");
			}
			where.append(DBHelper.DATABASE_DATE_FIELD).append(" like ?");
			params.add("%" + filtroMes + "-%");
		}

		String[] paramsArray = null;
		if (params.size() > 1) {
			paramsArray = params.toArray(new String[params.size()]);
		}

		Cursor cr = db.query(DBHelper.TBL_PAGAMENTOS, cols, where.toString(),
				paramsArray, null, null, DBHelper.DATABASE_DATE_FIELD);

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

	public List<Despesa> getList(Long idConta, String filtroMes) {
		filtroMes = filtroMes + DateUtil.SEPARATOR;

		// TODO filtrar despesa
		return null;
	}

	private Despesa fillDespesa(Cursor c) {
		Long id = c.getLong(0);

		return fill(c, id, getListaPagamentos(id, null));
	}

	private Despesa fillDespesa(Cursor c, Map<Long, List<Long>> mapaIdsPagamento) {
		Long id = c.getLong(0);

		return fill(c, id, mapaIdsPagamento.get(id));
	}
	
	private Despesa fill(Cursor c, Long id, List<Long> idsPagamento) {
		String nome = c.getString(1);
		Long idConta = c.getLong(2);

		return new Despesa(id, nome, idConta, idsPagamento);
	}
	

}