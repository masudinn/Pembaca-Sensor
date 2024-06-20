package com.instiper.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserModel (
    var uid : String? = null,
    var username : String? = null,
    var alamat : String? = null,
    var email : String? = null
):Parcelable