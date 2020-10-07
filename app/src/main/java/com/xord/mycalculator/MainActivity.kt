package com.xord.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onNumBtnClicked(view: View){

        val btnSelected = view as Button
        var textViewText: String= textView.text.toString()

        when(btnSelected.id){
            btn0.id->{ textViewText += "0" }
            btn1.id->{ textViewText += "1" }
            btn2.id->{ textViewText += "2" }
            btn3.id->{ textViewText += "3" }
            btn4.id->{ textViewText += "4" }
            btn5.id->{ textViewText += "5" }
            btn6.id->{ textViewText += "6" }
            btn7.id->{ textViewText += "7" }
            btn8.id->{ textViewText += "8" }
            btn9.id->{ textViewText += "9" }
            btnDot.id->{
                if(!(textViewText.contains("."))) textViewText += "."
            }
            btnPlusMinus.id-> {
                if (!(textViewText.contains("-")))
                    textViewText = "-" + textViewText
                else{
                    var str: String = textViewText
                    val re = "[-]".toRegex()
                    str = re.replace(str," ")
                    textViewText =str
                }

            }
        }
        textView.text = textViewText

    }


    fun onOpBtnClicked(view: View){
        val btnSelected:Button = view as Button
        var textViewText: String = textView.text.toString()

        var ln:Int = textViewText.length
        var ch:Char = textViewText[ln-1]

        if(ch!='+' && ch!='-' && ch!='*' && ch!='/' ) {
            when (btnSelected.id) {

                btnAdd.id -> {
                    textViewText += " + "
                }
                btnSub.id -> {
                    textViewText += " - "
                }
                btnMul.id -> {
                    textViewText += " * "
                }
                btnDiv.id -> {
                    textViewText += " / "
                }
            }
        }else{
            var subStr: String = textViewText.substring(0,ln-2)

            when (btnSelected.id) {

                btnAdd.id -> {
                    subStr += " + "
                }
                btnSub.id -> {
                    subStr += " - "
                }
                btnMul.id -> {
                    subStr += " * "
                }
                btnDiv.id -> {
                    subStr += " / "
                }

            }
            textViewText = subStr
        }
        textView.text = textViewText

        

    }

    fun onEqualBtnClicked(view: View){
        var txtViewText = textView.text.toString()
        var res: Double = evaluate(txtViewText)
        textView.text = res.toString()


    }

    fun onPerBtnClicked(view: View){
        val num = textView.text.toString().toDouble()/100
        textView.text = num.toString()

    }

    fun onACBtnClicked(view: View){
        textView.setText("")

    }

    fun evaluate(expression: String): Double {
        val tokens = expression.toCharArray()

        // Stack for numbers: 'values'
        val values: Stack<Double> = Stack<Double>()

        // Stack for Operators: 'ops'
        val ops: Stack<Char> = Stack<Char>()
        var i = 0
        while (i < tokens.size) {

            // Current token is a whitespace, skip it
            if (tokens[i] == ' ') {
                i++
                continue
            }

            // Current token is a number, push it to stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] =='.') {
                val sbuf = StringBuffer()
                // There may be more than one digits in number
                while (i < tokens.size && (tokens[i] >= '0' && tokens[i] <= '9'|| tokens[i] =='.') )sbuf.append(tokens[i++])
                values.push(sbuf.toString().toDouble())
            } else if (tokens[i] == '(') ops.push(tokens[i]) else if (tokens[i] == ')') {
                while (ops.peek() !== '(') values.push(applyOp(ops.pop(), values.pop(), values.pop()))
                ops.pop()
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) values.push(applyOp(ops.pop(), values.pop(), values.pop()))

                // Push current token to 'ops'.
                ops.push(tokens[i])
            }
            i++
        }

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty()) values.push(applyOp(ops.pop(), values.pop(), values.pop()))

        // Top of 'values' contains result, return it
        return values.pop()
    }

    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') return false
        return if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) false else true
    }

    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    fun applyOp(op: Char, b: Double, a: Double): Double {
        when (op) {
            '+' -> return a + b
            '-' -> return a - b
            '*' -> return a * b
            '/' -> {
                if (b == 0.0) throw UnsupportedOperationException("Cannot divide by zero")
                return a / b
            }
        }
        return 0.0
    }

}


