package com.p2p.wallet.common.permissions

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

@TargetApi(Build.VERSION_CODES.M)
object PermissionsUtil {

    fun isGranted(context: Context, permission: String): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun getPermissionStatus(activity: Activity, permission: String): PermissionState {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return PermissionState.GRANTED
        }

        return when {
            activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED -> PermissionState.GRANTED
            activity.shouldShowRequestPermissionRationale(permission) -> PermissionState.DENIED
            else -> PermissionState.PERMANENTLY_DENIED
        }
    }

    fun getPermissionStatus(fragment: Fragment, permission: String): PermissionState {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return PermissionState.GRANTED
        }

        return when {
            fragment.requireContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED ->
                PermissionState.GRANTED

            fragment.shouldShowRequestPermissionRationale(permission) -> PermissionState.DENIED
            else -> PermissionState.PERMANENTLY_DENIED
        }
    }

    fun showAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", context.packageName, null))
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}