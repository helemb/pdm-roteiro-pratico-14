package com.example.controladortarefas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TarefaAdapter(
    private var lista: List<Tarefa>,
    private val onClick: (Tarefa) -> Unit,
    private val onDelete: (Tarefa) -> Unit
) : RecyclerView.Adapter<TarefaAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDescricao: TextView = itemView.findViewById(R.id.tvDesc)
        val tvResponsavel: TextView = itemView.findViewById(R.id.tvResp)
        val progressBar: ProgressBar = itemView.findViewById(R.id.prog)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarefa, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val t = lista[position]

        holder.tvDescricao.text = t.descricao
        holder.tvResponsavel.text = t.responsavel
        holder.progressBar.progress = t.porcentagem

        holder.itemView.setOnClickListener {
            onClick(t)
        }

        holder.btnDelete.setOnClickListener {
            onDelete(t)
        }
    }

    override fun getItemCount(): Int = lista.size

    fun updateData(newList: List<Tarefa>) {
        lista = newList
        notifyDataSetChanged()
    }
}

