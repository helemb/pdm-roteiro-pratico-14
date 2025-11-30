package com.example.controladortarefas


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaFragment : Fragment(), EditarDialogFragment.Listener {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: TarefaAdapter
    private lateinit var db: TarefaDbHelper
    private var dialog: EditarDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerTarefas)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        db = TarefaDbHelper(requireContext())

        adapter = TarefaAdapter(
            emptyList(),
            onClick = { tarefa ->
                dialog = EditarDialogFragment.newInstance(tarefa.id)
                dialog?.show(parentFragmentManager, "editar")
            },
            onDelete = { tarefa ->
                db.excluir(tarefa.id)
                carregarTarefas()
            }
        )

        recycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        carregarTarefas()
    }

    override fun onTarefaAtualizada() {
        carregarTarefas()
    }

    override fun onTarefaExcluida() {
        carregarTarefas()
    }

    private fun carregarTarefas() {
        val lista = db.listar()
        adapter.updateData(lista)
    }
}
