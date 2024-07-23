package com.example.todo.Task

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val dueDate: String,
    val priority: Int,
    val category: String


)
