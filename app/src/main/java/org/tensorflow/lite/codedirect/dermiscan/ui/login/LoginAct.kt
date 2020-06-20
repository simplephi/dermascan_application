package org.tensorflow.lite.codedirect.dermiscan.ui.login

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.codedirect.audiometer.utils.SessionManager
import kotlinx.android.synthetic.main.act_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.tensorflow.lite.codedirect.dermiscan.R
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.ResponseJSON
import org.tensorflow.lite.codedirect.dermiscan.ui.main.MainAct
import org.tensorflow.lite.codedirect.dermiscan.ui.register.RegisterAct
import org.tensorflow.lite.codedirect.dermiscan.utils.Utils
import org.tensorflow.lite.codedirect.dermiscan.utils.common.Status
import org.tensorflow.lite.codedirect.dermiscan.utils.hideToolbar

class LoginAct : AppCompatActivity(), View.OnClickListener {

    private val model by viewModel<LoginVM>()
    private val sessionManager by lazy {
        SessionManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)

        hideToolbar()
        setupPermissions()
        setupChechSession()
        setupOnClickListener()
    }

    private fun setupPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)
        }
    }

    private fun setupChechSession() {
        if (sessionManager.getLogin() == true) {
            startActivity(Intent(this, MainAct::class.java))
            finish()
        }
    }

    private fun setupOnClickListener() {
        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> loginUsers()
            R.id.tv_register -> navigateToRegister()
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterAct::class.java))
    }

    private fun loginUsers() {
        model.getUsers(
                ed_login_username.text.toString(),
                ed_login_password.text.toString()
        ).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        login_loading.visibility = View.GONE
                        resource.data.let { users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        login_loading.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        login_loading.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: ResponseJSON?) {
        if (users?.msg.equals(getString(R.string.success_))) {
            sessionManager.setLogin(true)
            sessionManager.setIDUser(users?.doc?.get(0)?.id.toString())
            sessionManager.setUsername(users?.doc?.get(0)?.username.toString())
            sessionManager.setRoleUser(users?.doc?.get(0)?.email.toString())
            startActivity(Intent(this, MainAct::class.java))
            finish()
        } else {
            Utils.toast(this, users?.msg.toString())
        }
    }
}