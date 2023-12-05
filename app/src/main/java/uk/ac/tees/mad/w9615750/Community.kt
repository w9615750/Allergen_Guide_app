package uk.ac.tees.mad.w9615750

import NewPostFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.tees.mad.w9615750.dataclass.Post

class Community : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var postAdapter: PostAdapter
    private var postList = mutableListOf<Post>()
    private lateinit var forumRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

       findViewById<Button>(R.id.postButton).setOnClickListener {
            val newPostFragment = NewPostFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentLayout, newPostFragment)
                .addToBackStack(null)
                .commit()
        }

        database = FirebaseDatabase.getInstance().getReference("posts")

        forumRecyclerView = findViewById(R.id.forumRecyclerView)

        forumRecyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(postList)
        forumRecyclerView.adapter = postAdapter

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let { postList.add(it) }
                }

                postList.reverse()

                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        })
    }


}

