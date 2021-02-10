package org.mousehole.americanairline.onemanstrash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.mousehole.americanairline.onemanstrash.data.repository.OfferDatabase
import org.mousehole.americanairline.onemanstrash.model.Offer

object WatchViewModel : ViewModel() {
    fun getWatchData() : LiveData<List<Offer>> = OfferDatabase.getWatchData()
    fun setWatch(offer: Offer) : Unit = OfferDatabase.setWatch(offer)
    fun deleteWatch(offer: Offer) : Unit = OfferDatabase.deleteWatch(offer)
    fun bid(offer: Offer) : Unit = OfferDatabase.bid(offer)
    fun getUid() : String = OfferDatabase.getUid()
}