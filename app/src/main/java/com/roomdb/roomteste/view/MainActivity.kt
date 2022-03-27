package com.roomdb.roomteste.view

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.room.Room
import com.roomdb.roomteste.R
import com.roomdb.roomteste.data.AppDatabase
import com.roomdb.roomteste.domain.ContatoAdapter
import com.roomdb.roomteste.domain.entidade.Contato
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private lateinit var contatoAdapter: ContatoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

       adicionar.setOnClickListener{
           CoroutineScope(IO).launch {
               Salvar()
           }
        }

        CoroutineScope(IO).launch {
            MostrarDados()
        }
     }
    //==========================================================================
    ///////////// SHOW DATA IN THE MAIN THREAD
    //==========================================================================
    private fun MakeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun addDataSource(list: List<Contato>) {
        contatoAdapter.setDataSet(list)
    }

    private fun initRecyclerView() {
        contatoAdapter = ContatoAdapter()

        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = contatoAdapter

    }

    private fun SetarRecycler(contato: List<Contato>) {
        val num: Int = contato.size
        var i = 0
        val list = ArrayList<Contato>()
        while (i < num) {
            list.add(
                Contato(
                    null,
                    contato[i].Name.toString(),
                    contato[i].Number.toString(),
                    contato[i].Category.toString(),
                    contato[i].Color.toString()
                )
            )
            i++
        }
        addDataSource(list)
    }
    //==========================================================================
    ///////////// CONNECTION THREDS
    //==========================================================================
    private suspend fun ConnectThreads(msg: String) {
        withContext(Main){
            MakeToast(msg)
        }
    }

    private suspend fun EnviarDados(contato: List<Contato>) {
        withContext(Main) {
            SetarRecycler(contato)
        }
    }

    private suspend fun UpdateRecycler() {
        withContext(Main) {
            MostrarDados()
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
                UpdateRecycler()

            }
        } catch (e: Exception) {
            ConnectThreads("Notamos um err " + e)
            Log.d(TAG, "message ", e)
        }
    }

    private suspend fun MostrarDados() {
        try {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "newContact"
            ).build()

            val contatoDao = db.contatoDao()
            val dados = contatoDao.getAll()


            EnviarDados(dados)

        } catch (e: Exception) {
            ConnectThreads("Notamos um err " + e)
            Log.d(TAG, "message ", e)
        }
    }
}