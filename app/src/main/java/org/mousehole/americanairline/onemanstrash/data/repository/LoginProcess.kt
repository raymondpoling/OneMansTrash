package org.mousehole.americanairline.onemanstrash.data.repository

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.mousehole.americanairline.onemanstrash.utils.QuickToast
import org.mousehole.americanairline.onemanstrash.view.MainActivity

object LoginProcess {

    interface SuccessfulLoggedIn {
        fun requireContext(): Context
        fun requireFragmentManager(): FragmentManager
        fun loggedIn()
        fun clear()
    }

    fun createUser(email: String, password: String, successfulLoggedIn: SuccessfulLoggedIn) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(
                        email,
                        password
                ).addOnCompleteListener { p0 ->
                    when {
                        p0.isSuccessful &&
                                OfferDatabase.loggedInVerifiedUser() -> {
                            OfferDatabase.loggedInVerifiedUser()
                            successfulLoggedIn.loggedIn()
                        }
                        p0.isSuccessful -> {
                            OfferDatabase.requestValidationEmail()
                            QuickToast.showMessage(
                                    successfulLoggedIn.requireContext(),
                                    "Please check your email to verify your account"
                            )
                            successfulLoggedIn.clear()
                            successfulLoggedIn.requireFragmentManager().popBackStack()
                        }
                        else -> {
                            QuickToast.showMessage(
                                    successfulLoggedIn.requireContext(),
                                    "Login failed. Please check your email and password, " +
                                            "or register an account"
                            )
                        }
                    }
                }
    }

    fun loginUser(email: String, password: String, successfulLoggedIn: SuccessfulLoggedIn) {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        when {
                            p0.isSuccessful &&
                                    OfferDatabase.loggedInVerifiedUser() -> {
                                successfulLoggedIn.loggedIn()
                            }
                            p0.isSuccessful -> {
                                QuickToast.showMessage(successfulLoggedIn.requireContext(),
                                        "Please check your email to verify your account")
                            }
                            else -> {
                                QuickToast.showMessage(successfulLoggedIn.requireContext(),
                                        "Login failed. Please check your email and password, " +
                                                "or register an account")
                            }
                        }
                    }
                })
    }
}