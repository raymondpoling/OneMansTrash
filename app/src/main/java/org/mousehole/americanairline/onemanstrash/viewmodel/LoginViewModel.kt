package org.mousehole.americanairline.onemanstrash.viewmodel

import androidx.lifecycle.ViewModel
import org.mousehole.americanairline.onemanstrash.data.repository.OfferDatabase

object LoginViewModel : ViewModel() {
    fun loggedInVerifiedUser() = OfferDatabase.loggedInVerifiedUser()
    fun requestValidationEmail() = OfferDatabase.requestValidationEmail()
}