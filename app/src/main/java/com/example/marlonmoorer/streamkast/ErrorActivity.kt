package com.example.marlonmoorer.streamkast

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        this.intent.run {
            var message= getStringExtra("ERROR")
            error.text=message
        }
    }
}
