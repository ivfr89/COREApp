package com.developer.ivan.coreapp.data.server.mapper

import com.developer.ivan.coreapp.data.server.entities.EntityArticle
import com.developer.ivan.coreapp.data.server.entities.EntityRepository
import com.developer.ivan.coreapp.data.server.entities.EntityRepositoryDocument
import com.developer.ivan.domain.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONException
import org.json.JSONObject


class ServerMapper {

    object FormatFailure : Failure.CustomFailure()


    fun convertEntitiesArticleToDomain(entityArticles: List<EntityArticle>): Either<Failure, List<Article>> {

        return Either.Right(entityArticles.mapNotNull { entity ->

            when (val result = convertEntityArticleToDomain(entity)) {
                is Either.Right -> result.b
                else -> null
            }

        })
    }

    fun convertEntityArticleToDomain(entityArticle: EntityArticle): Either<Failure, Article> {

        var articleId: Int?
        try {
            articleId = entityArticle.id.toInt()
        } catch (e: NumberFormatException) {
            return Either.Left(FormatFailure)
        }



        return Either.Right(with(entityArticle) {
            Article(
                articleId,
                authors,
                contributors,
                datePublished,
                description,
                downloadUrl,
                fulltextIdentifier,
                fulltextUrls,
                oai,
                publisher,
                relations,
                repositories.mapNotNull { repository ->
                    when (val result = convertEntityRepositoryToDomain(repository)) {
                        is Either.Right -> result.b
                        else -> null
                    }
                },
                convertEntityRepositoryDocumentToDomain(repositoryDocument),
                subjects,
                title,
                topics,
                types,
                year
            )
        })


    }

    fun convertEntityRepositoryToDomain(entityRepository: EntityRepository): Either<Failure, Repository> {
        var repositoryId: Int?
        try {
            repositoryId = entityRepository.id.toInt()
        } catch (e: NumberFormatException) {
            return Either.Left(FormatFailure)
        }

        return Either.Right(with(entityRepository) {
            Repository(repositoryId, name)
        })
    }


    fun convertEntityRepositoryDocumentToDomain(entityRepositoryDocument: EntityRepositoryDocument?) =
        entityRepositoryDocument.let {
            RepositoryDocument(
                it?.depositedDate,
                it?.metadataAdded,
                it?.metadataUpdated,
                it?.pdfStatus
            )
        }


}