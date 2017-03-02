package com.github.patrickds.androidexperimental.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.patrickds.androidexperimental.R
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val title = intent.getStringExtra("title")
        details_title.text = title
    }
}
