package com.anno.processor.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anno.processor.java.HelloWorld

@HelloWorld
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

}
