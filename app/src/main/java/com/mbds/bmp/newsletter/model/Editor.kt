package com.mbds.bmp.newsletter.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.mbds.bmp.newsletter.BR
import java.io.Serializable

data class Editor(
    val id: String?,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
) : BaseObservable(), Serializable {
    var active = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.active)
        }
}