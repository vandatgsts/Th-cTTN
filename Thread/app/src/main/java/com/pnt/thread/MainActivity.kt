package com.pnt.thread


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pnt.thread.databinding.ActivityMainBinding
import java.lang.Thread.currentThread
import java.util.Random

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val DELAY = 80L
    }

    private lateinit var binding: ActivityMainBinding

    private var isIncrement = false
    val mHandler = Handler(Looper.getMainLooper())
    var mValue = 0
    private var bgThread = Thread()

    private var thread = Thread {
        mHandler.postDelayed(bgThread, 80)
    }

    private fun pauseThread() {
        //synchronized(this) {}
        mHandler.removeCallbacksAndMessages(null)
        currentThread().interrupt()
        if (!thread.isInterrupted) {
            thread.interrupt()
        }
    }

    private fun resumeThread() {
        thread = Thread(bgThread)
        mHandler.postDelayed({ thread.start() }, 1000)
    }

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mHandler2 = Handler(Looper.getMainLooper())
        //chang_color: 5s
        Thread(object : Runnable {
            override fun run() {
                mHandler2.postDelayed({
                    if (binding.tvCount.text.toString().toInt() != 0) {
                        val random = Random()
                        binding.tvCount.setTextColor(
                            Color.argb(
                                255, random.nextInt(256), random.nextInt(256),
                                random.nextInt(256)
                            )
                        )
                    }
                    mHandler2.post(this)
                }, 3200)
            }
        }).start()
        bgThread = Thread(object : Runnable {
            override fun run() {
                mHandler.post {
                    mValue = binding.tvCount.text.toString().toInt()
                    if (mValue > 0) {
                        mValue--
                    } else if (mValue < 0) mValue++
                    binding.tvCount.text = mValue.toString()
                }
                if (mValue != 0)
                    mHandler.postDelayed(this, DELAY)
                else mHandler.removeCallbacks(this)
            }
        })

        binding.btnDown.setOnClickListener {
            mValue--
            binding.tvCount.text = mValue.toString()
        }

        binding.btnUp.setOnClickListener {
            mValue++
            binding.tvCount.text = mValue.toString()
        }

        var a = 0

        binding.layoutTouch.setOnTouchListener { _, motionEvent ->
            Log.d(TAG, "onCreate: ${motionEvent.actionMasked}")
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    pauseThread()
                    a = motionEvent.y.toInt()
                }

                MotionEvent.ACTION_MOVE -> if (motionEvent.y >= a) {
                    mValue = binding.tvCount.text.toString().toInt() - 1
                    binding.tvCount.text = mValue.toString()
                } else {
                    mValue = binding.tvCount.text.toString().toInt() + 1
                    binding.tvCount.text = mValue.toString()
                }

                MotionEvent.ACTION_UP -> resumeThread()
            }
            true
        }
        val ruble = object : Runnable {
            override fun run() {
                if (isIncrement)
                    mValue++
                else mValue--
                binding.tvCount.text = mValue.toString()
                mHandler.postDelayed(this, DELAY)
            }

        }
        binding.btnUp.setOnTouchListener { _, motionEvent ->

            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    binding.btnUp.setBackgroundColor(Color.GREEN)
                    pauseThread()
                    isIncrement = true
                    mHandler.postDelayed(ruble, DELAY)
                }

                MotionEvent.ACTION_UP -> {
                    binding.btnUp.setBackgroundColor(
                        ContextCompat.getColor(
                            baseContext,
                            R.color.teal_200
                        )
                    )
                    isIncrement = false
                    mHandler.removeCallbacks(ruble)
                    resumeThread()
                }
            }
            false
        }

        binding.btnDown.setOnTouchListener { _, motionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    binding.btnDown.setBackgroundColor(Color.GREEN)
                    pauseThread()
                    isIncrement = false
                    mHandler.postDelayed(ruble, DELAY)
                }

                MotionEvent.ACTION_UP -> {
                    binding.btnDown.setBackgroundColor(
                        ContextCompat.getColor(
                            baseContext,
                            R.color.teal_200
                        )
                    )
                    isIncrement = true
                    mHandler.removeCallbacks(ruble)
                    resumeThread()
                }
            }
            false
        }
    }


}