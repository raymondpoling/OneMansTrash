package org.mousehole.americanairline.onemanstrash.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import org.mousehole.americanairline.onemanstrash.R
import org.mousehole.americanairline.onemanstrash.data.repository.LoginProcess
import org.mousehole.americanairline.onemanstrash.utils.DebugLogger.debug
import org.mousehole.americanairline.onemanstrash.utils.QuickToast.showMessage
import org.mousehole.americanairline.onemanstrash.validation.Fail
import org.mousehole.americanairline.onemanstrash.validation.PassFail
import org.mousehole.americanairline.onemanstrash.validation.Validation
import org.mousehole.americanairline.onemanstrash.validation.Validation.Companion.validateIsEmail

class SignUpFragment : Fragment(), LoginProcess.SuccessfulLoggedIn {

    private lateinit var usernameEditText: EditText
    private lateinit var password1EditText: EditText
    private lateinit var password2EditText: EditText
    private lateinit var signUpButton: Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameEditText = view.findViewById(R.id.email_edittext)
        password1EditText = view.findViewById(R.id.password1_edittext)
        password2EditText = view.findViewById(R.id.password2_edittext)
        signUpButton = view.findViewById(R.id.sign_up_button)

        signUpButton.setOnClickListener {
            debug("We got value: ${password1EditText.text.toString().trim().isNotBlank()}")
            when (val v = validation()) {
                is Fail -> showMessage(requireContext(), v.failMessages)
                else -> {
                    LoginProcess.createUser(
                            usernameEditText.text.toString().trim(),
                            password1EditText.text.toString().trim(),
                    this)
                }
            }
        }
    }

    override fun clear() {
        usernameEditText.text.clear()
        password1EditText.text.clear()
        password2EditText.text.clear()
    }

    private fun validation(): PassFail =
            Validation({ usernameEditText.text.toString().trim().isNotBlank() },
                    "EMail cannot be empty")
                    .chain({ validateIsEmail(usernameEditText.text.toString().trim()) },
                            "This is not a valid email address")
                    .chain({
                        password1EditText.text.toString().trim() ==
                                password2EditText.text.toString().trim()
                    },
                            "Passwords do not match")
                    .chain({ password1EditText.text.toString().trim().isNotBlank() },
                            "Password cannot be empty")
                    .exec()

    override fun loggedIn() {
        clear()
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}