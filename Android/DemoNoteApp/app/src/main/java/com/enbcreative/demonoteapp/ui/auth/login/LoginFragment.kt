package com.enbcreative.demonoteapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enbcreative.demonoteapp.LOGGED_ID
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.databinding.FragmentLoginBinding
import com.enbcreative.demonoteapp.ui.auth.AuthViewModel
import com.enbcreative.demonoteapp.ui.auth.AuthViewModelFactory
import com.enbcreative.demonoteapp.ui.auth.ProcessListener
import com.enbcreative.demonoteapp.ui.main.MainActivity
import com.enbcreative.demonoteapp.utils.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class LoginFragment : Fragment(), KodeinAware, ProcessListener {
    override val kodein by closestKodein()
    private val factory by instance<AuthViewModelFactory>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.listener = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_login_sign_up.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_SignUpFragment)
        }

        if (LOGGED_ID) startMainActivity()
        initViews()
    }

    override fun onStarted() {
        progress_bar_loading_login.show()
        requireContext().toast("Login started")
    }

    override fun onSuccess() {
        progress_bar_loading_login.hide()
        requireContext().toast("Login finished Successfully! Start New Activity")
        startMainActivity()
    }

    private fun startMainActivity() {
        Intent(requireContext(), MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
        }
    }

    override fun onSuccessResult(result: LiveData<String>) {
        result.observe(this, Observer {
            tv_login_sign_up.text = it
            progress_bar_loading_login.hide()
        })
    }

    override fun onFailure(message: String) {
        requireContext().toast("Login failed")
        logd("Execution failed: $message")
        progress_bar_loading_login.hide()
    }

    private fun initViews() {
        val userMail = edt_user_mail_login
        val userPassword = edt_password_login
        userMail.afterTextChanged {
            areFieldsValid(userMail.text.toString(), userPassword.text.toString())
        }

        userPassword.apply {
            afterTextChanged {
                areFieldsValid(userMail.text.toString(), userPassword.text.toString())
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        areFieldsValid(userMail.text.toString(), userPassword.text.toString())
                    }
                }
                false
            }

            /* loginButton.setOnClickListener {
                progress_bar_loading_login.show()
            }*/
        }
    }

    private fun areFieldsValid(email: String, password: String) {
        btn_login.isEnabled = false
        when {
            isUserEmailValid(email).not() -> {
                edt_user_mail_login.error = getString(R.string.invalid_user_mail)
            }
            isPassWordValid(password).not() -> {
                edt_password_login.error = getString(R.string.invalid_password)
            }
            else -> btn_login.isEnabled = true
        }
    }

    // A placeholder email check
    private fun isUserEmailValid(email: String): Boolean =
        email.isNotBlank() and Patterns.EMAIL_ADDRESS.matcher(email).matches()

    // A placeholder password check
    private fun isPassWordValid(password: String): Boolean = password.length > 5
}