package com.udacity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.udacity.Constants.DESCRIPTION
import com.udacity.Constants.SUCCESS
import com.udacity.Constants.TITLE

class DetailActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var tvStatus: TextView
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        findViews()
        setSupportActionBar(toolbar)

        if (intent.hasExtra(SUCCESS))
            if (intent.getBooleanExtra(SUCCESS, false)) {
                tvStatus.text = getString(R.string.success_txt)
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

            } else {
                tvStatus.text = getString(R.string.fail_txt)
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
        if (intent.hasExtra(TITLE)) {
            tvName.visibility = View.VISIBLE
            tvName.text = intent.getStringExtra(TITLE)
        }

        if (intent.hasExtra(DESCRIPTION)) {
            tvDescription.text = intent.getStringExtra(DESCRIPTION)
        }

        buttonBack.setOnClickListener {
            this.finish()
        }
    }

    private fun findViews() {
        toolbar = findViewById(R.id.toolbar)
        tvStatus = findViewById(R.id.tvStatus)
        tvName = findViewById(R.id.tvName)
        tvDescription = findViewById(R.id.tvDescription)
        buttonBack = findViewById(R.id.buttonBack)
    }
}
