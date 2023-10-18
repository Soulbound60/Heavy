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
    // Extract birthday fields from Firestore document
    val birthdayYear = getLong("birthdayYear")?.toInt()
    val birthdayMonth = getLong("birthdayMonth")?.toInt()
    val birthdayDay = getLong("birthdayDay")?.toInt()

    val birthday = if (birthdayYear != null && birthdayMonth != null && birthdayDay != null) {
        Birthday(birthdayYear, birthdayMonth, birthdayDay)
    } else null

    return ClientModel(
        id = id,
        name = getString("name") ?: "",
        email = getString("email") ?: "",
        password = getString("password") ?: "",
        phoneNumber = getString("phoneNumber") ?: "",
        type = getString("type") ?: "",
        city = getString("city") ?: "",
        birthday = birthday , // Assign the birthday object,
        lat = getDouble("lat") ?: 0.0,
        long = getDouble("long") ?: 0.0,

    )
}
