package com.roomdb.roomteste.view

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.roomdb.roomteste.R
import com.roomdb.roomteste.data.AppDatabase
import com.roomdb.roomteste.domain.ContatoAdapter
import com.roomdb.roomteste.domain.entidade.Contato
import kotlinx.android.synthetic.main.activity_cadastrar.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    private lateinit var contatoAdapter: ContatoAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        val numeroDeContatos: String = ContatoAdapter().getItemCount().toString()
        num_contatos.text =  "$numeroDeContatos contatos"

        fab.setOnClickListener { view ->
            val intente = Intent(this@MainActivity, Cadastrar::class.java)
            startActivity(intente)

//        Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .show()
        }
        CoroutineScope(IO).launch {
            MostrarDados()
        }
     }

    //==========================================================================
    ///////////// SHOW DATA IN THE MAIN THREAD
    //==========================================================================
    fun MakeToast(msg: String) {
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

    suspend fun UpdateRecycler() {
        withContext(Main) {
            MostrarDados()
        }
    }

    //==========================================================================
    ///////////// CONNECTION WITH DAO - COROUTINE THREADS
    //==========================================================================
    private suspend fun MostrarDados() {
        try {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "contato"
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