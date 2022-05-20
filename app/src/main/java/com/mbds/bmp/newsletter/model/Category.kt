package com.mbds.bmp.newsletter.model

import androidx.annotation.StringRes
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.mbds.bmp.newsletter.BR
import java.io.Serializable

data class Category(@StringRes val nameId: Int, val key: String?, val image: String) :
    BaseObservable(), Serializable {
    var active: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.active)
        }
}