package org.tensorflow.lite.codedirect.dermiscan.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaScannerConnection
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.multidex.BuildConfig
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.tensorflow.lite.codedirect.dermiscan.R
import java.io.*
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun changeFragment(fragmentManager: FragmentManager?, id: Int, frameDestination: Fragment) {
        fragmentManager?.beginTransaction()?.replace(id, frameDestination)?.commit()
    }

    fun changeFragmentBack(fragmentManager: FragmentManager?, id: Int, frameDestination: Fragment) {
        fragmentManager?.beginTransaction()?.replace(id, frameDestination)?.addToBackStack("")
                ?.commit()
    }

    fun toast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun loadModelFile(activity: Activity, path: String): MappedByteBuffer {
        val fileDescriptor = activity.assets.openFd(path)

        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel

        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun changeToActivity(context: Context?, destinationClass: AppCompatActivity) {
        context?.startActivity(Intent(context, destinationClass::class.java))
    }

    fun showToolbar(tvTitle: TextView, text: String) {
        tvTitle.text = text
    }

    fun showLoading(loading: ProgressBar) {
        loading.visibility = View.VISIBLE
    }

    fun formDialogList(
            context: Context,
            dataItems: ArrayList<String>,
            listenerPositive: (MaterialDialog, Int) -> Unit
    ) {
        MaterialDialog(context).show {
            listItems(items = dataItems) { dialog, index, text ->
                listenerPositive(dialog, index)
            }
        }
    }

    fun convertBitmapGallery(bitmap: Bitmap, config: Bitmap.Config): Bitmap {
        val convertedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, config)
        val canvas = Canvas(convertedBitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return convertedBitmap
    }

    fun hideLoading(loading: ProgressBar) {
        loading.visibility = View.GONE
    }

    fun getFileName(idUser: String): String {
        return "${Calendar.getInstance()
                .timeInMillis}.jpg"
    }

    fun formDialog(
            context: Context,
            title: String,
            message: String,
            listenerPositive: (MaterialDialog) -> Unit
    ) {
        MaterialDialog(context).show {
            title(text = title)
            message(text = message)
            positiveButton(text = context.resources?.getString(R.string.yes_)) {
                listenerPositive(it)
            }
            negativeButton(text = context.resources?.getString(R.string.no_)) {
                it.dismiss()
            }
        }
    }

    fun saveImage(myBitmap: Bitmap, context: Context?, nameFile: String): String {
        val imagePath = ""
        val m = context?.packageManager
        var s = context?.packageName
        var p: PackageInfo? = null
        try {
            p = m?.getPackageInfo(s, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        s = p?.applicationInfo?.dataDir
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File("${context?.getExternalFilesDir(context.getString(R.string.app_name))}$s")
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            var f = File(wallpaperDirectory, nameFile)
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                    context,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null
            )
            fo.close()
            f = File(f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return imagePath
    }

    fun getMultipartBody(filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val mFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("image", file.name, mFile)
    }

    fun getMultipartBodyUpload(filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val mFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("upload", file.name, mFile)
    }

    fun getRequestBody(filePath: String): RequestBody {
        val file = File(filePath)
        return RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)
    }

    fun getDateNow(): String {
        val cal = Calendar.getInstance()
        val date = cal.time
        val dateFormats = SimpleDateFormat("yyyy-MM-dd")
        return dateFormats.format(date)
    }

    fun formatDateNow(): String {
        val cal = Calendar.getInstance()
        val date = cal.time
        val dateFormats = SimpleDateFormat("yyyyMMdd")
        return dateFormats.format(date)
    }

    fun getDateTimeNow(): String {
        val today = Date()
        val dateFormats = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormats.format(today)
    }

    fun getVersiAplication(): String {
        return BuildConfig.VERSION_CODE.toString()
    }

    fun changeToActivityWithBundle(
            context: Context?,
            destinationClass: AppCompatActivity,
            bundle: Bundle
    ) {
        val intent = Intent(context, destinationClass::class.java)
        intent.putExtras(bundle)
        context?.startActivity(intent)
    }
}