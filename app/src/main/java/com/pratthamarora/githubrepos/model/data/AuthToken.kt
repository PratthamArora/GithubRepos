package com.pratthamarora.githubrepos.model.data

import com.google.gson.annotations.SerializedName

data class AuthToken(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("token_type")
    val tokenType: String?
)