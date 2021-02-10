package org.mousehole.americanairline.onemanstrash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mousehole.americanairline.onemanstrash.data.repository.OfferDatabase
import org.mousehole.americanairline.onemanstrash.model.Offer

object ListingViewModel : ViewModel() {
    fun putData(offer: Offer): Unit = OfferDatabase.putData(offer)
    fun getData(): LiveData<List<Offer>> {
        OfferDatabase.getUpdateData()
        return OfferDatabase.getData()
    }
    fun getEmail() = OfferDatabase.getEmail()
    fun bid(offer: Offer): Unit = OfferDatabase.bid(offer)
    fun getUid() : String = OfferDatabase.getUid()
}