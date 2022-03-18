package com.adam.io.mycalculator


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adam.io.mycalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.roundToLong


class MainActivity : AppCompatActivity() {

    // Represent whether the lastly pressed key is numeric or not
    private var lastNumeric: Boolean = false

    // Represent that current state is in error or not
    private var stateError: Boolean = false

    // Represent whether the inputText is empty or not
    private var isEmpty: Boolean = true

    // If true, do not allow to add another DOT
    private var lastDot: Boolean = false

    // if true , do not allow to  add new percent operator
    private var lastPercent: Boolean = false

    // If true , there is a right parenthesis
    private var rightPar: Boolean = false

    // If true , there is a left parenthesis
    private var leftPar: Boolean = false

    private var exp: String = ""

    // operator exist

    private var lastOperator: Boolean = false

    private lateinit var inputText: TextView
    private lateinit var outputText: TextView

    private var leftParenthesisCounter = 0
    private var rightParenthesisCounter = 0

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        inputText = binding.formula
        outputText = binding.result

        //Numbers buttons

        binding.btnZero.setOnClickListener {
            onDigit(binding.btnZero.text.toString())
        }
        binding.btnOne.setOnClickListener {
            onDigit(binding.btnOne.text.toString())
        }
        binding.btnTwo.setOnClickListener {
            onDigit(binding.btnTwo.text.toString())
        }
        binding.btnThree.setOnClickListener {
            onDigit(binding.btnThree.text.toString())
        }
        binding.btnFour.setOnClickListener {
            onDigit(binding.btnFour.text.toString())
        }
        binding.btnFive.setOnClickListener {
            onDigit(binding.btnFive.text.toString())
        }
        binding.btnSix.setOnClickListener {
            onDigit(binding.btnSix.text.toString())
        }
        binding.btnSeven.setOnClickListener {
            onDigit(binding.btnSeven.text.toString())
        }
        binding.btnEight.setOnClickListener {
            onDigit(binding.btnEight.text.toString())
        }
        binding.btnNine.setOnClickListener {
            onDigit(binding.btnNine.text.toString())
        }
        // decimal dot

        binding.btnDot.setOnClickListener {
            onDecimalPoint()
        }

        // plus operator

        binding.btnPlus.setOnClickListener {
            onOperator(binding.btnPlus.text.toString())
        }

        //Minus operator
        binding.btnMinus.setOnClickListener {
            onOperator(binding.btnMinus.text.toString())
        }

        //Div operator
        binding.btnDiv.setOnClickListener {
            onOperator(binding.btnDiv.text.toString())
        }

        //Multi operator
        binding.btnMultiply.setOnClickListener {
            onOperator(binding.btnMultiply.text.toString())
        }

        //Left par
        binding.btnLeftPar.setOnClickListener {
            onLeftParenthesis(binding.btnLeftPar.text.toString())
        }

        //Right par
        binding.btnRightPar.setOnClickListener {
            onRightParenthesis(binding.btnRightPar.text.toString())
        }

        //Equal
        binding.btnEqual.setOnClickListener {
            onEqual()
        }

