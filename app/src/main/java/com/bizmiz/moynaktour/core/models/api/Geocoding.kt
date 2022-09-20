package com.bizmiz.moynaktour.core.models.api

data class Geocoding(
    val city: String,
    val country: String,
    val county: String,
    val district: String,
    val state: String
)