package org.mousehole.americanairline.onemanstrash.data.repository

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.mousehole.americanairline.onemanstrash.R
import org.mousehole.americanairline.onemanstrash.model.Offer
import org.mousehole.americanairline.onemanstrash.utils.QuickToast
import java.io.ByteArrayOutputStream
import java.util.*

class FileSaver(private val fileSaverCallback: FileSaverCallback) {

    interface FileSaverCallback {
        fun reset(): Unit
    }

    // using an activity because, really, it needs to be where the value returns
    fun saveFile(context: Context, offer: Offer, offerBitmap: Bitmap?) {
        val filename = OfferDatabase.getUid()
        val storageRef = Firebase.storage.getReference(
                filename + "/" + GregorianCalendar().time.time.toString() + ".jpg")
        val byteOutputArray = ByteArrayOutputStream()
        offerBitmap?.compress(Bitmap.CompressFormat.JPEG, 80, byteOutputArray)
        val bytes = byteOutputArray.toByteArray()
        storageRef.putBytes(bytes)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        storageRef.downloadUrl.addOnCompleteListener { u ->
                            offer.apply {
                                email = OfferDatabase.getEmail()
                                seller = OfferDatabase.getUid()
                                url = u.result.toString()
                            }
                            OfferDatabase.putData(offer)
                            QuickToast.showMessage(context,
                                    "Thank you for using One Man's Trash!")
                            fileSaverCallback.reset()
                        }
                    }
                }
    }
}