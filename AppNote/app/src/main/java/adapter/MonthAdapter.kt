package adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_solar.AddNoteActivity
import com.example.noteapp_solar.databinding.ItemDayBinding
import `object`.Day


class MonthAdapter(private val dataList: MutableList<Day>, var listTime: MutableList<String>) :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {
    var month = 0
    var year = 0
    var day = ""
    lateinit var context: Context
    lateinit var onItemClick: (index: Int) -> Unit
    var check = 0

    lateinit var binding: ItemDayBinding

//    fun getListTimeNote() {
//        listTime.clear()
//        val noteDatabase = NoteDatabase(context)
//        val noteList = noteDatabase.getAllNote()
//        for (item in noteList) {
//            if (listTime.indexOf(item.timeNote) == -1) {
//                listTime.add(item.timeNote)
//            }
//        }
//    }


    fun setItemClick(event: (index: Int) -> Unit) {
        onItemClick = event
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(color: String) {
            val item = dataList[adapterPosition]
            binding.tvDay.text = item.value
            binding.tvDay.setTextColor(Color.parseColor(color))

            if (adapterPosition > 6 && dataList[adapterPosition].isCurrentMonth) {
                val s =
                    dataList[adapterPosition].value + "/" + month + "/" + year //time in calendarview
                if (listTime.indexOf(s) != -1) {//compare database to set background
                    itemView.setBackgroundColor(Color.parseColor("#F4A460"))
                }
            }

            if (dataList[adapterPosition].isClick) {
                if (adapterPosition > 6)
                    if (check == 1) {
//                        itemView.setBackgroundColor(Color.parseColor("#7FFFD4"))
                    } else if (check > 1) { //doubleclick
                        val intent = Intent(context, AddNoteActivity::class.java)

//                        if (!dataList[adapterPosition].isCurrentMonth && adapterPosition < 20) {// not currentMonth
//                            month--
//                        } else month++
                        intent.putExtra("dayNote", day.toInt())
                        intent.putExtra("monthNote", month)
                        intent.putExtra("yearNote", year)
                        ContextCompat.startActivities(
                            context,
                            arrayOf(intent)
                        )
                    }

            } else {
//                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
//                val share = context.getSharedPreferences("date", Context.MODE_PRIVATE)
//                if (month == share.getInt("month", 0) && year == share.getInt("year", 0)
//                    && adapterPosition > 6
//                    && dataList[adapterPosition].value.toInt() == share.getInt("value", 0)
//                    && dataList[adapterPosition].isCurrentMonth == share.getBoolean(
//                        "isCurrentMonth",
//                        false
//                    )
//                ) {//set clicked
//                    Log.d(TAG, "bindData: $adapterPosition - ${share.getInt("index", 0)}")
//                    itemView.setBackgroundColor(Color.parseColor("#7FFFD4"))
//                }
            }

        }

        init {
            itemView.setOnClickListener {
                onItemClick.invoke(adapterPosition)
//                indexCLick = adapterPosition
            }

            itemView.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    check++
                    Handler(Looper.getMainLooper()).postDelayed({
                        check = 0
                    }, 500)
                }
                false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position <= 6 || dataList[position].isCurrentMonth) {
            holder.bindData("#000000")//currentMonth or title
        } else holder.bindData("#C0C0C0")//previousMonth

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}