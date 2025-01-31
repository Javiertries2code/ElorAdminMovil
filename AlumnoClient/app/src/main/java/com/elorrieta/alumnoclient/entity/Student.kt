package com.elorrieta.alumnoclient.entity

import java.io.Serializable

data class Student(
    var idStudent: Int? = null,
    var registered: Boolean? = null,
    var email: String? = null,
    var passwordHashed: String? = null,
    var passwordNotHashed: Int? = null,
    val dni: String? = null,
    var name: String? = null,
    var lastName: String? = null,
    var address: String? = null,
    var phone1: String? = null,
    var phone2: String? = null,
    var foto: ByteArray? = null,
    var user_type: String? = null,
    var registrations: Set<Any> = emptySet()
): Serializable
