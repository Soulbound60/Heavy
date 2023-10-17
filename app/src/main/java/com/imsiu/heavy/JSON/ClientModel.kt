import com.google.firebase.firestore.DocumentSnapshot

data class ClientModel(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var phoneNumber: String = "",
    var type: String = "",
    var city: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0
)

// Extension function to convert a Firestore DocumentSnapshot to ClientModel
fun DocumentSnapshot.toClientModel(): ClientModel {
    return ClientModel(
        id = id,
        name = getString("name") ?: "",
        email = getString("email") ?: "",
        password = getString("password") ?: "",
        phoneNumber = getString("phoneNumber") ?: "",
        type = getString("type") ?: "",
        city = getString("city") ?: "",
        lat = getDouble("lat") ?: 0.0,
        long = getDouble("long") ?: 0.0
    )
}
