package tv.justplay.android

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

suspend fun getMovieDetail(): VideoInfo? {
    return withContext(Dispatchers.Default) {
        val client: OkHttpClient = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url("https://mzaalo.azure-api.net/platform/player/MOVIE/details/55ca20b0-147f-4c1f-80b0-0e7060464360")
            .method("GET", null)
            .addHeader(
                "Authorization",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiZTNmNTY1NDMtNDQ4Mi00ZWRlLTllMzEtYWYyNzRhYTU3ZGQwIiwiZW1haWwiOiJzaGl2YW1zZXRoaUBnbWFpbC5jb20iLCJpYXQiOjE1ODgwNzQ0NTgsImV4cCI6MTU4ODE2MDg1ODB9.jGA8tgyobuP806TbY27FpR7jlptjHX-ipMu5yX7r8fA"
            )
            .addHeader("Ocp-Apim-Subscription-Key", "ff2ad2c0e2b046909ccd2797d53ae49f")
            .build()
        val response: Response = client.newCall(request).execute()

        response.body()?.let {
            val result = Gson().fromJson(it.string(), JsonObject::class.java)

            VideoInfo(
                title = result.getAsJsonObject("data")["title"].asString,
                thumbnailUrl = "https://images004-a.erosnow.com/movie/4/1023354/img768851/6943014/1023354_6943014.jpg",
                mpdUrl = result.getAsJsonObject("data")["streamURL"].asString,
                id = "810",
                fps = 24
            )
        }
    }
}
