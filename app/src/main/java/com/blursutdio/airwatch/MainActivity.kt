package com.blursutdio.airwatch

import android.location.Location
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.TextView
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import org.w3c.dom.Text

class MainActivity : WearableActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Enables Always-on
        setAmbientEnabled()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
            val lat = location.latitude.toString()
            val long = location.longitude.toString()
            fetchAir(lat, long)
        }
    }



    private fun fetchAir(lat:String, long:String) {
        val message = findViewById<TextView>(R.id.message)
        val value = findViewById<TextView>(R.id.value)
        val location = findViewById<TextView>(R.id.location);
        val httpAsync = "https://wave-air-quality.herokuapp.com/api/v1/air/?provider=airvisual"
        val requestBody = JSONObject()


        requestBody.put("lat", lat)
        requestBody.put("long", long)

        httpAsync.httpPost()
            .header("Content-type" to "Application/json")
            .body(requestBody.toString())
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex.toString())
                    }
                    is Result.Success -> {
                        val data = result.get()
                        val jsonResponse = JSONObject(data)
                        message.text = jsonResponse.getString("message")
                        value.text = jsonResponse.getString("aqi")
//                        location.text = jsonResponse.getString("location")
                    }
                }
            }.join()
    }


}
