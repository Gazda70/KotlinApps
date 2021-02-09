package pl.polsl.tm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlin.Exception

const val REALIS = "pl.polsl.tm.REALIS"
const val IMAGINARIS = "pl.polsl.tm.IMAGINARIS"

class CalculatorActivity() : Activity(), AdapterView.OnItemSelectedListener {

    var realResult: Double? = 0.0

    var imgResult: Double? = 0.0

    var resultString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

      var add:Button = this.findViewById(R.id.buttonAdd)
        var subtract:Button = this.findViewById(R.id.buttonSubtract)

      add.setOnClickListener {
          add()
      }

        subtract.setOnClickListener {
            subtract()
        }

        val mySpinner: BetterSpinner = this.findViewById(R.id.betterSpinner) as BetterSpinner

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.operations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            mySpinner.adapter   = adapter
        }
        mySpinner.setSelection( Adapter.NO_SELECTION, false)
        mySpinner.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
            if(parent.getItemAtPosition(pos) == "+"){
                add()
            }else if(parent.getItemAtPosition(pos) == "-"){
                subtract()
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    private fun add(){
        if(createResultAdd())
        {
            startGraphActivityFromIntent()
        }
        displayInfoToast()
    }

    private fun subtract(){
        if(createResultSubtract())
        {
            startGraphActivityFromIntent()
        }
        displayInfoToast()
    }

    private fun addRealis():Boolean{
        var toReturn = false
        val fieldRealA:TextView = this.findViewById(R.id.realisA)
        val fieldRealB:TextView = this.findViewById(R.id.realisB)

        if(fieldRealA.text.isNotEmpty() && fieldRealB.text.isNotEmpty())
        {
            try {
                if (!(fieldRealA.text.toString().toDouble().isNaN() ||
                            fieldRealB.text.toString().toDouble().isNaN())
                ) {
                    realResult =
                        fieldRealA.text.toString().toDouble() + fieldRealB.text.toString()
                            .toDouble()
                    toReturn = true
                }
            }catch(e:NumberFormatException){

            }
        }

        return toReturn
    }

    private fun addImaginaris():Boolean{
        var toReturn = false
        val fieldImgA:TextView = this.findViewById(R.id.imaginarisA)
        val fieldImgB:TextView = this.findViewById(R.id.imaginarisB)

        if(fieldImgA.text.isNotEmpty() && fieldImgB.text.isNotEmpty())
        {
            try{
                if(!(fieldImgA.text.toString().toDouble().isNaN() ||
                   fieldImgB.text.toString().toDouble().isNaN())) {
                    imgResult =
                        fieldImgA.text.toString().toDouble() + fieldImgB.text.toString().toDouble()
                    toReturn = true
                }
            }catch(e:NumberFormatException){

            }
        }

        return toReturn
    }

    private fun subtractRealis():Boolean{
        var toReturn = false
        val fieldRealA:TextView = this.findViewById(R.id.realisA)
        val fieldRealB:TextView = this.findViewById(R.id.realisB)

        if(fieldRealA.text.isNotEmpty() && fieldRealB.text.isNotEmpty())
        {
            try {
                if (!(fieldRealA.text.toString().toDouble().isNaN() ||
                            fieldRealB.text.toString().toDouble().isNaN())
                ) {
                    realResult =
                        fieldRealA.text.toString().toDouble() - fieldRealB.text.toString()
                            .toDouble()
                    toReturn = true
                }
            }catch(e:NumberFormatException){

            }
        }

        return toReturn
    }

    private fun subtractImaginaris():Boolean{
        var toReturn = false
        val fieldImgA:TextView = this.findViewById(R.id.imaginarisA)
        val fieldImgB:TextView = this.findViewById(R.id.imaginarisB)

        if(fieldImgA.text.isNotEmpty() && fieldImgB.text.isNotEmpty())
        {
            try {
                if (!(fieldImgA.text.toString().toDouble().isNaN() ||
                            fieldImgB.text.toString().toDouble().isNaN())
                ) {
                    imgResult =
                        fieldImgA.text.toString().toDouble() - fieldImgB.text.toString().toDouble()
                    toReturn = true
                }
            }catch(e:NumberFormatException){

            }
        }

        return toReturn
    }

    private fun displayInfoToast(){
            Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show()
    }

    private fun createResultAdd():Boolean{
        var toReturn = true
      if(addRealis() && addImaginaris()){
            resultString = if(imgResult!! < 0.0){
                "$realResult${imgResult}i"
            }else{
                "$realResult+${imgResult}i"
            }
      }else{
            toReturn = false
            resultString = "Nie podałeś odpowiednich wartości !"
        }

        return toReturn
    }

    private fun createResultSubtract():Boolean{
        var toReturn = true
        if(subtractRealis() && subtractImaginaris()){

            resultString = if(imgResult!! < 0.0){
                "$realResult${imgResult}i"
            }else{
                "$realResult+${imgResult}i"
            }
        }else{
            toReturn = false
            resultString = "Nie podałeś odpowiednich wartości !"
        }
        return toReturn
    }

    private fun startGraphActivityFromIntent(){
        val seeGraph:Intent = Intent(this, GraphActivity::class.java).apply {
            putExtra(REALIS, realResult)
            putExtra(IMAGINARIS, imgResult)
        }
        startActivity(seeGraph)
    }

}