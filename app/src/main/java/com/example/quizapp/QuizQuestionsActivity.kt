package com.example.quizapp

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var currentPosition: Int = 1
    private var questionsList: ArrayList<Question>? = null
    private var selectedOptionPosition: Int = 0
    private var correctAnswersCount: Int = 0
    private var userName: String? = null
    private var clickedFlag: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        userName = intent.getStringExtra(Constants.USER_NAME)

        questionsList = Constants.getQuestions()
        setQuestion()

        textViewOption1.setOnClickListener(this)
        textViewOption2.setOnClickListener(this)
        textViewOption3.setOnClickListener(this)
        textViewOption4.setOnClickListener(this)
        buttonSubmit.setOnClickListener(this)

    }

    private fun setQuestion(){
        //currentPosition = 1
        clickedFlag = true
        val question = questionsList!![currentPosition-1]

        defaultOptionsView()

        if (currentPosition == questionsList!!.size) {
            buttonSubmit.text = "FINISH"
        } else {
            buttonSubmit.text = "SUBMIT"
        }

        progressBar.progress = currentPosition
        progressCount.text = "${currentPosition}/${progressBar.max}"

        textViewQuestion.text = question.question
        imageViewFlag.setImageResource(question.image)
        textViewOption1.text = question.option1
        textViewOption2.text = question.option2
        textViewOption3.text = question.option3
        textViewOption4.text = question.option4
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()

        options.add(0, textViewOption1)
        options.add(0, textViewOption2)
        options.add(0, textViewOption3)
        options.add(0, textViewOption4)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }

    }

    override fun onClick(v: View?) {
        if (clickedFlag) {
            when (v?.id) {
                R.id.textViewOption1 -> {
                    selectedOptionsView(textViewOption1, 1)
                }
                R.id.textViewOption2 -> {
                    selectedOptionsView(textViewOption2, 2)
                }
                R.id.textViewOption3 -> {
                    selectedOptionsView(textViewOption3, 3)
                }
                R.id.textViewOption4 -> {
                    selectedOptionsView(textViewOption4, 4)
                }
            }
        }

        if (v?.id == R.id.buttonSubmit) {
            if (selectedOptionPosition == 0) {
                currentPosition++
                when {
                    currentPosition <= questionsList!!.size -> {
                        setQuestion()
                    }
                    else -> {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(Constants.USER_NAME, userName)
                        intent.putExtra(Constants.CORRECT_ANSWERS, correctAnswersCount)
                        intent.putExtra(Constants.TOTAL_QUESTIONS, questionsList!!.size)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                val question = questionsList!!.get(currentPosition - 1)
                if (question.correctAnswer != selectedOptionPosition) {
                    answerView(
                        selectedOptionPosition,
                        R.drawable.wrong_option_border_bg
                    )
                } else {
                    correctAnswersCount++
                }
                answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                if (currentPosition == questionsList!!.size) {
                    buttonSubmit.text = "FINISH"
                } else {
                    buttonSubmit.text = "GO TO NEXT QUESTION"
                }
                clickedFlag = false
                selectedOptionPosition = 0
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when(answer) {
            1 -> {
                textViewOption1.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                textViewOption2.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                textViewOption3.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                textViewOption4.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun selectedOptionsView(textView: TextView, selectedOptionNum: Int){
        defaultOptionsView()
        selectedOptionPosition = selectedOptionNum

        textView.setTextColor(Color.parseColor("#363A43"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }
}