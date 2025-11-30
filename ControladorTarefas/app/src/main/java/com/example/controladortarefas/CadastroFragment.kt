package com.example.controladortarefas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class CadastroFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cadastro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etDesc = view.findViewById<EditText>(R.id.etDescricao)
        val etResp = view.findViewById<EditText>(R.id.etResponsavel)
        val seek = view.findViewById<SeekBar>(R.id.seekPorcentagem)
        val tvPerc = view.findViewById<TextView>(R.id.tvPorcent)
        val btn = view.findViewById<Button>(R.id.btnSalvar)

        val db = TarefaDbHelper(requireContext())

        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, p: Int, b: Boolean) {
                tvPerc.text = "$p%"
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        btn.setOnClickListener {
            val tarefa = Tarefa(
                descricao = etDesc.text.toString(),
                responsavel = etResp.text.toString(),
                porcentagem = seek.progress
            )

            db.inserir(tarefa)
            Toast.makeText(requireContext(), "Tarefa adicionada com sucesso!", Toast.LENGTH_SHORT).show()

            etDesc.text.clear()
            etResp.text.clear()
            seek.progress = 0
        }
    }
}