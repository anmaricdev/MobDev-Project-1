package dec.hex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.lang.Exception
import kotlin.math.pow
import kotlin.math.withSign

class MainActivity : AppCompatActivity() {
    // tag for logging
    private val TAG = "HexToDec"

    // for controlling UI elements for easier access
    private lateinit var taskView: TextView
    private lateinit var hexInput: EditText

    // dialog when result is correct
    private lateinit var dialog: AlertDialog.Builder

    // 0, ..., 3 for EditText on reading direction
    private var checkStep: Int = 0

    // the onchange listener for all input fields
    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (checkStep == 0 && hexInput.hasFocus() && s.toString() == hexCorrect) {
                //checkStep = 1
                dialog.show()
                //continue with next task
                createTask()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    // for task and result check
    private var number: Float = 0f
    private var hexCorrect: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle("Good job!")
        dialog.setMessage("That was correct!")
        dialog.setCancelable(true)
        dialog.setIcon(R.drawable.checkmark)

        //connect to UI elements
        taskView = findViewById(R.id.task)
        hexInput = findViewById(R.id.hex)

        // add onchange listeners
        hexInput.addTextChangedListener(textWatcher)

        //create first task
        createTask()
    }

    private fun createTask() {
        //clean input fields
        hexInput.setText("")

        //create new task, first create random number
        val minExp = -4
        val maxExp = 5
        val parts = List(maxExp - minExp + 1) { 2f.pow(maxExp - it) }
        Log.v(TAG, "List with values: " + parts.toString())
        val randVal = MutableList(maxExp - minExp + 2) { (0..1).random() }
        Log.v(TAG, "Random values: " + randVal.toString())
        number = 0f
        for (i in 0..(maxExp - minExp)) {
            number += parts[i] * randVal[i]
        }
// section for testing: just for one case: 0f and check whether it is changed
        // number = 0f // ok for -0.5, -0.125, 2, 8, -32, 0.0625
// end section for testing
        // check whether number is still zero, if so change it
        if (number == 0f) { //attention: in general it is not safe to use == for comparison of floats!
            val randNum = (0..(maxExp - minExp)).random()
            randVal[randNum] = 1
            number = parts[randNum]
        }
        // finalize number calculation (sign)
        if (randVal[maxExp - minExp + 1] == 1) {
            number = -number
        }
// section for testing: set other numbers here
        //number = 63.9375f // ok
        //number = -63.9375f // ok
        //number = 1f // ok
        //number = -1f // ok
        //number = 0.5f // ok
        //number = -0.5f // ok
        number = -14.5f // ok
        //number = -0.0625f // ok
        //number = -34.125f // ok
        //number = 55.5f // ok
        //number = -3.6875f // ok
        //number = 22.0625f // ok
// end section for testing
        // now calculate correct solutions, based on decimal to hexadecimal conversion
        /* if (number >= 0) {
             hexCorrect = "+1"
         }
         else {
             hexCorrect = "-1"
         }
        */
        Log.v(TAG, "Number: " + number.toString())
        try {
            //hexCorrect = Integer.toHexString(number)
            hexCorrect = (String.format("%x", number))
            Log.v(TAG, "Hexadecimal representation: " + hexCorrect)
        } catch (exception : Exception){println(exception.toString())}


        // put new task to UI
        taskView.text = number.toString()

        // start checks with first step
        checkStep = 0

        // set focus on first input field
        hexInput.requestFocus()
    }
}