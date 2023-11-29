package com.dosbots.flixme.ui.communication

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import java.util.UUID

class UiMessage private constructor() : Parcelable {

    private val id: String = UUID.randomUUID().toString()

    private var _message: String? = null

    @StringRes
    private var _messageResource: Int? = null

    constructor(message: String) : this() {
        this._message = message
    }

    constructor(@StringRes message: Int) : this() {
        this._messageResource = message
    }

    fun get(context: Context): String {
        return _messageResource?.let { stringRes -> context.getString(stringRes) } ?: _message.orEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is UiMessage) {
            return false
        }
        return this.id == other.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (_message?.hashCode() ?: 0)
        result = 31 * result + (_messageResource ?: 0)
        return result
    }

    constructor(parcel: Parcel) : this() {
        _message = parcel.readString()
        _messageResource = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_message)
        parcel.writeValue(_messageResource)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UiMessage> {
        override fun createFromParcel(parcel: Parcel): UiMessage {
            return UiMessage(parcel)
        }

        override fun newArray(size: Int): Array<UiMessage?> {
            return arrayOfNulls(size)
        }
    }
}
