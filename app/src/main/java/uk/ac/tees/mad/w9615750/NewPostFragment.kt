import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import uk.ac.tees.mad.w9615750.R
import uk.ac.tees.mad.w9615750.dataclass.Post

class NewPostFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonSavePost = view.findViewById<Button>(R.id.buttonSavePost)


        buttonSavePost.setOnClickListener {
            val title = view.findViewById<EditText>(R.id.editTextPostTitle).text.toString().trim()
            val description = view.findViewById<EditText>(R.id.editTextPostDescription).text.toString().trim()
            if (title.isNotEmpty() && description.isNotEmpty()) {
                savePostToDatabase(title, description,view)
            } else {
                Toast.makeText(activity, "Please enter a title and description", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePostToDatabase(title: String, description: String,view: View) {
        val sharedPreferences = view.context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("Name", "User")

        val post = Post(userName, title, description, System.currentTimeMillis()) // Replace "Username" with actual user data
        val databaseReference = FirebaseDatabase.getInstance().getReference("posts")
        databaseReference.push().setValue(post).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(view.context,"Posted",Toast.LENGTH_SHORT).show()
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }
}
