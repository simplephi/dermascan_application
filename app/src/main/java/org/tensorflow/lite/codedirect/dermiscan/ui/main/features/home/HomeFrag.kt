package org.tensorflow.lite.codedirect.dermiscan.ui.main.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.frag_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.tensorflow.lite.codedirect.dermiscan.R
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.DocItems
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.ResponseJSON
import org.tensorflow.lite.codedirect.dermiscan.utils.common.Status
import org.tensorflow.lite.codedirect.dermiscan.utils.hideToolbar

class HomeFrag : Fragment() {

    private val model by viewModel<HomeVM>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideToolbar()
        setupRvAdapter()
    }

    private fun setupRvAdapter() {
        model.getImages().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        loading_home.visibility = View.GONE
                        resource.data.let { users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        loading_home.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        loading_home.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: ResponseJSON?) {
        val adapter = HomeAdapter {}
        adapter.setData(users?.docs as ArrayList<DocItems>?)
        rv_home.layoutManager = LinearLayoutManager(requireContext())
        rv_home.setHasFixedSize(true)
        rv_home.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}