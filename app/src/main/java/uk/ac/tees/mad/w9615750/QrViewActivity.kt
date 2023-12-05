package uk.ac.tees.mad.w9615750

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.tees.mad.w9615750.dataclass.Allergen

class QrViewActivity : AppCompatActivity() {

    lateinit  var ingredients : String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_view)

        val title = intent.getStringExtra("EXTRA_TITLE") ?: "N/A"
        val brand = intent.getStringExtra("EXTRA_BRAND") ?: "N/A"
        ingredients = intent.getStringExtra("EXTRA_INGREDIENTS") ?: "N/A"
        val nutritionFacts = intent.getStringExtra("EXTRA_NUTRITION_FACTS") ?: "N/A"
        val extra = intent.getStringExtra("EXTRA") ?: "N/A"



        findViewById<TextView>(R.id.textTitle).text = title
        findViewById<TextView>(R.id.textBrand).text = brand
        findViewById<TextView>(R.id.textIngredients).text = ingredients

        Glide.with(this)
            .load(extra)
            .into(findViewById(R.id.imageTop))

        queryAllergens("Food")

    }

    private fun queryAllergens(field: String) {

       val databaseReference = FirebaseDatabase.getInstance().getReference("Allergens")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stringBuilder = StringBuilder()
                var found = false

                snapshot.children.forEach { dataSnapshot ->
                    val allergen = dataSnapshot.getValue(Allergen::class.java)
                    allergen?.let {
                        val fieldValue = if (field == "type") it.type.toLowerCase() else it.name.toLowerCase()
                        if (ingredients.toLowerCase().contains(fieldValue)) {
                            stringBuilder.append("Name: ${it.name}, Type: ${it.type}\n")
                            stringBuilder.append("Symptoms: ${it.symptoms.joinToString { symptom -> symptom.description }}\n\n")
                            found = true
                        }
                    }
                }

                if (found) {
                    findViewById<TextView>(R.id.textNutritionFacts).text = stringBuilder.toString()
                } else {
                    findViewById<TextView>(R.id.textNutritionFacts).text = "No Allergies found"
                }
            }

            override fun onCancelled(error: DatabaseError) {

                findViewById<TextView>(R.id.textNutritionFacts).text = "Failed to read value."
            }
        })
    }

    override fun onBackPressed() {
        finish()

        super.onBackPressed()
    }
}