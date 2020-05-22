package com.enbcreative.demonoteapp.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.data.db.AppDatabase
import com.enbcreative.demonoteapp.data.network.WebApi
import com.enbcreative.demonoteapp.data.repository.UserRepository
import com.enbcreative.demonoteapp.databinding.FragmentLoginBinding
import com.enbcreative.demonoteapp.ui.auth.AuthViewModel
import com.enbcreative.demonoteapp.ui.auth.AuthViewModelFactory
import com.enbcreative.demonoteapp.ui.auth.ProcessListener
import com.enbcreative.demonoteapp.utils.hide
import com.enbcreative.demonoteapp.utils.logd
import com.enbcreative.demonoteapp.utils.show
import com.enbcreative.demonoteapp.utils.toast
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), ProcessListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val webApi = WebApi()
        val db = AppDatabase(requireContext())
        val repository = UserRepository(webApi, db)
        val factory = AuthViewModelFactory(repository)

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

        initViews()
    }

    private fun initViews() {
        val userMail = edt_user_mail_login
        val userPassword = edt_password_login
        val loginButton = btn_login
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

            loginButton.setOnClickListener {
                progress_bar_loading_login.show()

            }
        }
    }

    override fun onStarted() {
        progress_bar_loading_login.show()
        requireContext().toast("Login started")
    }

    override fun onSuccess() {
        progress_bar_loading_login.hide()

        requireContext().toast("Login finished Successfully!")
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

    private fun areFieldsValid(email: String, password: String) {
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

    /**
     * Extension function to simplify setting on afterTextChanged action to EditText components
     */
    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}