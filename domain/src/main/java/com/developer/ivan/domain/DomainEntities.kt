package com.developer.ivan.domain

data class Article(
    val id: Int,
    val authors: List<String>,
    val contributors: List<String>,
    val datePublished: String?,
    val description: String?,
    val downloadUrl: String?,
    val fulltextIdentifier: String?,
    val fulltextUrls: List<String>,
    val oai: String?,
    val publisher: String?,
    val relations: List<String>,
    val repositories: List<Repository>,
    val repositoryDocument: RepositoryDocument?,
    val subjects: List<String>,
    val title: String,
    val topics: List<String>,
    val types: List<String>,
    val year: Int?
)

data class Repository(
    val id: Int,
    val name: String?
)

data class RepositoryDocument(
    val depositedDate: Long?,
    val metadataAdded: Long?,
    val metadataUpdated: Long?,
    val pdfStatus: Int?
)

sealed class Failure{

    abstract class CustomFailure : Failure()
}
object NoParams
