package com.raywenderlich.timefighter.dk150

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    private var score = 0
    private var time = 0
    private var inProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetGame()

    }

    private fun resetGame() {
        score = 0
        time = 5
        findViewById<TextView>(R.id.score).text = getString(R.string.score, score)
        findViewById<TextView>(R.id.time).text = getString(R.string.time, time)
        findViewById<TextView>(R.id.result).visibility = View.INVISIBLE
        inProgress = false
        findViewById<Button>(R.id.button).setOnClickListener {
            if(!inProgress)
                startTimer()
            updateScore()
        }
    }

    private fun startTimer() {
        inProgress = true
        var timer=Timer("timer", false)
        var mainHandler = Handler(mainLooper)
        var updateTime = Runnable() {
            findViewById<TextView>(R.id.time).text = getString(R.string.time, time)
        }
        var newGame = Runnable() {
            resetGame()
        }
        var finishGame = Runnable() {
            findViewById<Button>(R.id.button).setOnClickListener(null)
            var result = findViewById<TextView>(R.id.result);
            result.text = getString(R.string.result, score)
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
        findViewById<TextView>(R.id.score).text = getString(R.string.score, score)
    }
}
