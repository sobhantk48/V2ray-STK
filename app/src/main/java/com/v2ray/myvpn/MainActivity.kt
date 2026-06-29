package com.v2ray.myvpn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.v2ray.myvpn.ui.theme.V2raySTKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            V2raySTKTheme {
                Text("V2ray STK")
            }
        }
    }
}
