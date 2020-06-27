package com.pratthamarora.githubrepos.model

data class GithubRepo(
    val name: String?,
    val url: String?,
    val owner: GithubOwner
) {
    override fun toString(): String = "$name - $url"
}