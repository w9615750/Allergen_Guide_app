package uk.ac.tees.mad.w9615750

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.ac.tees.mad.w9615750.dataclass.Post


class PostAdapter(private val postList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleTextView: TextView = view.findViewById(R.id.titleTextView)
        var contentTextView: TextView = view.findViewById(R.id.contentTextView)
        var authorTextView: TextView = view.findViewById(R.id.authorTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_list, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.titleTextView.text = post.title
        holder.contentTextView.text = post.content
        holder.authorTextView.text = post.author
        // Set other views as needed
    }

    override fun getItemCount() = postList.size
}
