package com.elorrieta.alumnoclient.entity

data class Course(
    val idCourse: Int? = null,
    val name: String? = null,
    val registrations: Set<Any> = emptySet(),
    val subjects: Set<Any> = emptySet()
)
