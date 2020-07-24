package com.developer.ivan.coreapp.data.server.entities
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class EntityArticle(
    @Json(name = "authors")
    val authors: List<String>,
    @Json(name = "contributors")
    val contributors: List<String>,
    @Json(name = "datePublished")
    val datePublished: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "downloadUrl")
    val downloadUrl: String?,
    @Json(name = "fulltextIdentifier")
    val fulltextIdentifier: String?,
    @Json(name = "fulltextUrls")
    val fulltextUrls: List<String>,
    @Json(name = "id")
    val id: String,
    @Json(name = "oai")
    val oai: String?,
    @Json(name = "publisher")
    val publisher: String?,
    @Json(name = "relations")
    val relations: List<String>,
    @Json(name = "repositories")
    val repositories: List<EntityRepository>,
    @Json(name = "repositoryDocument")
    val repositoryDocument: EntityRepositoryDocument?,
    @Json(name = "subjects")
    val subjects: List<String>,
    @Json(name = "title")
    val title: String,
    @Json(name = "topics")
    val topics: List<String>,
    @Json(name = "types")
    val types: List<String>,
    @Json(name = "year")
    val year: Int?
)

@JsonClass(generateAdapter = true)
data class EntityRepository(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String?
)

@JsonClass(generateAdapter = true)
data class EntityRepositoryDocument(
    @Json(name = "depositedDate")
    val depositedDate: Long?,
    @Json(name = "metadataAdded")
    val metadataAdded: Long?,
    @Json(name = "metadataUpdated")
    val metadataUpdated: Long?,
    @Json(name = "pdfStatus")
    val pdfStatus: Int?
)