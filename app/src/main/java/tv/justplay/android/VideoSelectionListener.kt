package tv.justplay.android

import androidx.navigation.NavDirections

interface VideoSelectionListener {
    fun onVideoClicked(navOptions: NavDirections)
}