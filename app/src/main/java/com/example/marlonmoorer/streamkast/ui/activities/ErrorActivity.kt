package com.example.marlonmoorer.streamkast.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.marlonmoorer.streamkast.R
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        this.intent.run {
            val message= getStringExtra("ERROR")
            error.text=message
        }
    }
}
