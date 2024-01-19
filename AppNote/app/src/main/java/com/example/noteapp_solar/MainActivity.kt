package com.example.noteapp_solar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.noteapp_solar.databinding.ActivityMainBinding
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    val adapterViewPager = AdapterViewPager(supportFragmentManager, 1)
    val listTime: MutableList<String> = ArrayList()

    lateinit var binding: ActivityMainBinding

    private fun getListTimeNote() {
        listTime.clear()
        val noteDatabase = NoteDatabase(this)
        val noteList = noteDatabase.getAllNote()
        for (item in noteList) {
            if (listTime.indexOf(item.timeNote) == -1) {
                listTime.add(item.timeNote)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init listTime in database
        getListTimeNote()

        val times = LocalDateTime.now()
        binding.tvTitle.text = "Tháng ${times.month.value} năm ${times.year}"
        adapterViewPager.setView(times, "CN", adapterViewPager, listTime)
        binding.viewPager.adapter = adapterViewPager
        binding.viewPager.currentItem = 1
        var currentItem = 0
        var startDay = ""
        binding.btnChangeDay.setOnClickListener {
            val arrItems = arrayOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật")
            fun setAdapter() {
                val dateTime = adapterViewPager.fragmentList[1].time
                adapterViewPager.updateFragment(dateTime, startDay, listTime)
                Toast.makeText(this, "Lịch đã được cập nhật lại!", Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog.Builder? =
                AlertDialog.Builder(this).setTitle("Chọn ngày bắt đầu của tháng")
                    .setItems(arrItems) { _, position ->
                        startDay = when (position) {
                            0 -> "T2"
                            1 -> "T3"
                            2 -> "T4"
                            3 -> "T5"
                            4 -> "T6"
                            5 -> "T7"
                            else -> "CN"
                        }
                        setAdapter()
                    }
            dialog?.show()
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentItem = position
                val dateTime = adapterViewPager.fragmentList[currentItem].time
                binding.tvTitle.text = "Tháng ${dateTime.month.value} năm ${dateTime.year}"
            }

            override fun onPageScrollStateChanged(state: Int) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    val start = adapterViewPager.fragmentList[1].startDay
                    var dateTime = adapterViewPager.fragmentList[1].time
                    if (currentItem > 1) {//next month
                        dateTime = dateTime.plusMonths(1)
                    } else if (currentItem < 1) { //previous month
                        dateTime = dateTime.minusMonths(1)
                    }
                    binding.tvTitle.text = "Tháng ${dateTime.month.value} năm ${dateTime.year}"
                    adapterViewPager.updateFragment(dateTime, start, listTime)
                    binding.viewPager.setCurrentItem(1, false)
                    binding.viewPager.offscreenPageLimit = 2
                }
            }
        })

        binding.btnAddNoteCalendar.setOnClickListener {}

        binding.btnAddNoteCalendar.setOnClickListener { //set to add note
            val intentAdd = Intent(this, AddNoteActivity::class.java)
            val day = times.dayOfMonth
            val month = times.month.value
            val year = times.year
            intentAdd.putExtra("dayNote", day)
            intentAdd.putExtra("monthNote", month)
            intentAdd.putExtra("yearNote", year)
            startActivity(intentAdd)
        }

        binding.btnViewNotes.setOnClickListener {
            startActivity(Intent(this, NoteHomeScreen::class.java))
        }
    }

    class AdapterViewPager(manager: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(manager, behavior) {
        val fragmentList: MutableList<MonthFragment> = ArrayList()
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun setView(
            time: LocalDateTime,
            startDay: String,
            adapterViewPager: AdapterViewPager,
            listTime: MutableList<String>
        ) {

            adapterViewPager.fragmentList.add(
                MonthFragment().newInstance(time.minusMonths(1).withDayOfMonth(1), startDay)
            )
            adapterViewPager.fragmentList.add(MonthFragment().newInstance(time, startDay))
            adapterViewPager.fragmentList.add(
                MonthFragment().newInstance(time.plusMonths(1).withDayOfMonth(1), startDay)
            )
            fragmentList[0].listTime = listTime
            fragmentList[1].listTime = listTime
            fragmentList[2].listTime = listTime
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun updateFragment(time: LocalDateTime, startDay: String, listTime: MutableList<String>) {
            val previousMonth = time.minusMonths(1)
            val nextMonth = time.plusMonths(1)
            fragmentList[0].time = previousMonth
            fragmentList[1].time = time
            fragmentList[2].time = nextMonth
            fragmentList[0].startDay = startDay
            fragmentList[1].startDay = startDay
            fragmentList[2].startDay = startDay
            fragmentList[0].listTime = listTime
            fragmentList[1].listTime = listTime
            fragmentList[2].listTime = listTime
            fragmentList[0].initDataList(startDay)
            fragmentList[1].initDataList(startDay)
            fragmentList[2].initDataList(startDay)
        }
    }
}