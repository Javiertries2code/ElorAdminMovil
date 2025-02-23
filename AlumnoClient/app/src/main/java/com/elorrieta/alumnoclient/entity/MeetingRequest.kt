package com.elorrieta.alumnoclient.entity

import java.io.Serializable

data class MeetingRequest(
    var idMeetingRequest: Int? = null,
    var teacherByIdHost: Teacher? = null,
    var teacherByIdGuest: Teacher? = null,
    var meeting: Meeting? = null,
    var idTeacher: Int? = null,
    var status: String? = null
): Serializable
