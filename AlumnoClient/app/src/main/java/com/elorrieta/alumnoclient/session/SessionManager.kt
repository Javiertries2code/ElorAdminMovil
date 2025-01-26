package com.elorrieta.alumnoclient.session

import com.elorrieta.alumnoclient.entity.Student
import com.elorrieta.alumnoclient.entity.Teacher


object SessionManager {
    private var currentStudent: Student? = null
    private var currentTeacher: Teacher? = null
    private var email: String? = null




    fun setUser(student: Student) {
        currentStudent = student
        currentTeacher = null
        email = student.email
    }

    fun setUser(teacher: Teacher) {
        currentTeacher = teacher
        currentStudent = null
        email = teacher.email
    }

    fun getUser(): Any? {
        return currentStudent ?: currentTeacher
    }

    fun getEmail(): String {
        return email.toString()
    }


    fun isLoggedIn(): Boolean {
        return currentStudent != null || currentTeacher != null
    }

    fun logout() {
        currentStudent = null
        currentTeacher = null
    }
}
