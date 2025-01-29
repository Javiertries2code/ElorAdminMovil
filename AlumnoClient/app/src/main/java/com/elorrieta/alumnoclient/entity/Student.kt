package com.elorrieta.alumnoclient.entity

import java.io.Serializable

data class Student(
    var idStudent: Int? = null,
    val registered: Boolean? = null,
    var email: String? = null,
    var passwordHashed: String? = null,
    var passwordNotHashed: Int? = null,
    val dni: String? = null,
    var name: String? = null,
    var lastName: String? = null,
    val address: String? = null,
    val phone1: String? = null,
    val phone2: String? = null,
    val foto: ByteArray? = null,
    var user_type: String? = null,
    val registrations: Set<Any> = emptySet()
): Serializable
