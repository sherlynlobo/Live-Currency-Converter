package com.sl.livecurrencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

                      }
                   catch (e:Exception)
                   {
                       Log.e("Main", "$e")
                   }
               }
            }
        }
    }
}
