package com.developer.ivan.coreapp.ui.checkers

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.developer.ivan.data.utils.PermissionChecker

class PermissionCheckerImplementation(val application: Application) : PermissionChecker {


    override fun checkIfWriteIsGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) ==
                PackageManager.PERMISSION_GRANTED

}