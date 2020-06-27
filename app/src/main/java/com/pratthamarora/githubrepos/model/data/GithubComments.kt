package com.pratthamarora.githubrepos.model.data

data class GithubComments(
    val id: String?,
    val body: String?
) {
    override fun toString(): String = "$body - $id"
}