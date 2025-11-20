/**
 * Created by chrystian.lozada.local (20/11/25)
 */
package com.mobilitydevai.sce.data.network

import com.google.gson.Gson
import com.mobilitydevai.sce.BuildConfig
import com.mobilitydevai.sce.config.Constants
import com.mobilitydevai.sce.data.response.EpisodesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class EpisodesOkHttpClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {

    suspend fun fetchEpisodes(page: Int): EpisodesResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val url = "${BuildConfig.BASE_URL}${Constants.ENDPOINT_EPISODES}?page=$page"
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = okHttpClient.newCall(request).execute()

                if (response.isSuccessful) {
//                    response.body.string().let { jsonString ->
//                        gson.fromJson(jsonString, EpisodesResponse::class.java)
//                    }
                    gson.fromJsonCustom<EpisodesResponse>(response.body.string())
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    inline fun <reified T> Gson.fromJsonCustom(json: String): T =
        this.fromJson(json, T::class.java)
}
