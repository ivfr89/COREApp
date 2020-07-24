package com.developer.ivan.coreapp.data.server

import com.developer.ivan.domain.Constants
import com.developer.ivan.domain.Constants.Server.DEFAULT_PAGE
import com.developer.ivan.domain.Constants.Server.DEFAULT_SIZE
import com.developer.ivan.coreapp.data.server.entities.EntityArticle
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

//    https://core.ac.uk:443/api-v2/articles/search/arist%C3%B3teles?page=1&pageSize=10&metadata=true&fulltext=false&citations=false&similar=false&duplicate=false&urls=true&faithfulMetadata=false&apiKey=Bkbr7yEazKR8iq0Ahn6cpoHuJMjNU41L

    @retrofit2.http.GET(Constants.Server.BASE_URL + Constants.Server.Articles.NAME + Constants.Server.Articles.GET_SEARCH + "{query}")
    suspend fun searchArticles(
        @Path("query") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("metadata") metadata: Boolean = true,
        @Query("fulltext") fulltext: Boolean = false,
        @Query("citations") citations: Boolean = false,
        @Query("similar") similar: Boolean = false,
        @Query("duplicate") duplicate: Boolean=false,
        @Query("urls") urls: Boolean=true,
        @Query("faithfulMetadata") faithfulMetadata: Boolean = false,
        @HeaderMap headerMap: Map<String,String>
    ) : Response<String>

    @GET(Constants.Server.BASE_URL + Constants.Server.Articles.NAME + Constants.Server.Articles.GET_BY_ID + "{id}")
    suspend fun getArticle(
        @Path("id") id: Int,
        @Query("metadata") metadata: Boolean = true,
        @Query("fulltext") fulltext: Boolean = false,
        @Query("citations") citations: Boolean = false,
        @Query("similar") similar: Boolean = false,
        @Query("duplicate") duplicate: Boolean=false,
        @Query("urls") urls: Boolean=true,
        @Query("faithfulMetadata") faithfulMetadata: Boolean = false,
        @HeaderMap headerMap: Map<String,String>
    ) : Response<String>


}