        //Clear btn
        binding.btnClear.setOnClickListener {
            onClear()
        }
        // Delete btn
        binding.btnDelete.setOnClickListener {
            onDelete()
        }
        // Percent btn
        binding.btnPercent.setOnClickListener {
            onPercent(binding.btnPercent.text.toString())
        }
        // PlusOrMinus btn
        binding.btnPlusOrMinus.setOnClickListener {
            onPlusOrMinus()
        }
    }


    /**
     * Append the Button.text to the TextView
     */


    private fun onDigit(numberText: String) {
        when {
            stateError -> {
                // If current state is Error, replace the error message
                inputText.text = (numberText)
                exp = numberText
                stateError = false
            }
            lastPercent -> {
                Toast.makeText(this, "non valid format used", Toast.LENGTH_SHORT).show()
            }
            rightPar -> {
                inputText.append("*$numberText")
                exp += "*$numberText"
            }
            else -> {
                inputText.append(numberText)
                exp += numberText
            }
        }
        // Set the flag
        lastNumeric = true
        lastDot = false
        lastOperator = false
        leftPar = false
        rightPar = false
        isEmpty = false
        lastPercent = false
    }


    /**
     * Append . to the TextView
     */

    private fun onDecimalPoint() {
        when {
            lastNumeric -> {
                inputText.append(",")
                exp += "."

                lastNumeric = false
                lastDot = true
                leftPar = false
                rightPar = false
                lastOperator = false
                isEmpty = false
                lastPercent = false

            }
            rightPar -> {
                inputText.append("*(0,")
                exp += "*(0."
                leftParenthesisCounter++

                lastNumeric = false
                lastDot = true
                leftPar = false
                rightPar = false
                lastOperator = false
                isEmpty = false
                lastPercent = false

            }
            lastDot || lastPercent -> {

                Toast.makeText(this, "non valid format used", Toast.LENGTH_SHORT).show()

            }
            else -> {
                inputText.append("0,")
                exp += "0."

                lastNumeric = false
                lastDot = true
                leftPar = false
                rightPar = false
                lastOperator = false
                isEmpty = false
                lastPercent = false
            }
        }
    }


    /**
     * Append +,-,*,/ operators to the TextView
     */
    private fun onOperator(operatorType: String) {
        if (lastOperator || isEmpty || leftPar) {
            Toast.makeText(this, "non valid format used", Toast.LENGTH_SHORT).show()
        } else if (operatorType == "x") {
            inputText.append("*")
            exp += "*"

            lastNumeric = false
            lastDot = false
            lastOperator = true
            leftPar = false
            rightPar = false
            isEmpty = false
            lastPercent = false

        } else {

            inputText.append((operatorType))
            exp += operatorType

            lastNumeric = false
            lastDot = false
            lastOperator = true
            leftPar = false
            rightPar = false
            isEmpty = false
            lastPercent = false

        }
    }

    /**
     * Append ) to the TextView
     */

    private fun onRightParenthesis(rightParenthesis: String) {
        if (isEmpty || leftPar || lastOperator || lastDot) {
            Toast.makeText(this, "non valid format used", Toast.LENGTH_SHORT).show()
        } else {
            inputText.append((rightParenthesis))
            exp += rightParenthesis
            rightParenthesisCounter++
            isEmpty = false
            lastNumeric = false
            lastDot = false
            rightPar = true
            leftPar = false
            lastOperator = false
            lastPercent = false

        }
    }

    /**
     * Append ( to the TextView
     */

    private fun onLeftParenthesis(leftParenthesis: String) {
        if (lastNumeric || rightPar || lastPercent) {
            inputText.append("*(")
            exp += "*("
            leftParenthesisCounter++
            leftPar = true
            rightPar = false
            lastDot = false
            lastNumeric = false
            lastOperator = false
            isEmpty = false
            lastPercent = false
        } else if (lastDot) {
            Toast.makeText(this, "non valid format used", Toast.LENGTH_SHORT).show()
        } else {
            inputText.append(leftParenthesis)
            exp += leftParenthesis
            leftParenthesisCounter++
            leftPar = true
            rightPar = false
            lastDot = false
            lastNumeric = false
            lastOperator = false
            isEmpty = false
            lastPercent = false
        }
    }

    /**
     * Plus or Minus Button fun
     */
    private fun onPlusOrMinus() {
        if (lastNumeric || rightPar || lastPercent || lastPercent) {
            inputText.append("*(-")
            exp += "*(-"
            leftParenthesisCounter++
            leftPar = false
            rightPar = false
            lastDot = false
            lastNumeric = false
            lastOperator = true
            isEmpty = false
            lastPercent = false
        } else if (lastDot) {
            Toast.makeText(this, "non valid format used", Toast.LENGTH_SHORT).show()
        } else {
            inputText.append("(-")
            exp += "(-"
            leftParenthesisCounter++
            leftPar = false
            rightPar = false
            lastDot = false
            lastNumeric = false
            lastOperator = true
            isEmpty = false
            lastPercent = false
        }
    }

    /**
     * Percent button fun
     */
    private fun onPercent(percent: String) {
        if (lastNumeric) {
            inputText.append(percent)
            exp += ("/100")
            leftPar = false
            rightPar = false
            lastDot = false
            lastNumeric = false
            lastOperator = false
            isEmpty = false
            lastPercent = true
        } else {
            Toast.makeText(this, "non valid format used", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * Clear the TextView
     */
    private fun onClear() {
        inputText.text = ""
        outputText.text = ""
        exp = ""
        leftParenthesisCounter = 0
        rightParenthesisCounter = 0
        lastNumeric = false
        stateError = false
        lastDot = false
        leftPar = false
        rightPar = false
        lastOperator = false
        isEmpty = true
    }

    /**
     * To delete last input
     */
    private fun onDelete() {
        val operatorList = listOf("+", "-", "*", "/")
        val numberList = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val lastInput: String // if error set it to var
        when {
            isEmpty -> {
                Toast.makeText(this, "non valid format used", Toast.LENGTH_SHORT).show()
            }
            inputText.text.length == 1 -> {
                onClear()
            }
            lastPercent -> {
                inputText.text = inputText.text.substring(0, inputText.text.length - 1)
                exp = exp.substring(0, exp.length - 4)

                lastNumeric = true
                lastDot = false
                lastOperator = true
                leftPar = false
                rightPar = false
                isEmpty = false
                lastPercent = false

            }
            rightPar -> {
                inputText.text = inputText.text.substring(0, inputText.text.length - 1)
                exp = exp.substring(0, exp.length - 1)
                lastInput =
                    inputText.text.subSequence(inputText.text.length - 1, inputText.text.length)
                        .toString()
                rightParenthesisCounter--

                when {
                    lastInput == "%" -> {
                        leftPar = false
                        rightPar = false
                        lastDot = false
                        lastNumeric = false
                        lastOperator = false
                        isEmpty = false
                        lastPercent = true

                    }
                    numberList.contains(lastInput) -> {
                        lastNumeric = true
                        lastDot = false
                        lastOperator = false
                        leftPar = false
                        rightPar = false
                        isEmpty = false
                        lastPercent = false

                    }
                    lastInput == ")" -> {
                        leftPar = false
                        rightPar = true
                        lastDot = false
                        lastNumeric = false
                        lastOperator = false
                        isEmpty = false
                        lastPercent = false
                    }

                }

            }
            leftPar -> {
                inputText.text = inputText.text.substring(0, inputText.text.length - 1)
                exp = exp.substring(0, exp.length - 1)
                lastInput =
                    inputText.text.subSequence(inputText.text.length - 1, inputText.text.length)
                        .toString()
                leftParenthesisCounter--
                when {
                    lastInput == "(" -> {
                        leftPar = true
                        rightPar = false
                        lastDot = false
                        lastNumeric = false
                        lastOperator = false
                        isEmpty = false
                        lastPercent = false

                    }
                    operatorList.contains(lastInput) -> {
                        lastNumeric = false
                        lastDot = false
                        lastOperator = true
                        leftPar = false
                        rightPar = false
                        isEmpty = false
                        lastPercent = false

                    }
                }
            }
            else -> {
                inputText.text = inputText.text.substring(0, inputText.text.length - 1)
                exp = exp.substring(0, exp.length - 1)
                lastInput =
                    inputText.text.subSequence(inputText.text.length - 1, inputText.text.length)
                        .toString()

                when {
                    operatorList.contains(lastInput) -> {
                        lastNumeric = false
                        lastDot = false
                        lastOperator = true
                        leftPar = false
                        rightPar = false
                        isEmpty = false
                        lastPercent = false

                    }
                    numberList.contains(lastInput) -> {
                        lastNumeric = true
                        lastDot = false
                        lastOperator = false
                        leftPar = false
                        rightPar = false
                        isEmpty = false
                        lastPercent = false

                    }
                    lastInput == "." -> {
                        lastNumeric = false
                        lastDot = true
                        leftPar = false
                        rightPar = false
                        lastOperator = false
                        isEmpty = false
                        lastPercent = false
                    }
                    lastInput == "(" -> {
                        leftPar = true
                        rightPar = false
                        lastDot = false
                        lastNumeric = false
                        lastOperator = false
                        isEmpty = false
                        lastPercent = false

                    }
                    lastInput == ")" -> {
                        leftPar = false
                        rightPar = true
                        lastDot = false
                        lastNumeric = false
                        lastOperator = false
                        isEmpty = false
                        lastPercent = false
                    }
                    lastInput == "%" -> {
                        leftPar = false
                        rightPar = false
                        lastDot = false
                        lastNumeric = false
                        lastOperator = false
                        isEmpty = false
                        lastPercent = true
                    }
                }
            }
        }
    }

    /**
     * checks if the number of left and right parenthesis is equal
     */
    private fun parenthesesCounter() = (leftParenthesisCounter != rightParenthesisCounter)

    /**
     * Calculate the output using Exp4j
     */

    @SuppressLint("SetTextI18n")
    private fun onEqual() {
        // If the current state is error, nothing to do.
        // If the last input is a number only, solution can be found.
        if (isEmpty || leftPar || lastOperator || parenthesesCounter()) {
            Toast.makeText(this, "non valid format used", Toast.LENGTH_SHORT).show()
        } else {
            // Read the expression
            ///
            // Create an Expression (A class from exp4j library)
            val expression = ExpressionBuilder(exp).build()


            try {
                Log.d("onEqual", "check point 2")
                // Calculate the result and display
                val result = expression.evaluate()
                val roundedResult = result.roundToLong()
                if (roundedResult.toDouble() == result) {
                    outputText.text = roundedResult.toString()
                } else {
                    outputText.text = result.toString()
                    lastDot = true // Result contains a dot
                }

            } catch (ex: ArithmeticException) {
                // Display an error message
                outputText.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }

    }

}



