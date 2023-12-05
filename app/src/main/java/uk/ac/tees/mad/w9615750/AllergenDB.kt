package uk.ac.tees.mad.w9615750

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.tees.mad.w9615750.dataclass.Allergen

class AllergenDB : AppCompatActivity() {

    private val databaseReference = FirebaseDatabase.getInstance().getReference("Allergens")

    private lateinit var spinnerSearchType: Spinner
    private lateinit var editTextQuery: EditText
    private lateinit var textViewResults: TextView
    private lateinit var buttonSearch: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergen_db)

        spinnerSearchType = findViewById(R.id.spinnerSearchType)
        editTextQuery = findViewById(R.id.editTextQuery)
        textViewResults = findViewById(R.id.textViewResults)

        buttonSearch = findViewById(R.id.buttonSearch)

        setupSpinner()
        setupSearchButton()





        databaseReference.limitToFirst(100).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allergens = snapshot.children.mapNotNull { it.getValue(Allergen::class.java) }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        })
    }

    private fun setupSearchButton() {
        buttonSearch.setOnClickListener {
            val query = editTextQuery.text.toString()
            if (query.isNotEmpty()) {
                displayAllergenDetails(query)
            } else {
                Toast.makeText(this, "Please enter a query", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinner() {
        val searchOptions = arrayOf("Search by Type", "Search by Food", "Search by Symptom")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, searchOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSearchType.adapter = adapter




        spinnerSearchType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                editTextQuery.visibility = View.VISIBLE
                editTextQuery.hint = "Enter ${searchOptions[position]}"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                editTextQuery.visibility = View.GONE
            }
        }
    }

    private fun displayAllergenDetails(query: String) {
        val searchType = spinnerSearchType.selectedItem.toString()

        when (searchType) {
            "Search by Type" -> queryAllergens("type", query)
            "Search by Food" -> queryAllergens("name", query)
            "Search by Symptom" -> queryAllergensSymptoms(query)
        }
    }

    private fun queryAllergens(field: String, query: String) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stringBuilder = StringBuilder()
                var found = false

                snapshot.children.forEach { dataSnapshot ->
                    val allergen = dataSnapshot.getValue(Allergen::class.java)
                    allergen?.let {
                        println(it.name)
                        val fieldValue = if (field == "type") it.type.toLowerCase() else it.name.toLowerCase()
                        if (fieldValue.contains(query)) {
                            stringBuilder.append("Name: ${it.name}, Type: ${it.type}\n")
                            stringBuilder.append("Symptoms: ${it.symptoms.joinToString { symptom -> symptom.description }}\n\n")
                            found = true
                        }
                    }
                }

                if (found) {
                    textViewResults.text = stringBuilder.toString()
                } else {
                    textViewResults.text = "No results found for \"$query\""
                }
            }

            override fun onCancelled(error: DatabaseError) {
                textViewResults.text = "Failed to read value."
            }
        })
    }

    private fun queryAllergensSymptoms(query: String) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stringBuilder = StringBuilder()
                var found = false
                val lowerCaseQuery = query.toLowerCase()

                snapshot.children.forEach { dataSnapshot ->
                    val allergen = dataSnapshot.getValue(Allergen::class.java)
                    allergen?.let {

                        if (it.symptoms.any { symptom -> symptom.description.toLowerCase().contains(lowerCaseQuery) }) {
                            stringBuilder.append("Name: ${it.name}, Type: ${it.type}\n")
                            stringBuilder.append("Symptoms: ${it.symptoms.joinToString { symptom -> symptom.description }}\n\n")
                            found = true
                        }
                    }
                }

                if (found) {
                    textViewResults.text = stringBuilder.toString()
                } else {
                    textViewResults.text = "No allergens found with symptom \"$query\""
                }
            }

            override fun onCancelled(error: DatabaseError) {
                textViewResults.text = "Failed to read value."
            }
        })
    }


    private fun displayAllergens(allergens: List<Allergen>) {
        val stringBuilder = StringBuilder()
        allergens.forEach { allergen ->
            stringBuilder.append("Name: ${allergen.name}, Type: ${allergen.type}\n")
            stringBuilder.append("Symptoms: ${allergen.symptoms.joinToString { symptom -> symptom.description }}\n\n")
        }
        textViewResults.text = stringBuilder.toString()
    }






}