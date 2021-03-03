package com.twogentle.inartist.model

data class ComicArtist(
    var id: String? = null,
    var name: String? = null,
    var social: HashMap<String, String>? = null,
    var totalPosts: Int? = null
) {
}