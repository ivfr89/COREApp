package com.developer.ivan.domain

object Constants{

    object Server{
        const val BASE_URL = "https://core.ac.uk:443/api-v2/"
        const val DEFAULT_PAGE = 1
        const val DEFAULT_SIZE = 10

        object Articles {
            const val NAME = "articles/"
            const val GET_SEARCH = "search/"
            const val GET_BY_ID = "get/"
        }

        object OkHTTPConfig {
            const val timeOut = 15000L
        }

        object Headers {
            const val API = "apiKey"
        }
    }
}