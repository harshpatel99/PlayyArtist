package com.twogentle.inartist.model

data class Poll(
    val pollId: String? = null,
    val postA: String? = null,
    val postB: String? = null,
    val urlA: String? = null,
    val urlB: String? = null,
    val voteA: Long? = null,
    val voteB: Long? = null,
    val userA: List<String>? = null,
    val userB: List<String>? = null,
    val pollDate: Long? = null,
    val postType: Int? = null
) {
}