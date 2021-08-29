package com.udacity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.Constants.DESCRIPTION
import com.udacity.Constants.SUCCESS
import com.udacity.Constants.TITLE
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        if (intent.hasExtra(SUCCESS))
            if (intent.getBooleanExtra(SUCCESS, false)) {
                status.text = getString(R.string.success)
                status.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

            } else {
                status.text = getString(R.string.fail)
                status.setTextColor(ContextCompat.getColor(this, R.color.Red))
            }
        if (intent.hasExtra(TITLE)) {
            name.visibility = View.VISIBLE
            name.text = intent.getStringExtra(TITLE)
        }

        if (intent.hasExtra(DESCRIPTION)) {
            description.text = intent.getStringExtra(DESCRIPTION)
        }

        btn_back.setOnClickListener {
            this.finish()
        }
    }
}
