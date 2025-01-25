package com.elorrieta.alumnoclient.entity

data class Subject(
    var idSubject: Int? = null,
    var teacher: Teacher? = null,
    var code: String? = null,
    var hours: Int? = null,
    var schedules: Set<Schedule>? = emptySet(),
    var courses: Set<Course>? = emptySet()
)
