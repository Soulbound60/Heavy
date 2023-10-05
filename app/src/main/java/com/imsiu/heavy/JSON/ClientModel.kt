package com.imsiu.heavy.JSON

import java.io.Serializable

data class ClientModel(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var phoneNumber: String = "",
    var type: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0
)
