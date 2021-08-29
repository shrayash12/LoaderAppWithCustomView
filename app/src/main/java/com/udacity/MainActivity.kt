package com.udacity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var radioGlide: RadioButton
    private lateinit var radioRetrofit: RadioButton
    private lateinit var radioLoadApp: RadioButton
    private lateinit var downloadButton: LoadingButton

    private var downloadID: Long = 0
    private lateinit var pleaseSelectFileDownload: String
    private lateinit var downloadManager: DownloadManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        downloadButton = findViewById(R.id.downloadButton)
        radioGroup = findViewById(R.id.radioGroup)
        radioGlide = findViewById(R.id.radioGlide)
        radioRetrofit = findViewById(R.id.radioRetrofit)
        radioLoadApp = findViewById(R.id.radioLoadApp)

        setSupportActionBar(toolBar)
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        pleaseSelectFileDownload = getString(R.string.please_select_the_file_download);
        downloadButton.setOnClickListener {
            chooseRadioButton()
        }
    }

    private fun chooseRadioButton() {
        if (radioGroup.checkedRadioButtonId == -1) {
            showToastMessage()
        } else {
            when (radioGroup.checkedRadioButtonId) {
                radioGlide.id -> download(R.string.glide_txt, Constants.GLIDE_URL)
                radioRetrofit.id -> download(
                    R.string.loadApp_txt,
                    Constants.LOAD_APP_URL
                )
                radioLoadApp.id -> download(
                    R.string.retrofit_txt,
                    Constants.RETROFIT_URL
                )
            }
        }
    }

    private fun showToastMessage() {
        Toast.makeText(
            this,
            pleaseSelectFileDownload,
            Toast.LENGTH_LONG
        ).show()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                val query = DownloadManager.Query()
                query.setFilterById(id);
                val cursor: Cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val status: Int =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    val downloadTitle =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                    val downloadDescription =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))

                    val isSuccess = status == DownloadManager.STATUS_SUCCESSFUL
                    Toast.makeText(this@MainActivity, downloadTitle, Toast.LENGTH_LONG).show()
                    NotificationHelper.createNotificationChannel(
                        context,
                        downloadTitle,
                        isSuccess, downloadDescription
                    )
                }
                cursor.close()
            }
        }
    }

    private fun download(title: Int, url: String) {
        downloadButton.downloadStartLoading()

        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(title))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        downloadID =
            downloadManager.enqueue(request)

    }
}
