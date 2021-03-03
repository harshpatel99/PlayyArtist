package com.twogentle.inartist.model

data class Artist(
    var id: String? = null,
    var name: String? = null,
    var username: String? = null,
    var bio: String? = null,
    var profilePic: String? = null,
    var social: HashMap<String, String>? = null,
    var totalPosts : Int? = null,
    var collection : String? = null
) {
}