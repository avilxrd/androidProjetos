package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class sqlHelper extends SQLiteOpenHelper { //precisar criar os dois métodos

    private static final String db_Name = "wc.db";
    private static final int db_Version = 1;

    private static sqlHelper instance;

    static sqlHelper getInstance(Context context){ // criar ou retornar instancia do banco.
        if (instance==null)
            instance=new sqlHelper(context);
        return instance;
    }

    public sqlHelper(@Nullable Context context) { //construtor da classe (deixar nessa estrutura utilizando as variavies db_Name e db_version, Factory nao precisa)
        super(context, db_Name, null, db_Version);
    }
    //---------------metodos para controle de para adição ou atualização da base de dados--------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { // executada quando ainda nao existe o banco
        sqLiteDatabase.execSQL( //text = string, decimal=float, date = datetime
                "CREATE TABLE palpite (id INTEGER primary key, campeao TEXT, segundo TEXT, terceiro TEXT)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { //executa quando esta atualizando...

    }
    // aqui é a parte de busca ------

    @SuppressLint("Range")
    List<Registro> getRegistro(String valor){

        List<Registro> registros = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        //Cursor cursor = db.rawQuery("Select * from agenda where disciplina = ?",new String[]{valor});
        Cursor cursor = db.rawQuery("Select * from palpite",null);

        try{
            if(cursor.moveToFirst()){
                do{
                    Registro registro = new Registro();
                    registro.campeao = cursor.getString(cursor.getColumnIndex("campeao"));
                    registro.segundo = cursor.getString(cursor.getColumnIndex("segundo"));
                    registro.terceiro = cursor.getString(cursor.getColumnIndex("terceiro"));

                    registros.add(registro);
                }while(cursor.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return registros;
    }

    //------------------------------------
//-----------------------------------------------------------------------
    long addAgendamento(String campeao, String segundo, String terceiro){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase(); //usamos writable ou readable
        // precisa porque podemos gerar erro ao adicionar dados...

        long id_table = 0;
        try{
            sqLiteDatabase.beginTransaction();
            ContentValues valores = new ContentValues();
            valores.put("campeao", campeao);
            valores.put("segundo", segundo);
            valores.put("terceiro", terceiro);
            id_table = sqLiteDatabase.insertOrThrow("palpite",null, valores);
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e){
            Log.e("sqllite",e.getMessage(),e);

        } finally {
            sqLiteDatabase.endTransaction();
        }
        return id_table;

    }

}
