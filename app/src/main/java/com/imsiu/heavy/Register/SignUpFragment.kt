package com.imsiu.heavy.Register

import ClientModel
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsiu.heavy.MainActivity
import com.imsiu.heavy.Helper.FireBaseViewxModel
import com.imsiu.heavy.R
import com.imsiu.heavy.databinding.FragmentSignUpBinding
import androidx.core.content.ContextCompat
import com.imsiu.heavy.JSON.Birthday
import java.util.Calendar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class SignUpFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentSignUpBinding
    val db = Firebase.firestore
    var isDriver: Boolean = false
    var ClientOrDriver: String = "Client"
    lateinit var fireBaseViewxModel : FireBaseViewxModel
    var dateFlag : Boolean = false
    var birthday : Birthday = Birthday(0,0,0)

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        (activity as MainActivity).showBottomNavigation(false)
        fireBaseViewxModel = FireBaseViewxModel(requireContext())
        val switch: SwitchCompat = binding.switch1
        switch.setOnCheckedChangeListener { _, isChecked ->
            ClientOrDriver = if (isChecked) "Driver" else "Client"
            if (isChecked) {
                binding.switch1.text = "Registed As Driver"
            } else {
                binding.switch1.text = "Register As Client"
            }
            // Optionally, handle any other logic based on the isChecked value
        }
        binding.idTxt.addTextChangedListener(createNumericTextWatcher(binding.idTxt))
        binding.phoneNumber.addTextChangedListener(createNumericTextWatcher(binding.phoneNumber))
        binding.usernameTxt.addTextChangedListener(createLetterAndWhitespaceTextWatcher(binding.usernameTxt))
        binding.cityTxt.addTextChangedListener(createLetterAndWhitespaceTextWatcher(binding.cityTxt))

        binding.signUpBtn.setOnClickListener {

            val areNumericFieldsValid = areAllEditTextsValid(
                Pair(binding.idTxt, ::isNumeric),
                Pair(binding.phoneNumber, ::isNumeric))
            val areLetterFieldsValid = areAllEditTextsValid(
                Pair(binding.usernameTxt, ::isLetterOrWhitespace))


            if (areFieldsFilled()&& areNumericFieldsValid && areLetterFieldsValid && dateFlag) {
                if (passwordsMatch()) {
                    db.collection("Users")
                        .whereEqualTo("email", binding.emailTxt.text.toString())
                        .get()
                        .addOnSuccessListener { result ->
                            if (result.isEmpty) {
                                addNewUser(
                                    binding.usernameTxt.text.toString(),
                                    binding.emailTxt.text.toString(),
                                    binding.pass1Txt.text.toString(),
                                    binding.idTxt.text.toString(),
                                    binding.phoneNumber.text.toString()
                                )
                            } else {
                                Toast.makeText(requireContext(), "المستخدم موجود مسبقا!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Handle passwords not matching if needed
                }
            } else {
                Toast.makeText(requireContext(), "Please Fill all the needed Information", Toast.LENGTH_SHORT).show()
            }
        }
        binding.backBtn.setOnClickListener {
            loadFragment(LogInFragment())
        }



        binding.usernameTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // We don't need this method for now
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // We don't need this method for now
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.toString().any { it.isDigit() }) {
                    // If there's any numeric character, set the text color to red
                    binding.usernameTxt.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
                    Toast.makeText(requireContext(), "No numbers allowed!", Toast.LENGTH_SHORT).show()
                } else if (!s.isNullOrEmpty() && s.toString().all { it.isLetter() }) {
                    // If all characters are letters, set the text color to green
                    binding.usernameTxt.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
                } else {
                    // Otherwise, you can set to default color or handle other cases (like special characters)
                    binding.usernameTxt.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black)) // Example: Set to black
                }
            }
        })

        binding.idTxt.addTextChangedListener(createNumericTextWatcher(binding.idTxt))
        binding.phoneNumber.addTextChangedListener(createNumericTextWatcher(binding.phoneNumber))
// Add more if needed


        val btnSelectBirthday = binding.btnSelectBirthday
        val tvSelectedBirthday = binding.tvSelectedBirthday

        btnSelectBirthday.setOnClickListener {
            // Get current date
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Initialize date picker dialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val age = currentYear - selectedYear

                    if (age < 18) {
                        // Display a toast if age is less than 18
                        Toast.makeText(requireContext(), "You need to be over 18.", Toast.LENGTH_LONG).show()
                    } else {
                        birthday = Birthday(selectedYear, selectedMonth + 1, selectedDayOfMonth)
                        tvSelectedBirthday.text = "${birthday.day}/${birthday.month}/${birthday.year}"
                        dateFlag = true

                    }
                },
                currentYear, month, day
            )

            datePickerDialog.show()
        }



        return binding.root

    }

    private fun addNewUser(userName: String, email: String, password: String, id : String,phone :String) {
        fireBaseViewxModel.createUser(ClientModel(id, userName,
            email,password,binding.phoneNumber.text.toString(),ClientOrDriver,binding.cityTxt.text.toString(), birthday = birthday))
        runObserver()

    }
    private fun loadFragment(fragment: Fragment) {
        val bundle = Bundle()
        var supportFragmentManager = (activity as FragmentActivity).supportFragmentManager
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun runObserver(){
        fireBaseViewxModel.signUpstatues.observe(viewLifecycleOwner, Observer {
            signed -> if (signed){
                loadFragment(LogInFragment()) }
        })
    }

    private fun areFieldsFilled(): Boolean {
        return listOf(binding.idTxt, binding.emailTxt, binding.pass1Txt, binding.pass2Txt, binding.usernameTxt).all { it.text.isNotEmpty() }
    }
    private fun passwordsMatch(): Boolean {
        return binding.pass1Txt.text.toString() == binding.pass2Txt.text.toString()
    }

    fun createNumericTextWatcher(target: EditText): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !s.toString().all { it.isDigit() }) {
                    target.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
                    Toast.makeText(requireContext(), "Only numbers allowed!", Toast.LENGTH_SHORT).show()
                } else {
                    target.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
                }
            }
        }
    }
    fun createLetterAndWhitespaceTextWatcher(target: EditText): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !s.toString().all { it.isLetter() || it.isWhitespace() }) {
                    target.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
                    Toast.makeText(requireContext(), "Only letters and white spaces allowed!", Toast.LENGTH_SHORT).show()
                } else {
                    target.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
                }
            }
        }
    }
    fun isNumeric(char: Char): Boolean {
        return char.isDigit()
    }
    fun isLetterOrWhitespace(char: Char): Boolean {
        return char.isLetter() || char.isWhitespace()
    }
    fun areAllEditTextsValid(vararg editTexts: Pair<EditText, (Char) -> Boolean>): Boolean {
        return editTexts.all { (editText, predicate) -> editText.text.toString().all(predicate) }
    }





}