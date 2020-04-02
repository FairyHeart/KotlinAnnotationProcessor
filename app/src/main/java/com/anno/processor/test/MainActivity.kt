package com.anno.processor.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lib.anno.BindField

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    @BindField(viewIds = ["tv_name", "tv_age"], viewName = "view")
    fun bindView() {

    }
}
