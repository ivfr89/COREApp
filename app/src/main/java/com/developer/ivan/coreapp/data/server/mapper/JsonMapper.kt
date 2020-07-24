package com.developer.ivan.coreapp.data.server.mapper

import com.developer.ivan.coreapp.data.server.entities.EntityArticle
import com.developer.ivan.domain.Article
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class JsonMapper(private val moshi: Moshi) {

    object JsonSyntaxException : Failure.CustomFailure()


    inline fun <T> getArray(json: String, body: JsonMapper.(JSONArray)->T): T {
        return body(JSONObject(json).getJSONArray("data"))
    }

    inline fun <T> getObject(json: String, body: JsonMapper.(JSONObject)->T): T{
        return body(JSONObject(json).getJSONObject("data"))
    }

    fun convertJsonToArticle(
        json: JSONObject,
        transform: (EntityArticle) -> Either<Failure, Article>
    ): Either<Failure, Article> {
        return when (val result = convertJsonToEntityArticle(json.toString())) {
            is Either.Left -> result
            is Either.Right -> transform(result.b)
        }
    }

    fun convertJsonToArticles(
        json: JSONArray,
        transform: (EntityArticle) -> Either<Failure, Article>
    ): Either<Failure, List<Article>> {

        val list = mutableListOf<Article>()
        var result: Either<Failure, List<Article>> = Either.Right(list)

        var index = 0
        var failure = false

        try {
            while (index < json.length() && !failure) {
                when (val convertion = convertJsonToArticle(json.getJSONObject(index), transform)) {
                    is Either.Left -> {
                        failure = true
                        result = convertion
                    }
                    is Either.Right -> {
                        list.add(convertion.b)
                    }
                }
                index++
            }
        }catch (e: JSONException){
            result = Either.Left(JsonSyntaxException)
        }



        return result
    }

    fun convertJsonToEntitiesArticle(json: JSONObject): Either<Failure, List<EntityArticle>> {
        return try {

            val listType = Types.newParameterizedType(
                List::class.java,
                EntityArticle::class.java
            )

            val data =
                moshi.adapter<List<EntityArticle>>(listType).fromJson(json.toString())

            data?.let {
                Either.Right(it)
            } ?: kotlin.run {
                Either.Left(JsonSyntaxException)
            }

        } catch (e: JSONException) {
            Either.Left(JsonSyntaxException)
        }
    }

    fun convertJsonToEntityArticle(json: String): Either<Failure, EntityArticle> {
        return try {
            val data =
                moshi.adapter(EntityArticle::class.java).fromJson(json)

            data?.let {
                Either.Right(it)
            } ?: kotlin.run {
                Either.Left(JsonSyntaxException)
            }

        } catch (e: JSONException) {
            Either.Left(JsonSyntaxException)
        }
    }

}