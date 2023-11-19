package com.dosbots.flixme.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import com.dosbots.flixme.R

fun Context.sendEmail(
    email: String,
    subject: String = "",
    body: String = ""
): Boolean {
    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    return try {
        startActivity(
            Intent.createChooser(emailIntent, getString(R.string.send_email_intent_chooser_title))
        )
        true
    } catch (ex: ActivityNotFoundException) {
        false
    }
}