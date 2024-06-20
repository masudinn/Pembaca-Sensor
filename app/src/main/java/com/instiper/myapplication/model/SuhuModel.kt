package com.instiper.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class SuhuModel (
    var suhu : Float? = null,
    var ph: String? = null,
    var tds : String? = null,
    var jam : String? = null,
    var tanggal : String? = null
    ):Parcelable





