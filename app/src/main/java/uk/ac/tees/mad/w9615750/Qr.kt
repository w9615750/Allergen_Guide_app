package uk.ac.tees.mad.w9615750

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.PixelCopy.Request
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONObject

class Qr : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result.contents != null) {
            fetchProductDetails(result.contents)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun fetchProductDetails(barcode: String) {

        val url = "https://api.barcodelookup.com/v3/products?barcode=$barcode&formatted=y&key=hyps86o37k76wodlah6p5ys0wthypn"

        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            com.android.volley.Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val productsArray = jsonObject.getJSONArray("products")
                    if (productsArray.length() > 0) {

                        val product = productsArray.getJSONObject(0)
                        val title = product.getString("title")
                        val brand = product.getString("brand")
                        val ingredients = product.optString("ingredients")
                        val nutritionFacts = product.optString("nutrition_facts")

                        // Extracting the first image URL
                        val imagesArray = product.getJSONArray("images")
                        val firstImageUrl = if (imagesArray.length() > 0) imagesArray.getString(0) else "No Image"



                        Toast.makeText(applicationContext,product.toString(),Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, QrViewActivity::class.java).apply {
                            putExtra("EXTRA_TITLE", title)
                            putExtra("EXTRA_BRAND", brand)
                            putExtra("EXTRA_INGREDIENTS", ingredients)
                            putExtra("EXTRA_NUTRITION_FACTS", nutritionFacts)
                            putExtra("EXTRA", firstImageUrl)
                        }
                        startActivity(intent)
                        finish()

                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.printStackTrace().toString(),Toast.LENGTH_SHORT).show()

                }
            },
            { error -> error.printStackTrace() }
        )

        queue.add(stringRequest)
    }
}