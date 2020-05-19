package com.enbcreative.demonoteapp.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enbcreative.demonoteapp.R
import com.enbcreative.demonoteapp.databinding.FragmentLoginBinding
import com.enbcreative.demonoteapp.ui.auth.AuthViewModel
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

        val viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
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
    }

    override fun onStarted() {
        loading.show()
        requireContext().toast("Login started")
    }

    override fun onSuccess() {
        loading.hide()
        requireContext().toast("Login finished")
    }

    override fun onSuccessResult(result: LiveData<String>) {
        result.observe(this, Observer {
            tv_login_sign_up.text = it
            loading.hide()
        })
    }

    override fun onFailure(message: String) {
        requireContext().toast("Login failed")
        logd("Execution failed: $message")
        loading.hide()
    }
}