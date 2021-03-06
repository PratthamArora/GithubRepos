package com.pratthamarora.githubrepos.model.data

import com.google.gson.annotations.SerializedName

data class GithubPR(
    val id: String?,
    val title: String?,
    val number: String?,
    @SerializedName("comments_url")
    val comments: String?,
    val user: GithubOwner?
) {
    override fun toString(): String = "$title - ${user?.login}"
}