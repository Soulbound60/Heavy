import com.google.firebase.firestore.DocumentSnapshot
import com.imsiu.heavy.JSON.Birthday

data class ClientModel(
    var id: String = "123",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var phoneNumber: String = "",
    var type: String = "",
    var city: String = "",
    var birthday: Birthday? = null,
    var lat: Double = 0.0,
    var long: Double = 0.0,
   // var birthday: Birthday? = null  // Added birthday field
)
fun DocumentSnapshot.toClientModel(): ClientModel {
    // Extract birthday map from Firestore document
    val birthdayMap = get("birthday") as? Map<String, Any>

    // Extract birthday fields from the map
    val birthdayYear = (birthdayMap?.get("year") as? Long)?.toInt()
    val birthdayMonth = (birthdayMap?.get("month") as? Long)?.toInt()
    val birthdayDay = (birthdayMap?.get("day") as? Long)?.toInt()

    // Create Birthday object
    val birthday = if (birthdayYear != null && birthdayMonth != null && birthdayDay != null) {
        Birthday(birthdayYear, birthdayMonth, birthdayDay)
    } else null

    // ... (other code remains the same)

    return ClientModel(
        id = id,
        name = getString("name") ?: "",
        email = getString("email") ?: "",
        password = getString("password") ?: "",
        phoneNumber = getString("phoneNumber") ?: "",
        type = getString("type") ?: "",
        city = getString("city") ?: "",
        birthday = birthday, // Assign the birthday object
        lat = getDouble("lat") ?: 0.0,
        long = getDouble("long") ?: 0.0,
    )
}

