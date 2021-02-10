package org.mousehole.americanairline.onemanstrash.utils

import android.content.Context
import android.widget.Toast

// quick toast
object QuickToast {
    fun showMessage(context: Context, msg:String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    fun showMessage(context: Context, msg:List<String>) {
        showMessage(context, msg.joinToString("\n"))
    }
}