package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var textInput: TextView
    var lastNumeric: Boolean = false
    var stateError: Boolean = false
    var lastDot: Boolean = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        textInput = findViewById(R.id.texInput)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onDigit(view: View) {
        if (stateError) {
            textInput.text = (view as Button).text
            stateError = false
        } else {
            textInput.append((view as Button).text)
        }
        lastNumeric = true
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            textInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        if (lastNumeric && !stateError) {
            textInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    fun onClear(view: View) {
        this.textInput.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    fun onEqual(view: View) {
        if (lastNumeric && !stateError) {
            val text = textInput.text.toString()
            try {
                val expression = net.objecthunter.exp4j.ExpressionBuilder(text).build()
                val result = expression.evaluate()
                textInput.text = result.toString()
                lastDot = true
            } catch (ex: ArithmeticException) {
                textInput.text = "Error"
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
                stateError = true
                lastNumeric = false
            }
        }
    }
}