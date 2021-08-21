package com.raywenderlich.timefighter.dk150

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate

class MainActivity : AppCompatActivity() {
    private var score = 0
    private var time = 0
    private var inProgress = false
    private var highScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetGame()
    }

    private fun resetGame() {
        score = 0
        time = resources.getInteger(R.integer.time)
        findViewById<TextView>(R.id.gameScoreTextView).text = getString(R.string.your_score, score)
        findViewById<TextView>(R.id.timeLeftTextView).text = getString(R.string.your_time, time)
        findViewById<TextView>(R.id.resultMsgTextView).visibility = View.INVISIBLE
        inProgress = false
        findViewById<Button>(R.id.tapMeButton).setOnClickListener {
            if(!inProgress)
                startTimer()
            updateScore()
        }
    }

    private fun startTimer() {
        inProgress = true
        val timer=Timer("timer", false)
        val mainHandler = Handler(mainLooper)
        val updateTime = Runnable {
            findViewById<TextView>(R.id.timeLeftTextView).text = getString(R.string.your_time, time)
        }
        val newGame = Runnable {
            resetGame()
        }
        val finishGame = Runnable {
            findViewById<Button>(R.id.tapMeButton).setOnClickListener(null)
            val result = findViewById<TextView>(R.id.resultMsgTextView)
            val sP = getSharedPreferences("score", MODE_PRIVATE)
            highScore = sP.getInt("highScore", 0)
            val resText: String = if(score > highScore) {
                sP.edit().putInt("highScore", score).apply()
                getString(R.string.high_score, score)
            } else {
                getString(R.string.no_score, score, highScore)
            }
            result.text = resText
            result.visibility = View.VISIBLE
            Timer("main", false).schedule(3000) {
                mainHandler.post(newGame)
            }
        }
        timer.scheduleAtFixedRate(0, 1000) {
            if(time-1==0) {
                timer.cancel()
                mainHandler.post(finishGame)
            }
            time--
            mainHandler.post(updateTime)
        }
    }

    private fun updateScore() {
        score++
        findViewById<TextView>(R.id.gameScoreTextView).text = getString(R.string.your_score, score)
    }
}
