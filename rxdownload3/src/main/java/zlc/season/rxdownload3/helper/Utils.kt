package zlc.season.rxdownload3.helper

import android.content.Context
import android.content.pm.PackageManager
import io.reactivex.disposables.Disposable
import java.io.File
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.round

internal val ANY = Any()

fun dispose(disposable: Disposable?) {
    if (disposable != null && !disposable.isDisposed) {
        disposable.dispose()
    }
}

fun formatSize(size: Long, s: Int = 1): String {
    val b = size.toDouble()
    val k = size / 1024.0
    val m = size / 1024.0 / 1024.0
    val g = size / 1024.0 / 1024.0 / 1024.0
    val t = size / 1024.0 / 1024.0 / 1024.0 / 1024.0

    return when {
        t > 1 -> "${t.roundTo(s)} TB"
        g > 1 -> "${g.roundTo(s)} GB"
        m > 1 -> "${m.roundTo(s)} MB"
        k > 1 -> "${k.roundTo(s)} KB"
        else ->  "${b.roundTo(s)} B"
    }
}

fun getPackageName(context: Context, apkFile: File): String {
    val pm = context.packageManager
    val apkInfo = pm.getPackageArchiveInfo(apkFile.path, PackageManager.GET_META_DATA)
    return apkInfo.packageName
}

fun Double.roundTo(s: Int = 1): Double {
    if (s == 0) return round(this)
    val power = (10.0).pow(s)
    return round(this * power) / power
}