package com.elorrieta.alumnoclient.entity

data class Student(
    var idStudent: Int? = null,
    val registered: Boolean? = null,
    val email: String? = null,
    val passwordHashed: String? = null,
    var passwordNotHashed: Int? = null,
    val dni: String? = null,
    var name: String? = null,
    var lastName: String? = null,
    val address: String? = null,
    val phone1: String? = null,
    val phone2: String? = null,
    val foto: ByteArray? = null,
    val registrations: Set<Any> = emptySet()
)
