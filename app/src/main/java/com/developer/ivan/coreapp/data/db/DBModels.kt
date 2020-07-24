package com.developer.ivan.coreapp.data.db

import androidx.room.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


class Converters {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = Types.newParameterizedType(
            List::class.java,
            String::class.java
        )
        return moshi.adapter<ArrayList<String>>(listType).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {

        val listType = Types.newParameterizedType(
            List::class.java,
            String::class.java
        )
        return moshi.adapter<List<String>>(listType).toJson(list)
    }
}

@Entity(primaryKeys = ["id_article"])
@TypeConverters(value = [Converters::class])
class DBArticle(
    @ColumnInfo(name = "id_article")
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
    @Embedded
    val repositoryDocument: DBRepositoryDocument?,
    val subjects: List<String>,
    val title: String,
    val topics: List<String>,
    val types: List<String>,
    val year: Int?
)

class DBRepositoryDocument(
    val depositedDate: Long?,
    val metadataAdded: Long?,
    val metadataUpdated: Long?,
    val pdfStatus: Int?
)

@Entity(primaryKeys = ["id_repository"])
class DBRepository(
    @ColumnInfo(name = "id_repository")
    val id: Int,
    val name: String?
)

@Entity(
    primaryKeys = ["idArticle", "idRepository"],
    indices = [Index("idArticle"), Index("idRepository")],
    foreignKeys = [
        ForeignKey(
            entity = DBArticle::class,
            parentColumns = ["id_article"],
            childColumns = ["idArticle"]
        ),
        ForeignKey(
            entity = DBRepository::class,
            parentColumns = ["id_repository"],
            childColumns = ["idRepository"]
        )
    ]
)
class DBArticleRepositoryJoin(
    val idArticle: Int,
    val idRepository: Int
)

class DBArticleWithRepositories {

    @Embedded
    lateinit var article: DBArticle

    @Relation(
        entityColumn = "id_repository",
        parentColumn = "id_article",
        entity = DBRepository::class,
        associateBy = Junction(
            DBArticleRepositoryJoin::class,
            entityColumn = "idRepository",
            parentColumn = "idArticle"
        )
    )
    var repositories: List<DBRepository> = emptyList()
}

@Entity(primaryKeys = ["query"])
class DBQuery(
    val query: String
)

@Entity(
    primaryKeys = ["query", "idArticle"],
    indices = [
        Index("query"),
        Index("idArticle")
    ],
    foreignKeys = [
        ForeignKey(
            entity = DBQuery::class,
            parentColumns = ["query"],
            childColumns = ["query"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DBArticle::class,
            parentColumns = ["id_article"],
            childColumns = ["idArticle"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class DBQueryArticleJoin(
    val query: String,
    val idArticle: Int
)


class DBQueryWithArticles {
    @Embedded
    lateinit var dbQuery: DBQuery

    @Relation(
        entity = DBArticle::class,
        parentColumn = "query",
        entityColumn = "id_article",
        associateBy = Junction(
            DBQueryArticleJoin::class,
            parentColumn = "query",
            entityColumn = "idArticle"
        )
    )
    var articles: List<DBArticleWithRepositories> = emptyList()

}
/*
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
)*/