package com.example.data.model

import com.google.firebase.firestore.IgnoreExtraProperties


@IgnoreExtraProperties
data class LocationL(
    var latitude: String = "",
    var longitude: String = "",
    var currentdate: String = "",
)