package com.twogentle.inartist.model

import java.io.Serializable

data class Post(
    var postType: Int? = null,
    var title: String? = null,
    var id: String? = null,
    var url: String? = null,
    var datePosted: Long? = null,
    var views: Int? = null,
    var likes: Int? = null,
    var saves: Int? = null,
    var shares: Int? = null,
    var artistID: String? = null,
    var artistName: String? = null,
    var artistUsername: String? = null,
    var artistBio: String? = null,
    var artistProfilePic: String? = null,
    var artistSocial: HashMap<String, String>? = null,
    var category: String? = null,
    var collection: String? = null,
    var type: String? = null,
    var userLiked: List<String>? = null,
    var userSaved: List<String>? = null,
    var userUnlocked: List<String>? = null,
    var contentType: String? = null,
    var thumbnail: String? = null,
    var random:Long?=null,
    var date:Long?=null,
    var instagramUrl:String?=null
) : Serializable {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_POST = 1
        const val TYPE_POLL = 2
        const val TYPE_POST_LOCKED = 3
        const val TYPE_NATIVE_AD = 9
        const val TYPE_UNAVAILABLE = 99
    }

}