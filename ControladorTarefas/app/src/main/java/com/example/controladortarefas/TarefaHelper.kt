package com.example.controladortarefas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TarefaDbHelper(context: Context) : SQLiteOpenHelper(context, "tarefas.db", null, 1) {

    private val TABLE = "tarefas"
    private val COL_ID = "id"
    private val COL_DESC = "descricao"
    private val COL_RESP = "responsavel"
    private val COL_PORC = "porcentagem"

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE $TABLE (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_DESC TEXT,
                $COL_RESP TEXT,
                $COL_PORC INTEGER
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun inserir(t: Tarefa): Long {
        val cv = ContentValues()
        cv.put(COL_DESC, t.descricao)
        cv.put(COL_RESP, t.responsavel)
        cv.put(COL_PORC, t.porcentagem)

        return writableDatabase.insert(TABLE, null, cv)
    }

    fun listar(): ArrayList<Tarefa> {
        val lista = ArrayList<Tarefa>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE", null)

        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Tarefa(
                        id = cursor.getInt(0),
                        descricao = cursor.getString(1),
                        responsavel = cursor.getString(2),
                        porcentagem = cursor.getInt(3)
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }

    fun buscarPorId(id: Int): Tarefa? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE,
            arrayOf(COL_ID, COL_DESC, COL_RESP, COL_PORC),
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        val tarefa = if (cursor.moveToFirst()) {
            Tarefa(
                id = cursor.getInt(0),
                descricao = cursor.getString(1),
                responsavel = cursor.getString(2),
                porcentagem = cursor.getInt(3)
            )
        } else {
            null
        }

        cursor.close()
        return tarefa
    }

    fun atualizar(tarefa: Tarefa) {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put("descricao", tarefa.descricao)
        cv.put("responsavel", tarefa.responsavel)
        cv.put("porcentagem", tarefa.porcentagem)

        db.update("tarefas", cv, "id = ?", arrayOf(tarefa.id.toString()))
        db.close()
    }


    fun excluir(id: Int) {
        val db = writableDatabase
        db.delete("tarefas", "id = ?", arrayOf(id.toString()))
        db.close()
    }

}