package com.example.reseau_permissions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var networkReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.textNetworkStatus)

        // BroadcastReceiver pour surveiller les changements réseau
        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                updateNetworkStatus()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        updateNetworkStatus()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }

    private fun updateNetworkStatus() {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(activeNetwork)

        val statusText = when {
            capabilities == null -> "Aucun réseau disponible ❌"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                "Connecté en Wi-Fi ✅"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                "Connecté en Données Mobiles 📱"
            else -> "Connecté à un autre type de réseau 🌐"
        }

        statusTextView.text = statusText
    }
}
