package com.developer.ivan.coreapp.ui.checkers

import android.app.Application
import com.developer.ivan.data.utils.PermissionRequester
import com.developer.ivan.data.utils.PermissionRequester.PERMISSION
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

@ExperimentalCoroutinesApi
class PermissionRequesterImplementation(val application: Application) : PermissionRequester {


    override suspend fun requestPermission(permission: PERMISSION): Either<Failure, Unit> {

        return suspendCancellableCoroutine { continuation ->

            Dexter.withContext(application)
                .withPermission(getPermission(permission))
                .withListener(object : BasePermissionListener() {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        continuation.resume(Either.Right(Unit)) {}
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        continuation.resume(Either.Left(PermissionRequester.PermissionDenied())) {}
                    }
                })
                .check()
        }

    }

    private fun getPermission(enumPermission: PERMISSION): String = when (enumPermission) {
        PERMISSION.WRITE -> android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    }


}