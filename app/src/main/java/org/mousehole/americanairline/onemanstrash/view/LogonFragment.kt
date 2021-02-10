package org.mousehole.americanairline.onemanstrash.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.mousehole.americanairline.onemanstrash.R
import org.mousehole.americanairline.onemanstrash.data.repository.LoginProcess
import org.mousehole.americanairline.onemanstrash.utils.DebugLogger.debug
import org.mousehole.americanairline.onemanstrash.utils.QuickToast.showMessage
import org.mousehole.americanairline.onemanstrash.validation.Fail
import org.mousehole.americanairline.onemanstrash.validation.PassFail
import org.mousehole.americanairline.onemanstrash.validation.Validation
import org.mousehole.americanairline.onemanstrash.validation.Validation.Companion.validateIsEmail

class LogonFragment : Fragment(), LoginProcess.SuccessfulLoggedIn {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var logonButton: Button
    private lateinit var signupTextView: TextView

    private val signupFragment = SignUpFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.logon_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bindings
        emailEditText = view.findViewById(R.id.username_edittext)
        passwordEditText = view.findViewById(R.id.password_edittext)
        logonButton = view.findViewById(R.id.logon_button)
        logonButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            when (val t = validate()) {
                is Fail -> {
                    showMessage(requireContext(), t.failMessages)
                }
                else -> {
                    LoginProcess.loginUser(email, password, this)
                }
            }
        }
            signupTextView = view.findViewById(R.id.sign_up_textview)
            signupTextView.setOnClickListener {
                debug("I was clicked.")
                requireFragmentManager()
                        .beginTransaction()
                        .replace(R.id.logon_window, signupFragment)
                        .addToBackStack(signupFragment.tag)
                        .commit()
            }
        }


    fun validate() : PassFail {
        return Validation({emailEditText.text.toString().trim().isNotBlank()},
                "EMail cannot be blank")
                .chain({validateIsEmail(emailEditText.text.toString().trim())},
                "EMail is not a valid EMail")
                .chain({passwordEditText.text.toString().trim().isNotBlank()},
                        "Password cannot be empty").exec()
    }

    override fun loggedIn() {
        clear()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun clear() {
        passwordEditText.text.clear()
        emailEditText.text.clear()
    }
}