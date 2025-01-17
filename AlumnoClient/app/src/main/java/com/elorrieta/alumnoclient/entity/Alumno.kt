package com.elorrieta.alumnoclient.entity

/**
 * The entity Alumno, used to manipulate data as objects instead of JSON
 */
data class Alumno (private val id: Int, private val name: String, private val surname: String,
    private val pass: String, private val edad: Int)