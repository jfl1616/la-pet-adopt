package com.joseapps.lapetadopt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.joseapps.lapetadopt.ui.navigation.LaPetAdoptNavHost
import com.joseapps.lapetadopt.ui.theme.LaPetAdoptTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as LaPetAdoptApp).container

        setContent {
            LaPetAdoptTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    LaPetAdoptNavHost(container = container)
                }
            }
        }
    }
}
