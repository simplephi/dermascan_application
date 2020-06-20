package org.tensorflow.lite.codedirect.dermiscan.ui.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.act_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.tensorflow.lite.codedirect.dermiscan.R
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.ResponseJSON
import org.tensorflow.lite.codedirect.dermiscan.utils.Utils
import org.tensorflow.lite.codedirect.dermiscan.utils.common.Status
import org.tensorflow.lite.codedirect.dermiscan.utils.hideToolbar

class RegisterAct : AppCompatActivity(), View.OnClickListener {

    private val model by viewModel<RegisterVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_register)

        hideToolbar()
        setupClickListener()
    }

    private fun setupClickListener() {
        btn_register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> doRegister()
        }
    }

    private fun doRegister() {
        if (ed_register_password.text.toString() == ed_register_confirm_password.text.toString())
            model.usersRegister(
                    ed_register_username.text.toString(),
                    ed_register_email.text.toString(),
                    ed_register_password.text.toString()
            ).observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            register_loading.visibility = View.GONE
                            resource.data.let { users -> retrieveList(users) }
                        }
                        Status.ERROR -> {
                            register_loading.visibility = View.GONE
                        }
                        Status.LOADING -> {
                            register_loading.visibility = View.VISIBLE
                        }
                    }
                }
            })
        else
            Utils.toast(this, getString(R.string.password_not_match))
    }

    private fun retrieveList(users: ResponseJSON?) {
        if (users?.msg.equals(getString(R.string.success_))) {
            Utils.toast(this, users?.msg.toString())
            finish()
        }
    }

}