package com.developer.ivan.coreapp.ui.mapper

import com.developer.ivan.coreapp.ui.main.UIArticle
import com.developer.ivan.coreapp.ui.main.UIRepository
import com.developer.ivan.coreapp.ui.main.UIRepositoryDocument
import com.developer.ivan.domain.Article
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Repository
import com.developer.ivan.domain.RepositoryDocument


class UIMapper {

    object FormatFailure : Failure.CustomFailure()

    fun convertUIArticlesToDomain(dbArticles: List<UIArticle>): List<Article> =
        dbArticles.map { entity ->
            convertUIArticleToDomain(entity)
        }

    fun convertUIArticleToDomain(uiArticle: UIArticle): Article {

        return with(uiArticle) {
            Article(
                id,
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
                uiArticle.repositories.map { repository -> convertUIRepositoryToDomain(repository) },
                convertUIRepositoryDocumentToDomain(repositoryDocument),
                subjects,
                title,
                topics,
                types,
                year
            )
        }


    }


    fun convertArticlesToUI(articles: List<Article>): List<UIArticle> =
        articles.map { article -> convertArticleToUI(article) }

    fun convertArticleToUI(articleDomain: Article): UIArticle {

        return with(articleDomain) {

            UIArticle(id,
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
                repositories.map { repository-> convertRepositoryToUI(repository) },
                convertRepositoryDocumentToUI(repositoryDocument),
                subjects,
                title,
                topics,
                types,
                year)
            }
        }

    fun convertUIRepositoryToDomain(uiRepository: UIRepository): Repository =
        with(uiRepository) {
            Repository(id, name)
        }

    fun convertRepositoryToUI(repository: Repository): UIRepository =
        with(repository) {
            UIRepository(id, name)
        }


    fun convertUIRepositoryDocumentToDomain(entityRepositoryDocument: UIRepositoryDocument?) =
        entityRepositoryDocument.let {
            RepositoryDocument(
                it?.depositedDate,
                it?.metadataAdded,
                it?.metadataUpdated,
                it?.pdfStatus
            )
        }

    fun convertRepositoryDocumentToUI(repositoryDocument: RepositoryDocument?) =
        repositoryDocument.let {
            UIRepositoryDocument(
                it?.depositedDate,
                it?.metadataAdded,
                it?.metadataUpdated,
                it?.pdfStatus
            )
        }

}
