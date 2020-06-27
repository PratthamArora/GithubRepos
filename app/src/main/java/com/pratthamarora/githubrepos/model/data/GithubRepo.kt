package com.pratthamarora.githubrepos.model.data

data class GithubRepo(
    val name: String?,
    val url: String?,
    val owner: GithubOwner
) {
    override fun toString(): String = "$name - ${owner.login}"
}