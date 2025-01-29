package com.elorrieta.alumnoclient.entity

import java.io.Serializable

data class Schedule(
    var idSchedule: Int? = null,
    var subject: Subject? = null,
    var dayWeek: String? = null,
    var hour: String? = null,
    var dayYear: java.sql.Date? = null,
    var classroom: Int? = null
): Serializable
