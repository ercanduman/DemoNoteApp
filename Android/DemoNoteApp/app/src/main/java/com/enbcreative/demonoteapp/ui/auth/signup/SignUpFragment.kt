package com.enbcreative.demonoteapp.ui.auth.signup

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
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.databinding.FragmentSignUpBinding
import com.enbcreative.demonoteapp.ui.auth.AuthViewModel
import com.enbcreative.demonoteapp.ui.auth.AuthViewModelFactory
import com.enbcreative.demonoteapp.ui.auth.ProcessListener
import com.enbcreative.demonoteapp.utils.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class SignUpFragment : Fragment(), KodeinAware, ProcessListener {
    override val kodein by closestKodein()
    private val factory by instance<AuthViewModelFactory>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSignUpBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        val viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_sign_in_here.setOnClickListener { activity?.onBackPressed() }
        initViews()
    }

    override fun onStarted() {
        progress_bar_sign_up.show()
    }

    override fun onSuccess() {
        progress_bar_sign_up.hide()
        requireContext().toast("Sign Up finished Successfully! Start New Activity")
    }

    override fun onSuccessResult(result: LiveData<String>) {
        result.observe(this, Observer {
            tv_sign_in_here.text = it
            progress_bar_sign_up.hide()
        })
    }

    override fun onFailure(message: String) {
        requireContext().toast("Execution failed: $message")
        logd("Execution failed: $message")
        progress_bar_sign_up.hide()
    }

    private fun initViews() {
        edt_sign_up_name.afterTextChanged { areFieldsValid() }
        edt_sign_up_email.afterTextChanged { areFieldsValid() }
        edt_sign_up_password.afterTextChanged { areFieldsValid() }
        edt_sign_up_password_confirm.apply {
            afterTextChanged { areFieldsValid() }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) areFieldsValid()
                false
            }
        }
    }

    private fun isNameValid(password: String): Boolean = password.length > 2
    private fun isPasswordValid(password: String): Boolean = password.length > 5
    private fun passwordMatches(pass: String, passConfirm: String) = pass == passConfirm
    private fun isEmailValid(email: String): Boolean =
        email.isNotBlank() and Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun areFieldsValid() {
        btn_sign_up.isEnabled = false
        when {
            isNameValid(edt_sign_up_name.text.toString()).not() -> {
                edt_sign_up_name.error = getString(R.string.invalid_name)
            }
            isEmailValid(edt_sign_up_email.text.toString()).not() -> {
                edt_sign_up_email.error = getString(R.string.invalid_user_mail)
            }
            isPasswordValid(edt_sign_up_password.text.toString()).not() -> {
                edt_sign_up_password.error = getString(R.string.invalid_password)
            }
            isPasswordValid(edt_sign_up_password_confirm.text.toString()).not() -> {
                edt_sign_up_password_confirm.error = getString(R.string.invalid_password)
            }
            passwordMatches(
                edt_sign_up_password.text.toString(),
                edt_sign_up_password_confirm.text.toString()
            ).not() -> {
                edt_sign_up_password_confirm.error =
                    getString(R.string.invalid_passwords_not_matched)
            }
            else -> btn_sign_up.isEnabled = true
        }
    }
}
