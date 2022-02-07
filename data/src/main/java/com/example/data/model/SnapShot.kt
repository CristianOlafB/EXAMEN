package com.example.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class SnapShot(
    var id: String = "",
    var title: String = "",
    var photoUrl: String ="",
)