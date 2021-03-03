package com.twogentle.inartist.model

data class PointsHistory(
    val type: Int? = null,
    val timestamp: Long? = null,
    val change: String? = null
) {

    companion object {
        const val TYPE_INITIAL = 0
        const val TYPE_DAILY = 1
        const val TYPE_VIDEO = 2
        const val TYPE_IMAGE = 3
        const val TYPE_UNLOCK_VIDEO = 4
        const val TYPE_UNLOCK_ART = 5
        const val TYPE_REFER = 6
        const val TYPE_POLL_ENTRY = 7
        const val TYPE_POLL_WIN = 8
        const val TYPE_POLL_TIE = 9

        const val CHANGE_INITIAL = "+50"
        const val CHANGE_DAILY_STREAK_ONE = "+40"
        const val CHANGE_DAILY_STREAK_TWO = "+60"
        const val CHANGE_DAILY_STREAK_THREE = "+80"
        const val CHANGE_DAILY_STREAK_FOUR = "+100"
        const val CHANGE_DAILY_STREAK_FIVE = "+120"
        const val CHANGE_DAILY_STREAK_SIX = "+150"
        const val CHANGE_DAILY_STREAK_SEVEN = "+200"
        const val CHANGE_VIDEO = "+30"
        const val CHANGE_IMAGE = "+5"
        const val CHANGE_UNLOCK_VIDEO = "0"
        const val CHANGE_UNLOCK_ART = "-30"
        const val CHANGE_REFER = "+50"
        const val CHANGE_POLL_ENTRY = "-50"
        const val CHANGE_POLL_WIN = "+100"
    }

}