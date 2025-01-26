package com.elorrieta.alumnoclient.entity

data class Teacher(
    var idTeacher: Int? = null,
    var registered: Boolean? = null,
    var passwordHashed: String? = null,
    var passwordNotHashed: Int? = null,
    var email: String? = null,
    var dni: String? = null,
    var name: String? = null,
    var lastName: String? = null,
    var address: String? = null,
    var phone1: String? = null,
    var phone2: String? = null,
    var user_type: String? = null,

    var subjects: Set<Subject>? = emptySet(),
    var meetingRequestsForIdHost: Set<MeetingRequest>? = emptySet(),
    var meetingRequestsForIdGuest: Set<MeetingRequest>? = emptySet()
)
