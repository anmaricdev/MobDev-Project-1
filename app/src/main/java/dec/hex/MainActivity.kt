package dec.hex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    // tag for logging
    private val TAG = "HexToDec"

    // for controlling UI elements for easier access
    private lateinit var taskView: TextView
    private lateinit var hexInput: EditText

    // dialog when result is correct
    private lateinit var dialog: AlertDialog.Builder


    // the onchange listener for all input fields
    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (hexInput.hasFocus() && s.toString() == hexCorrect) {
                dialog.show()
                // continue with next task
                createTask()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    // for task and result check
    private var number: Int = 0
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
        val minExp = 0
        val maxExp = 1
        val parts = List(maxExp - minExp + 1) { 16f.pow(maxExp-it) }
        Log.v(TAG, "List with values: " + parts.toString())
        val randVal = MutableList(maxExp - minExp + 2) { (0..15).random() }
        Log.v(TAG, "Random values: " + randVal.toString())
        number = 0
        for (i in 0..(maxExp - minExp)) {
            number += parts[i].toInt() * randVal[i]
        }
// section for testing: just for one case: 0f and check whether it is changed
        //number = 0f // ok
// end section for testing
        // check whether number is still zero, if so change it
        // attention: in general it is not safe to use == for comparison of floats!
        if (number == 0) {
            val randNum = (0..(maxExp - minExp)).random()
            randVal[randNum] = 1
            number = parts[randNum].toInt()
        }
// section for testing
        //number = 127 // ok
        //number = 5 // ok
        //number = 9 // ok
        //number = 1 // ok
        //number = 69 // ok
        //number = 11 // ok
        //number = 123 // ok
        //number = 231 // ok
        //number = 255 // ok
        //number = 3 // ok
        //number = 222 // ok
        //number = 111 // ok
// end section for testing

        // now calculate correct solutions, based on decimal to hexadecimal conversion
        // convert number to a HexString
        Log.v(TAG, "Number: " + number.toString())
        hexCorrect = (String.format("%X", number))
        Log.v(TAG, "Hexadecimal representation: " + hexCorrect)


        // put new task to UI
        taskView.text = number.toString()

        // set focus on first input field
        hexInput.requestFocus()
    }
}