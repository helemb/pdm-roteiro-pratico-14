package com.example.controladortarefas

data class Tarefa(
    var id: Int = 0,
    var descricao: String,
    var responsavel: String,
    var porcentagem: Int
)
