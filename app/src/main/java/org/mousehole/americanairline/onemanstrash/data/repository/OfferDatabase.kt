package org.mousehole.americanairline.onemanstrash.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.mousehole.americanairline.onemanstrash.model.Offer
import org.mousehole.americanairline.onemanstrash.utils.Constants.BIDON
import org.mousehole.americanairline.onemanstrash.utils.Constants.OFFERS
import org.mousehole.americanairline.onemanstrash.utils.Constants.USERS
import org.mousehole.americanairline.onemanstrash.utils.Constants.WATCHERS
import org.mousehole.americanairline.onemanstrash.utils.DebugLogger.debug

object OfferDatabase {

    private val offerData = MutableLiveData<List<Offer>>()

    private fun makePath(vararg str:String) : String =
            str.joinToString("/")

    fun putData(offer: Offer) {
        val offers = Firebase.database.getReference(OFFERS)
        val id = offers.push()
        debug("Got id: $id")
        offer.id = id.toString()
        id.setValue(offer)
        val addId = Firebase.database
                .getReference(makePath(USERS, offer.seller, OFFERS)).push()
        addId.setValue(id.toString())
    }

    private class UpdateValueEventListener(private val mutableLiveData: MutableLiveData<List<Offer>>) : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val values = snapshot.children.map{it.getValue(Offer::class.java)}
                    .filterNotNull()
            mutableLiveData.postValue(values)
        }

        override fun onCancelled(error: DatabaseError) {
            // TODO("Not yet implemented")
        }
    }

    fun getUpdateData() : LiveData<List<Offer>> {
        val orderRef = Firebase.database.getReference(OFFERS)
        orderRef.addValueEventListener(UpdateValueEventListener(offerData))
        return offerData
    }

    fun getData() : LiveData<List<Offer>> {
        val orderRef = Firebase.database.getReference(OFFERS)
//        getData(orderRef, offerData)
        return offerData
    }

    private val watchData = MutableLiveData<List<Offer>>()

    fun getWatchData() : LiveData<List<Offer>> {
        debug("uid is? ${getUid()}")
        val orderRef = Firebase.database.getReference(OFFERS).orderByChild(makePath(WATCHERS, getUid())).equalTo(true)
        orderRef.addValueEventListener(UpdateValueEventListener(watchData))
        return watchData
    }

    fun setWatch(offer:Offer) {
        val user = Firebase.auth.currentUser?.uid?:""
        debug("offer id is: ${offer.id}")
        Firebase.database.getReference(makePath(USERS, user, WATCHERS, Uri.parse(offer.id).lastPathSegment.toString())).setValue(offer)
        Firebase.database.getReference(makePath(OFFERS, Uri.parse(offer.id).lastPathSegment.toString(), WATCHERS, user)).setValue(true)
    }

    fun deleteWatch(offer:Offer) {
        val user = Firebase.auth.currentUser?.uid?:""
        debug("offer id is: ${offer.id}")
        Firebase.database.getReference(makePath(USERS, user, WATCHERS, Uri.parse(offer.id).lastPathSegment.toString())).removeValue()
        Firebase.database.getReference(makePath(OFFERS, Uri.parse(offer.id).lastPathSegment.toString(), WATCHERS, user)).removeValue()
    }

    fun bid(offer:Offer) {
        val user = Firebase.auth.currentUser?.uid?:""
        debug("offer id is: ${offer.id}")
        Firebase.database.getReference(makePath(OFFERS, Uri.parse(offer.id).lastPathSegment.toString(), BIDON)).push().setValue(user)
    }

    fun getUid() : String = Firebase.auth.currentUser?.uid?:""

    fun loggedInVerifiedUser() : Boolean = Firebase.auth.currentUser?.isEmailVerified?:false

    fun requestValidationEmail() = Firebase.auth.currentUser?.sendEmailVerification()

    fun getEmail() = Firebase.auth.currentUser?.email?:""
//    fun createOffer(offer:Offer) {
//        val newOffer = Firebase.database.getReference(OFFERS).push()
//        offer.id = newOffer.toString()
//        newOffer.setValue(offer)
//        val userOffers = Firebase.database.getReference(makeUserPath(offer.name, OFFERS) + "/${offer.id}").setValue(offer)
//    }
}