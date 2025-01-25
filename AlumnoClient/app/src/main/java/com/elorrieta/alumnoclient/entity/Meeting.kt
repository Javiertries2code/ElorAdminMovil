package com.elorrieta.alumnoclient.entity

data class Meeting(
    val idMeeting: Int? = null,
    val dayWeek: String? = null,
    val hour: String? = null,
    val dayYear: java.sql.Date? = null,
    val classroom: Int? = null,
    val receiver: String? = null,
    val confirmed: Boolean? = null,
    val title: String? = null,
    val numGuests: Int? = null,
    val remainingAcceptance: Int? = null,
    val topic: String? = null,
    val meetingRequests: Set<Any> = emptySet()
)