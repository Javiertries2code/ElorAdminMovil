package com.elorrieta.alumnoclient.entity

import java.io.Serializable

data class Course(
    val idCourse: Int? = null,
    val name: String? = null,
    val registrations: Set<Any> = emptySet(),
    val subjects: Set<Any> = emptySet()
): Serializable
