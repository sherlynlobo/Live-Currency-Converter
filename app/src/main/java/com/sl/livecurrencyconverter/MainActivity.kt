package com.sl.livecurrencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    var baseCurrency = "EUR"
    var convertedToCurrency = "USD"
    var conversionRate = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerSetup()

    }
    private fun getApiResult()
    {
        if(et_firstConversion != null && et_firstConversion.text.isNotEmpty() && et_firstConversion.text.isNotBlank())
        {
           val API = "https://api.ratesapi.io/api/latest?base=$baseCurrency&symbols=$convertedToCurrency"
            if (baseCurrency == convertedToCurrency)
            {
                Toast.makeText(applicationContext, "Cannot convert the same currency", Toast.LENGTH_SHORT).show()
            }
            else
            {
               GlobalScope.launch(Dispatchers.IO)
               {
                   try{
                       val apiResult = URL(API).readText()
                       val jsonObject = JSONObject(apiResult)

                       conversionRate = jsonObject.getJSONObject("rates").getString(convertedToCurrency).toFloat()

                       Log.d("Main", "$conversionRate")
                       Log.d("Main", apiResult)

                       withContext(Dispatchers.Main)
                       {
                           val text = ((et_firstConversion.text.toString().toFloat()) * conversionRate).toString()
                           et_secondConversion?.setText(text)
                       }

                      }
                   catch (e:Exception)
                   {
                       Log.e("Main", "$e")
                   }
               }
            }
        }
    }

    private fun spinnerSetup()
    {
      val spinner: Spinner = findViewById(R.id.spinner_firstConversion)
        val spinner2: Spinner = findViewById(R.id.spinner_secondConversion)

        ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.currencies2,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter
        }

        spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               baseCurrency = parent?.getItemAtPosition(position).toString()
                getApiResult()
            }

        })

        spinner2.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                convertedToCurrency = parent?.getItemAtPosition(position).toString()
                getApiResult()
            }

        })

    }
}
