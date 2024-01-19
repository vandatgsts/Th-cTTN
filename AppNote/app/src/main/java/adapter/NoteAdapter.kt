package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_solar.Note
import com.example.noteapp_solar.databinding.ItemNoteLayoutBinding

class NoteAdapter(var noteList: MutableList<Note>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var onCLickItem: ((Note) -> Unit)? = null
    lateinit var binding: ItemNoteLayoutBinding

    fun setOnClickItem(callback: (Note) -> Unit) {
        onCLickItem = callback
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(note: Note) {
            binding.tvTitleNote.text = note.title
            binding.tvTimeNote.text = note.timeNote
            binding.tvContentNote.text = note.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemNoteLayoutBinding.inflate(LayoutInflater.from(parent.context))

//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_note_layout, parent, false)

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(noteList[position])
        holder.itemView.setOnClickListener { onCLickItem?.invoke(noteList[position]) }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}