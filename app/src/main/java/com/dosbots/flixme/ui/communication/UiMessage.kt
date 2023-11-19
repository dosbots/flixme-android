package com.dosbots.flixme.ui.communication

import android.content.Context
import androidx.annotation.StringRes

class UiMessage {

    private var _message: String? = null

    @StringRes
    private var _messageResource: Int? = null

    constructor(message: String) {
        this._message = message
    }

    constructor(@StringRes message: Int) {
        this._messageResource = message
    }

    fun get(context: Context): String {
        return _messageResource?.let { stringRes -> context.getString(stringRes) } ?: _message.orEmpty()
    }
}
