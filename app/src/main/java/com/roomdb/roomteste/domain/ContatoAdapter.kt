package com.roomdb.roomteste.domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roomdb.roomteste.R
import com.roomdb.roomteste.domain.entidade.Contato
import com.roomdb.roomteste.view.MainActivity
import kotlinx.android.synthetic.main.contato_layout.view.*

class ContatoAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Contato> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContatoAdapter.ContatosViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contato_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ContatoAdapter.ContatosViewHolder ->{
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setDataSet(contato: List<Contato>) {
        this.items = contato
    }


    class ContatosViewHolder constructor(itemView : View): RecyclerView.ViewHolder(itemView){

        private val contatoNome = itemView.c_nome
        private val contatoNumero = itemView.c_numero
        private val contatoCategoria = itemView.c_categoria
        private val contatoCor = itemView.c_cor


        fun bind(contato: Contato) {

            contatoNome.text = contato.Name
            contatoNumero.text = contato.Number
            contatoCategoria.text = contato.Category


        }
    }
}