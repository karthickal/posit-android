package tv.justplay.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class DiscoverViewModel : ViewModel(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private var videoList: List<VideoInfo> = mutableListOf()


    fun fetchVideoList(): LiveData<List<VideoInfo>> {

        val result = MutableLiveData<List<VideoInfo>>()

        if (this.videoList.isEmpty()) {
            this.videoList = mutableListOf(
//                VideoInfo(
//                    title = "YC - Pitch - Posit",
//                    thumbnailUrl = "https://s3.ap-south-1.amazonaws.com/content.justplay.tv/testing/posters/yc/yc_pitch_tb.png",
//                    mpdUrl = "https://s3.ap-south-1.amazonaws.com/testing.deliver.videos.justplay.tv/titles/YC/mobile/media.m3u8",
//                    id = "24",
//                    fps = 24
//                ),
                VideoInfo(
                    title = "Aval Anthathi",
                    thumbnailUrl = "https://s3.ap-south-1.amazonaws.com/content.justplay.tv/testing/posters/aval-anthathi/web_poster.png",
                    mpdUrl = "https://s3.ap-south-1.amazonaws.com/testing.deliver.videos.justplay.tv/titles/aval-anthathi/web/output/media.m3u8",
                    id = "1",
                    fps = 24
                ),
                VideoInfo(
                    title = "Time Travel Machine",
                    thumbnailUrl = "https://s3.ap-south-1.amazonaws.com/content.justplay.tv/testing/posters/time-travel-machine/web_poster.png",
                    mpdUrl = "https://s3.ap-south-1.amazonaws.com/testing.deliver.videos.justplay.tv/titles/short-films/time-travel/mobile/output/media.m3u8",
                    id = "13",
                    fps = 24
                ),
                VideoInfo(
                    title = "Avalum Naanum",
                    thumbnailUrl = "https://s3.ap-south-1.amazonaws.com/content.justplay.tv/testing/posters/avalum-naanum/web_poster.png",
                    mpdUrl = "https://s3.ap-south-1.amazonaws.com/testing.deliver.videos.justplay.tv/titles/short-films/avalum-naanum/mobile/output/media.m3u8",
                    id = "a234d213",
                    fps = 24
                )
            )
        }

        result.postValue(this.videoList)

        return result

    }


}