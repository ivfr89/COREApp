package com.developer.ivan.coreapp.data.db.mapper

import com.developer.ivan.coreapp.data.db.*
import com.developer.ivan.domain.Article
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Repository
import com.developer.ivan.domain.RepositoryDocument

class DBMapper {

    object FormatFailure : Failure.CustomFailure()

    fun convertDBArticlesToDomain(dbArticles: List<DBArticleWithRepositories>): List<Article> =
        dbArticles.map { entity ->
            convertDBArticleWithRepositoriesToDomain(entity)
        }

    fun convertDBArticleWithRepositoriesToDomain(dbArticle: DBArticleWithRepositories): Article {

        return with(dbArticle.article) {
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
                dbArticle.repositories.map { repository -> convertDBRepositoryToDomain(repository) },
                convertDBRepositoryDocumentToDomain(repositoryDocument),
                subjects,
                title,
                topics,
                types,
                year
            )
        }


    }

    fun convertQueryArticlesToDB(
        query: String,
        articles: List<Article>
    ): DBQueryWithArticles =
        DBQueryWithArticles().also {
            it.dbQuery = DBQuery(query)
            it.articles =
                articles.map { article -> convertArticleToDBArticlesWithRepositories(article) }
        }

    fun convertArticlesToDB(articles: List<Article>): List<DBArticleWithRepositories> =
        articles.map { article -> convertArticleToDBArticlesWithRepositories(article) }

    fun convertArticleToDBArticlesWithRepositories(articleDomain: Article): DBArticleWithRepositories {

        return with(articleDomain) {

            DBArticleWithRepositories().also {
                it.article = DBArticle(
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
                    convertRepositoryDocumentToDB(repositoryDocument),
                    subjects,
                    title,
                    topics,
                    types,
                    year
                )
                it.repositories = repositories.map { convertRepositoryToDB(it) }

            }
        }


    }

    fun convertDBRepositoryToDomain(entityRepository: DBRepository): Repository =
        with(entityRepository) {
            Repository(id, name)
        }

    fun convertRepositoryToDB(repository: Repository): DBRepository =
        with(repository) {
            DBRepository(id, name)
        }


    fun convertDBRepositoryDocumentToDomain(entityRepositoryDocument: DBRepositoryDocument?) =
        entityRepositoryDocument.let {
            RepositoryDocument(
                it?.depositedDate,
                it?.metadataAdded,
                it?.metadataUpdated,
                it?.pdfStatus
            )
        }

    fun convertRepositoryDocumentToDB(repositoryDocument: RepositoryDocument?) =
        repositoryDocument.let {
            DBRepositoryDocument(
                it?.depositedDate,
                it?.metadataAdded,
                it?.metadataUpdated,
                it?.pdfStatus
            )
        }

//    QUERY

    fun convertDBQueryToDomain(dbArticles: List<DBArticleWithRepositories>): List<Article> =
        dbArticles.map { entity ->
            convertDBArticleWithRepositoriesToDomain(entity)
        }
}
