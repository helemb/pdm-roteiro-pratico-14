package com.example.controladortarefas

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class EditarDialogFragment : DialogFragment() {
    interface Listener {
        fun onTarefaAtualizada()
        fun onTarefaExcluida()
    }

    private val tarefaId: Int by lazy {
        requireArguments().getInt(ARG_TAREFA_ID)
    }

    private val db by lazy { TarefaDbHelper(requireContext()) }
    private var tarefa: Tarefa? = null
    private var listener: Listener? = null

    companion object {
        private const val ARG_TAREFA_ID = "tarefa_id"

        fun newInstance(id: Int): EditarDialogFragment {
            val fragment = EditarDialogFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_TAREFA_ID, id)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tarefa = db.buscarPorId(tarefaId)
        listener = parentFragment as? Listener ?: activity as? Listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = layoutInflater.inflate(R.layout.dialog_editar, null)

        val editDescricao = view.findViewById<EditText>(R.id.editDescricaoEditar)
        val editResponsavel = view.findViewById<EditText>(R.id.editRespEditar)
        val seek = view.findViewById<SeekBar>(R.id.seekEditar)

        tarefa?.let {
            editDescricao.setText(it.descricao)
            editResponsavel.setText(it.responsavel)
            seek.progress = it.porcentagem
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Editar Tarefa")
            .setView(view)
            .setPositiveButton("Salvar") { _, _ ->
                val atual = tarefa ?: return@setPositiveButton
                val novaDescricao = editDescricao.text.toString()
                val novoResp = editResponsavel.text.toString()
                val novaPorcentagem = seek.progress

                val tarefaAtualizada = atual.copy(
                    descricao = novaDescricao,
                    responsavel = novoResp,
                    porcentagem = novaPorcentagem
                )

                db.atualizar(tarefaAtualizada)
                listener?.onTarefaAtualizada()
            }
            .setNeutralButton("Excluir") { _, _ ->
                tarefa?.let {
                    db.excluir(it.id)
                    listener?.onTarefaExcluida()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    override fun onDestroy() {
        listener = null
        super.onDestroy()
    }
}
