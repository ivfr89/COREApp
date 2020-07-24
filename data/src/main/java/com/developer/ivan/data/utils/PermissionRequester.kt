package com.developer.ivan.data.utils

import com.developer.ivan.data.datasources.BroadcastDataSource
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure

interface PermissionRequester{

    class PermissionDenied: Failure.CustomFailure()

    suspend fun requestPermission(permission: PERMISSION) : Either<Failure, Unit>

    enum class PERMISSION{
        WRITE
    }
}