package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // get system use dark mode
        val modeButton = findViewById<Switch>(R.id.mode_switch)
        val switchModeLayout = findViewById<FrameLayout>(R.id.switch_layout)

        switchModeLayout.setOnClickListener {
            when (modeButton.isChecked) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    modeButton.isChecked = false
                }

                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    modeButton.isChecked = true
                }
            }
        }

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES ->
                modeButton.isChecked = true

            Configuration.UI_MODE_NIGHT_NO ->
                modeButton.isChecked = false
        }

        modeButton.setOnClickListener {
            modeButton.isChecked = !modeButton.isChecked
            when (modeButton.isChecked) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    modeButton.isChecked = false
                }

                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    modeButton.isChecked = true
                }
            }
        }

        val backButton = findViewById<ImageView>(R.id.back_button)
        val offerButton = findViewById<FrameLayout>(R.id.offer)
        val sendButton = findViewById<FrameLayout>(R.id.send)
        val shareButton = findViewById<FrameLayout>(R.id.share)

        backButton.setOnClickListener {
            finish()
        }

        offerButton.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.practicum_offer))
                startActivity(this)
            }
        }

        sendButton.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SENDTO
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.user_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))
                data = Uri.parse("mailto:")
                startActivity(this)
            }
        }

        shareButton.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                type = getString(R.string.share_type)
                startActivity(this)
            }

        }
    }

}