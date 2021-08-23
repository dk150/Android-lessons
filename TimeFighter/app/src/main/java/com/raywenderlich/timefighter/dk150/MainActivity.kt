package com.raywenderlich.timefighter.dk150

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    internal lateinit var tapMeButton: Button
    private lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
        private const val GAME_STARTED_KEY = "GAME_STARTED"
    }

    private var score = 0
    private var highScore = 0
    private var gameStarted = false

    private lateinit var timer: CountDownTimer
    private val initialCountDownTime: Long = 10000
    private val countDownInterval: Long = 1000
    private var timeOut = false
    internal var timeLeftOnTimer: Long = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called. Score is: $score")

        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)

        if(savedInstanceState!=null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            gameStarted = savedInstanceState.getBoolean(GAME_STARTED_KEY)
            resumeGame()
        } else {
            resetGame()
        }

        // make button clickable
        tapMeButton.setOnClickListener {
            // start timer only on 1st click aka game start
            if(!gameStarted)
                startGame()
            // on every click increment score
            if(!timeOut)
                updateScore()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        outState.putBoolean(GAME_STARTED_KEY, gameStarted)
        timer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }

    private fun resetGame() {
        // initialize score & time value
        score = 0
        gameStarted = false
        timeOut = false
        gameScoreTextView.text = getString(R.string.your_score, score)
        timeLeftTextView.text = getString(R.string.your_time, initialCountDownTime/1000)
        timer=object : CountDownTimer(initialCountDownTime, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                if(millisUntilFinished.toInt()/1000==0) {
                    timeOut = true
                    tapMeButton.isClickable = false
                    tapMeButton.isEnabled = false
                }
                timeLeftOnTimer = millisUntilFinished
                timeLeftTextView.text = getString(R.string.your_time, millisUntilFinished/1000)
            }

            override fun onFinish() {
                endGame()
            }
        }
        tapMeButton.isClickable = true
        tapMeButton.isEnabled = true
    }

    private fun resumeGame() {
        gameScoreTextView.text = getString(R.string.your_score, score)
        var countDownTime: Long = if(timeLeftOnTimer/1000 == 0.toLong()){
            // last onTick executed
            if(gameStarted) {
                // onFinish not executed
                endGame()
                return
            } else {
                // onFinish executed
                initialCountDownTime
            }
        } else {
            timeLeftOnTimer
        }
        timeLeftTextView.text = getString(R.string.your_time, countDownTime/1000)
        timer=object : CountDownTimer(countDownTime, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                if(millisUntilFinished.toInt()/1000==0) {
                    timeOut = true
                    tapMeButton.isClickable = false
                    tapMeButton.isEnabled = false
                }
                timeLeftOnTimer = millisUntilFinished
                timeLeftTextView.text = getString(R.string.your_time, millisUntilFinished/1000)
            }

            override fun onFinish() {
                endGame()
            }
        }
        // resume timer only if game is in progress & last onTick has not been executed
        if(gameStarted)
            timer.start()
    }

    private fun startGame() {
        gameStarted = true
        timer.start()
    }

    private fun endGame() {
        // check if high score has been made
        val sP = getSharedPreferences("score", MODE_PRIVATE)
        highScore = sP.getInt("highScore", 0)
        // show result msg & update high score
        val resText: String = if(score > highScore) {
            sP.edit().putInt("highScore", score).apply()
            getString(R.string.high_score, score)
        } else {
            getString(R.string.no_score, score, highScore)
        }
        Toast.makeText(this, resText, Toast.LENGTH_LONG).show()
        resetGame()
    }

    private fun updateScore() {
        // increment score & update score View
        score++
        gameScoreTextView.text = getString(R.string.your_score, score)
    }
}
