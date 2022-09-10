package com.example.w2_d2_retrofit

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

object DemoApi {
    const val url = "https://en.wikipedia.org/w/"

    object Model {
        data class PresidentResult(
            val batchcomplete: String,
            @SerializedName("continue")
            val continues: Continue,
            val query: QueryResult,
        )

        data class Continue(
            val sroffset: String,
            @SerializedName("continue")
            val continues: String,
        )

        data class QueryResult(
            val searchinfo: SearchInfo,
            val search: List<Number>,
        )

        data class SearchInfo(
            val totalhits: Int,
            val suggestion: String,
            val suggestionsnippet: String,
        )

        data class Number(
            val ns: Int,
            val title: String,
            val pageid: Int,
            val size: Int,
            val wordcount: Int,
            val snippet: String,
            val timestamp: Date,
        )
    }

    interface Service {
        @GET("api.php?action=query&format=json&list=search")
        suspend fun searchPresident(@Query("srsearch") action: String): Model.PresidentResult
    }

    private val retrofit = Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(Service::class.java)!!

    class WebServiceRepository() {
        private val call = service
        suspend fun search(name: String) = call.searchPresident(name)
    }
}