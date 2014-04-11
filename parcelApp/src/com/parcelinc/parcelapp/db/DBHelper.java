package com.parcelinc.parcelapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
public static final String NOME_DO_BANCO = "parcelApp";
public static final int VERSAO_BANCO = 1;

public static final String DATABASE_ID_FIELD = "_ID";
public static final String DATABASE_ID_CONTA = "ID_CONTA";
public static final String DATABASE_ID_USUARIO = "ID_USUARIO";
public static final String DATABASE_ID_DESPESA = "ID_DESPESA";

public static final String DATABASE_NAME_FIELD = "NOME";
public static final String DATABASE_OBS_FIELD = "OBSERVACAO"; 
public static final String DATABASE_VALUE_FIELD = "VALOR"; 
public static final String DATABASE_DATE_FIELD = "DATA";


	
    // TABELA USUARIOS
	public static final String TBL_USUARIOS = "usuarios";
		
	private static final String SCRIPT_TABLE_USUARIOS = "CREATE TABLE " + TBL_USUARIOS + " (" 
			+ DATABASE_ID_FIELD + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ DATABASE_NAME_FIELD + " TEXT NOT NULL, "
			+ DATABASE_OBS_FIELD + " TEXT);";
	
	
	//TABELA CONTA
	public static final String TBL_CONTAS = "contas";
	
	private static final String SCRIPT_TABLE_CONTAS = "CREATE TABLE " + TBL_CONTAS + " (" 
			+ DATABASE_ID_FIELD + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ DATABASE_NAME_FIELD + " TEXT);";
	
	//TABELA CONTAS_USUARIOS
	
	public static final String TBL_CONTAS_USUARIOS = "contas_usuarios";
		
	private static final String SCRIPT_TABLE_CONTAS_USUARIOS = "CREATE TABLE " + TBL_CONTAS_USUARIOS + " (" 
			+ DATABASE_ID_USUARIO + " INTEGER NOT NULL REFERENCES "+TBL_USUARIOS+" ("+DATABASE_ID_FIELD+") ON DELETE CASCADE, "
			+ DATABASE_ID_CONTA + " INTEGER NOT NULL REFERENCES "+TBL_CONTAS+" ("+DATABASE_ID_FIELD+") ON DELETE CASCADE, "
			+ "PRIMARY KEY ("+DATABASE_ID_USUARIO+", "+DATABASE_ID_CONTA+")); ";
	
	// TABELA DESPESAS
	public static final String TBL_DESPESAS = "despesas";
	
	private static final String SCRIPT_TABLE_DESPESAS = "CREATE TABLE " + TBL_DESPESAS + " (" 
			+ DATABASE_ID_FIELD + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ DATABASE_NAME_FIELD + " TEXT NOT NULL, " 
			+ DATABASE_ID_CONTA + " INTEGER NOT NULL REFERENCES "+TBL_CONTAS+" ("+DATABASE_ID_FIELD+") ON DELETE CASCADE);";
	
	// TABELA PAGAMENTOS
	public static final String TBL_PAGAMENTOS = "pagamentos";
	
	private static final String SCRIPT_TABLE_PAGAMENTOS = "CREATE TABLE " + TBL_PAGAMENTOS + " (" 
			+ DATABASE_ID_FIELD + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ DATABASE_DATE_FIELD + " DATE, "
			+ DATABASE_VALUE_FIELD + " REAL, "
			+ DATABASE_ID_USUARIO + " INTEGER NOT NULL REFERENCES "+TBL_USUARIOS+" ("+DATABASE_ID_FIELD+") ON DELETE CASCADE, "
			+ DATABASE_ID_DESPESA + " INTEGER NOT NULL REFERENCES "+TBL_DESPESAS+" ("+DATABASE_ID_FIELD+") ON DELETE CASCADE); ";
	

	/**
	 * Este método foi posto aqui apenas facilitar o entendimento dos parâmetros
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	protected DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * Toda classe DBHelper só precisará do Context para sua construção.  
	 * @param ctx Contexto (activity) que está gerindo o acesso ao banco. 
	 */
	public DBHelper(Context ctx) {
		super(ctx, NOME_DO_BANCO, null, VERSAO_BANCO);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SCRIPT_TABLE_USUARIOS);
		db.execSQL(SCRIPT_TABLE_CONTAS);
		db.execSQL(SCRIPT_TABLE_CONTAS_USUARIOS);
		db.execSQL(SCRIPT_TABLE_DESPESAS);
		db.execSQL(SCRIPT_TABLE_PAGAMENTOS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
