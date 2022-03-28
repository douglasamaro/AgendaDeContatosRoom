package com.roomdb.roomteste.view

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.roomdb.roomteste.R
import com.roomdb.roomteste.data.AppDatabase
import com.roomdb.roomteste.domain.entidade.Contato
import kotlinx.android.synthetic.main.activity_cadastrar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Cadastrar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)

        adicionar.setOnClickListener{
            CoroutineScope(IO).launch {
                Salvar()
            }
        }
    }

    //==========================================================================
    ///////////// SHOW DATA IN THE MAIN THREAD
    //==========================================================================
    private fun MakeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun MudarTelaAction(){
        val intente = Intent(this@Cadastrar, MainActivity::class.java)
        startActivity(intente)
    }

    //==========================================================================
    ///////////// CONNECTION THREDS
    //==========================================================================
    private suspend fun ConnectThreads(msg: String) {
        withContext(Dispatchers.Main){
            MakeToast(msg)
        }
    }

    private suspend fun MudarTela() {
        withContext(Dispatchers.Main){
            MudarTelaAction()
        }
    }

    //==========================================================================
    ///////////// CONNECTION WITH DAO - COROUTINE THREADS
    //==========================================================================
    private suspend fun Salvar(){
        try {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "newContact"
            ).build()

            if(nome.text.toString().equals("") || numero.text.toString().equals("") || categoria.text.toString().equals("") || cor.text.toString().equals("")) {
                ConnectThreads("preencha todos os campos")
            } else {
                val newContact = Contato(
                    null,
                    nome.text.toString(),
                    numero.text.toString(),
                    categoria.text.toString(),
                    cor.text.toString()
                )
                val contatoDao = db.contatoDao()
                contatoDao.insertAll(newContact)
                ConnectThreads("dados salvos com sucesso")
                nome.text.clear()
                numero.text.clear()
                categoria.text.clear()
                cor.text.clear()
                MudarTela()

            }
        } catch (e: Exception) {
            ConnectThreads("Notamos um err " + e)
            Log.d(ContentValues.TAG, "message ", e)
        }
    }
}