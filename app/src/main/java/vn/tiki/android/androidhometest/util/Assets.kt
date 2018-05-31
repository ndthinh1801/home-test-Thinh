package vn.tiki.android.androidhometest.util

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import java.nio.charset.Charset

fun AssetManager.readFile(file: String): String {
  return open(file).bufferedReader(Charset.forName("utf-8")).use { it.readText() }
}

fun AssetManager.getImage(file: String): Drawable {
  return Drawable.createFromStream(open(file), null)
}