package org.tensorflow.lite.codedirect.dermiscan.ui.main.features.profile

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.codedirect.audiometer.utils.SessionManager
import kotlinx.android.synthetic.main.act_login.*
import kotlinx.android.synthetic.main.frag_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.tensorflow.lite.codedirect.dermiscan.R
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.ResponseJSON
import org.tensorflow.lite.codedirect.dermiscan.ui.login.LoginAct
import org.tensorflow.lite.codedirect.dermiscan.utils.common.Status
import org.tensorflow.lite.codedirect.dermiscan.utils.hideToolbar

class ProfileFrag : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_profile, container, false)
    }

    private val sessionManager by lazy {
        SessionManager(requireContext())
    }
    private val model by viewModel<ProfileVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideToolbar()
        setupClickListener()
        //setupLoadData()
        ed_profile_email.text = SpannableStringBuilder(sessionManager.getRoleUser().toString())
        ed_profile_email.isEnabled = false
        ed_profile_username.text = SpannableStringBuilder(sessionManager.getUsername().toString())
        ed_profile_username.isEnabled = false
    }

    private fun setupLoadData() {
        model.getUsersProfile(
                sessionManager.getIDUser().toString()
        ).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loading_profile.visibility = View.GONE
                        resource.data.let { users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        loading_profile.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        loading_profile.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: ResponseJSON?) {
        ed_profile_email.text = SpannableStringBuilder(sessionManager.getRoleUser().toString())
        ed_profile_username.text = SpannableStringBuilder(sessionManager.getUsername().toString())
    }

    private fun setupClickListener() {
        btn_logout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_logout -> doLogout()
        }
    }

    private fun doLogout() {
        sessionManager.setLogin(false)
        startActivity(Intent(requireActivity(), LoginAct::class.java))
        requireActivity().finish()
    }

}