package com.mbds.bmp.newsletter.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.jwang123.flagkit.FlagKit
import com.mbds.bmp.newsletter.BR
import com.mbds.bmp.newsletter.R
import com.neovisionaries.i18n.CountryCode
import java.io.Serializable
import java.util.*

data class Country(val countryCode: CountryCode?) : BaseObservable(), Serializable {
    var active = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.active)
        }

    fun getName(context: Context) =
        countryCode?.getName() ?: context.getString(R.string.all)

    fun getFlag(context: Context): Drawable {

        val drawable = countryCode?.name?.let {
            FlagKit.drawableWithFlag(context, it.toLowerCase(Locale.ROOT))
        }

        return (drawable ?: context.getDrawable(R.drawable.world)) as Drawable
    }
}