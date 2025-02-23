package com.elorrieta.alumnoclient.entity

import java.io.Serializable

data class Registration(
    var idRegistration: Int? = null,
    var student: Student? = null,
    var course: Course? = null,
    var typeRegistration: String? = null,
    var onDual: Boolean? = null,
    var dateRegistration: java.sql.Timestamp? = null
): Serializable
