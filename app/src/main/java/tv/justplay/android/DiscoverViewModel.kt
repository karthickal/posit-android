package tv.justplay.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class DiscoverViewModel : ViewModel(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private var videoList: List<VideoInfo> = mutableListOf()

    fun fetchVideoList(): LiveData<List<VideoInfo>> {
        return liveData {
            val movie = getMovieDetail() ?: return@liveData
            emit(
                listOf(
                    movie,
                    VideoInfo(
                        title = "Aval Anthathi",
                        thumbnailUrl = "https://s3.ap-south-1.amazonaws.com/content.justplay.tv/testing/posters/aval-anthathi/web_poster.png",
                        mpdUrl = "https://s3.ap-south-1.amazonaws.com/testing.deliver.videos.justplay.tv/titles/aval-anthathi/mobile/output/media.m3u8",
                        id = "811",
                        fps = 24
                    )
                )
            )
        }
    }
}