package com.developer.ivan.data.utils

import com.developer.ivan.domain.Failure


interface PermissionChecker{
    fun checkIfWriteIsGranted(): Boolean

    class PermissionNotGranted: Failure.CustomFailure()
}