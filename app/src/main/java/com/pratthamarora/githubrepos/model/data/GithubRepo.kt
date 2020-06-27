package com.pratthamarora.githubrepos.model.data

import com.pratthamarora.githubrepos.model.data.GithubOwner

data class GithubRepo(
    val name: String?,
    val url: String?,
    val owner: GithubOwner
) {
    override fun toString(): String = "$name - $url"
}