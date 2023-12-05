package uk.ac.tees.mad.w9615750

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class ProductDetailsFragment : DialogFragment() {

    private var title: String? = null
    private var brand: String? = null
    private var ingredients: String? = null
    private var nutritionFacts: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("TITLE")
            brand = it.getString("BRAND")
            ingredients = it.getString("INGREDIENTS")
            nutritionFacts = it.getString("NUTRITION_FACTS")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.fragment_product_details, null)

            // Set your TextViews here
            view.findViewById<TextView>(R.id.textTitle).text = title
            view.findViewById<TextView>(R.id.textBrand).text = brand
            view.findViewById<TextView>(R.id.textIngredients).text = ingredients
            view.findViewById<TextView>(R.id.textNutritionFacts).text = nutritionFacts

            builder.setView(view)
                // Add action buttons if needed
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    // User clicked OK button
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, brand: String, ingredients: String, nutritionFacts: String) =
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("TITLE", title)
                    putString("BRAND", brand)
                    putString("INGREDIENTS", ingredients)
                    putString("NUTRITION_FACTS", nutritionFacts)
                }
            }
    }
}
