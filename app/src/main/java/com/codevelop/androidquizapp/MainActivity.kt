package com.codevelop.androidquizapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.codevelop.androidquizapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val questions = QuizData.questions
    private val options = QuizData.options
    private val correctAnswers = QuizData.correctAnswers

    private var currentQuestionIndex = 0
    private var score = 0

    private var timer : CountDownTimer? = null

    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("username") ?: "Guest"
        // Display the username and current question number
        binding.txtUserName.text = "Welcome, $username"

        displayQuestion()

        binding.btnOption1.setOnClickListener {
            handleOptionClick(0)
        }
        binding.btnOption2.setOnClickListener {
            handleOptionClick(1)
        }
        binding.btnOption3.setOnClickListener {
            handleOptionClick(2)
        }
        binding.btnRestart.setOnClickListener {
            restartQuiz()
        }
        binding.btnSubmit.setOnClickListener {
            val intent = Intent(this, UserInfoActivity::class.java)
            startActivity(intent)
        }
        startTimer()
    }

    private fun startTimer(){
        timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                //val progress = ((15000 - millisUntilFinished) / 15000f) * 100
                binding.progressBar.progress = secondsRemaining.toInt()
                updateCountdownText(secondsRemaining.toInt())
            }

            override fun onFinish() {
                showCorrectAnswer()
            }
        }.start()
    }

    private fun updateCountdownText(secondsRemaining: Int) {
        // Update the countdown text based on the remaining time
        // Example: Set the text of a TextView inside the circular progress bar
        val countdownTextView: TextView = findViewById(R.id.countdownTextView)
        countdownTextView.text = secondsRemaining.toString()
    }

    private fun handleOptionClick(selectedAnswerIndex: Int) {
        timer?.cancel() // Cancel the timer when the user selects an option
        checkAnswer(selectedAnswerIndex)
    }

    private fun showCorrectAnswer() {
        val correctAnswerIndex = correctAnswers[currentQuestionIndex]
        correctButtonColors(correctAnswerIndex)

        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            binding.txtQuestion.postDelayed({ displayQuestion() }, 1000)
            startTimer() // Start the timer for the next question
        } else {
            showResult()
        }
    }

    private fun correctButtonColors(buttonIndex : Int){
        when(buttonIndex){
            0 -> binding.btnOption1.setBackgroundColor(Color.GREEN)
            1 -> binding.btnOption2.setBackgroundColor(Color.GREEN)
            2 -> binding.btnOption3.setBackgroundColor(Color.GREEN)
        }
    }

    private fun wrongButtonColors(buttonIndex : Int){
        when(buttonIndex){
            0 -> binding.btnOption1.setBackgroundColor(Color.RED)
            1 -> binding.btnOption2.setBackgroundColor(Color.RED)
            2 -> binding.btnOption3.setBackgroundColor(Color.RED)
        }
    }

    private fun resetButtonColor(){
        binding.btnOption1.setBackgroundColor(Color.rgb(50, 59, 96))
        binding.btnOption2.setBackgroundColor(Color.rgb(50, 59, 96))
        binding.btnOption3.setBackgroundColor(Color.rgb(50, 59, 96))
    }

    private fun showResult(){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_custom_layout, null)
        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()
        val btnOk = dialogView.findViewById<Button>(R.id.btnOk)

        // Set the actual score in the TextView
        val scoreTextView = dialogView.findViewById<TextView>(R.id.txtScore)
        scoreTextView.text = "Your Score: $score Out Of ${questions.size}"

        btnOk.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()

        binding.btnRestart.isEnabled = true
        binding.btnSubmit.isEnabled = true
    }

    private fun displayQuestion(){
        binding.txtQuestion.text = questions[currentQuestionIndex]
        binding.btnOption1.text = options[currentQuestionIndex][0]
        binding.btnOption2.text = options[currentQuestionIndex][1]
        binding.btnOption3.text = options[currentQuestionIndex][2]
        resetButtonColor()

        val questionNumber = currentQuestionIndex + 1
        binding.txtQuestionNumber.text = "Question: $questionNumber / ${questions.size}"
    }

    private fun checkAnswer(selectedAnswerIndex : Int){
        val correctAnswerIndex = correctAnswers[currentQuestionIndex]

        if(selectedAnswerIndex == correctAnswerIndex){
            score++
        }
        else{
            wrongButtonColors(selectedAnswerIndex)
        }
        correctButtonColors(correctAnswerIndex)

        if(currentQuestionIndex <questions.size - 1 ){
            currentQuestionIndex++
            binding.txtQuestion.postDelayed({displayQuestion()}, 1000)
            startTimer() // Start the timer for the next question
        }
        else{
            showResult()
        }
    }

    private fun restartQuiz(){
        currentQuestionIndex = 0
        score = 0
        displayQuestion()
        binding.btnRestart.isEnabled = false
        binding.btnSubmit.isEnabled = false
        startTimer()
    }
}