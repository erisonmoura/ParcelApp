package com.parcelinc.parcelapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
public static final String NOME_DO_BANCO = "parcelApp";

public static final String DATABASE_ID_FIELD = "_ID";
public static final String DATABASE_ID_CONTA = "_ID_CONTA";
public static final String DATABASE_ID_USUARIO = "_ID_USUARIO";
public static final String DATABASE_ID_DESPESA = "_ID_DESPESA";

public static final String DATABASE_NAME_FIELD = "NOME";
public static final String DATABASE_OBS_FIELD = "OBSERVACAO"; 
public static final String DATABASE_VALUE_FIELD = "VALOR"; 
public static final String DATABASE_DATE_FIELD = "DATA";


	
    // TABELA USUARIOS
	public static final String TBL_USUARIOS = "usuarios";
		
	private static final String SCRIPT_TABLE_USUARIOS = "CREATE TABLE " + TBL_USUARIOS
			+ " (" + DATABASE_ID_FIELD
			+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,"
			+ DATABASE_NAME_FIELD + " TEXT NOT NULL ," 
			+ DATABASE_OBS_FIELD + " TEXT);";
	
	
	//TABELA CONTA
	public static final String TBL_CONTAS = "contas";
	
	private static final String SCRIPT_TABLE_CONTAS = "CREATE TABLE " + TBL_CONTAS
			+ " (" + DATABASE_ID_FIELD
			+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,"
			+ DATABASE_NAME_FIELD + " TEXT);";
	
	//TABELA CONTAS_USUARIOS
	
	public static final String TBL_CONTAS_USUARIOS = "contas_usuarios";
		
	private static final String SCRIPT_TABLE_CONTAS_USUARIOS = "CREATE TABLE " + TBL_CONTAS_USUARIOS
			+ " (" + DATABASE_ID_FIELD
			+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,"
			+ DATABASE_ID_USUARIO + " INTEGER NOT NULL, FOREIGN KEY ("+DATABASE_ID_USUARIO+") REFERENCES "+TBL_USUARIOS+" ("+DATABASE_ID_FIELD+"),"
			+ DATABASE_ID_CONTA + " INTEGER NOT NULL, FOREIGN KEY ("+DATABASE_ID_CONTA+") REFERENCES "+TBL_CONTAS+" ("+DATABASE_ID_FIELD+");";
	
	// TABELA DESPESAS
	public static final String TBL_DESPESAS = "despesas";
	
	private static final String SCRIPT_TABLE_DESPESAS = "CREATE TABLE " + TBL_DESPESAS
			+ " (" + DATABASE_ID_FIELD
			+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,"
			+ DATABASE_NAME_FIELD + " TEXT NOT NULL ," 
			+ DATABASE_ID_CONTA + " INTEGER NOT NULL, FOREIGN KEY ("+DATABASE_ID_CONTA+") REFERENCES "+TBL_CONTAS+" ("+DATABASE_ID_FIELD+");";
	
	// TABELA PAGAMENTOS
	public static final String TBL_PAGAMENTOS = "pagamentos";
	
	private static final String SCRIPT_TABLE_PAGAMENTOS = "CREATE TABLE " + TBL_PAGAMENTOS
			+ " (" + DATABASE_ID_FIELD
			+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,"
			+ DATABASE_DATE_FIELD + " DATE ,"
			+ DATABASE_VALUE_FIELD + " REAL ,"
			+ DATABASE_ID_USUARIO + " INTEGER NOT NULL, FOREIGN KEY ("+DATABASE_ID_USUARIO+") REFERENCES "+TBL_USUARIOS+" ("+DATABASE_ID_FIELD+"),"
			+ DATABASE_ID_CONTA + " INTEGER NOT NULL, FOREIGN KEY ("+DATABASE_ID_CONTA+") REFERENCES "+TBL_CONTAS+" ("+DATABASE_ID_FIELD+");";
	

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DBHelper(Context ctx) {
		super(ctx, NOME_DO_BANCO, null, 1);
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