package com.twogentle.inartist.model

import java.io.Serializable

data class Comic(
    var postType: Int? = null,
    var title: String? = null,
    var id: String? = null,
    var urls: List<String>? = null,
    var datePosted: Long? = null,
    var views: Int? = null,
    var likes: Int? = null,
    var shares: Int? = null,
    var artistID: String? = null,
    var artistName: String? = null,
    var artistSocial: HashMap<String, String>? = null,
    var category: String? = null,
    var collection: String? = null,
    var price: String? = null,
    var userLiked: List<String>? = null,
    var userUnlocked: List<String>? = null,
    var contentType: String? = null,
    var totalUnlocked: Int? = null,
    var thumbnail:String? = null,
    var date:Long? = null,
    var instagramUrl:String?=null
) : Serializable {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_COMIC = 1
        const val TYPE_DETAILS = 2
        const val TYPE_COMIC_LOCKED = 3
        const val TYPE_UNLOCK = 4
        const val TYPE_NATIVE_AD = 9
        const val TYPE_UNAVAILABLE = 99
    }

}