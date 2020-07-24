package com.developer.ivan.coreapp.ui.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UIArticle(
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
    val repositories: List<UIRepository>,
    val repositoryDocument: UIRepositoryDocument?,
    val subjects: List<String>,
    val title: String,
    val topics: List<String>,
    val types: List<String>,
    val year: Int?
) : Parcelable

@Parcelize
data class UIRepository(
    val id: Int,
    val name: String?
) : Parcelable

@Parcelize
data class UIRepositoryDocument(
    val depositedDate: Long?,
    val metadataAdded: Long?,
    val metadataUpdated: Long?,
    val pdfStatus: Int?
) : Parcelable