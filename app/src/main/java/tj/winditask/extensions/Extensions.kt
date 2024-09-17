package tj.winditask.extensions

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Base64.encodeToString
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.io.Serializable

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

fun TextInputEditText.makeEnabled() = with(this) {
    isFocusable = false
    isEnabled = false
    isClickable = false
    keyListener = null
}

fun TextInputEditText.makeEnabledButClickable() = with(this) {
    isFocusable = false
    isClickable = true
    isLongClickable = true
    keyListener = null
}

fun isValidUsername(username: String): Boolean {
    val regexPattern = Regex("^[A-Za-z0-9\\-_]{5,}$")
    return username.matches(regexPattern)
}


fun ContentResolver.getFileName(uri: Uri): String? {
    var fileName: String? = null
    val cursor: Cursor? = this.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex != -1 && it.moveToFirst()) {
            fileName = it.getString(nameIndex)
        }
    }
    return fileName
}

fun ByteArray.encodeToBase64(): String {
    val outputStream = ByteArrayOutputStream()
    outputStream.write(this)
    val base64Image = encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    outputStream.close()
    return base64Image
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}
