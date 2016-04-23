package br.com.caelum.cadastro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.cadastroalunos.entities.Aluno;

/**
 * Created by android6021 on 02/04/16.
 */
public class AlunoDAO extends SQLiteOpenHelper{

    private static final int VERSAO = 2;
    private static final String TABELA = "Aluno";

    public AlunoDAO(Context context) {
        super(context, "CadastroAluno", null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA +
                " (id INTEGER PRIMARY KEY, " +
                " nome TEXT NOT NULL," +
                " telefone TEXT, " +
                " endereco TEXT, " +
                " site TEXT, " +
                "caminhoFoto TEXT," +
                " nota REAL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "ALTER TABLE " + TABELA + " ADD COLUMN caminhoFoto TEXT;";
        db.execSQL(sql);
    }

    public void inserir(Aluno aluno){

        getWritableDatabase().insert(TABELA, null, retornaContentValuesPreenchido(aluno));
        Log.i("DB_ID",String.valueOf(aluno.getId()));
    }

    public List<Aluno> getLista(){

        ArrayList<Aluno> alunos = new ArrayList<Aluno>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = null;

        try {
            c = db.rawQuery("SELECT * FROM " + TABELA + ";", null);

            while (c.moveToNext()){
                Aluno aluno = new Aluno();

                aluno.setId(c.getLong(c.getColumnIndex("id")));
                aluno.setNome(c.getString(c.getColumnIndex("nome")));
                aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
                aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
                aluno.setSite(c.getString(c.getColumnIndex("site")));
                aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
                aluno.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

                alunos.add(aluno);
            }
        }

        finally {
            c.close();
        }

        return alunos;
    }

    public void deletar(Aluno aluno){

        String[] args = {aluno.getId().toString()};
        getWritableDatabase().delete(TABELA, "id=?", args);
    }

    public void alterar(Aluno aluno){

        String[] idParaSerAlterado = {aluno.getId().toString()};
        getWritableDatabase().update(TABELA, retornaContentValuesPreenchido(aluno), "id=?", idParaSerAlterado);
    }

    public ContentValues retornaContentValuesPreenchido (Aluno aluno){

        ContentValues values = new ContentValues();
        values.put("id", aluno.getId());
        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());

        return values;
    }

    public void insereOuAltera(Aluno aluno){

        //Verificando o Id que ele está retornandoll
        Log.i("ALTERAR", String.valueOf(aluno.getId()));
        if (aluno.getId() == null){
            inserir(aluno);
        } else {
            alterar(aluno);
        }
    }

    public boolean isAluno(String telefone) {
        String[] parametros = {telefone};

        Cursor rawQuery = getReadableDatabase().rawQuery("SELECT telefone FROM " + TABELA
                + " WHERE telefone =?", parametros);

        int total = rawQuery.getCount();
        rawQuery.close();

        return total > 0;
    }
}
