package org.tensorflow.lite.codedirect.dermiscan.ui.main.features.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.tensorflow.lite.codedirect.dermiscan.R
import org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response.DocItems

class HomeAdapter(
        private val listener: (DocItems) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var mData = ArrayList<DocItems>()

    fun setData(data: ArrayList<DocItems>?) {
        if (data == null) return
        this.mData.clear()
        this.mData.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.rv_home,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(mData.get(position), listener)
    }

    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.item_home_name)
        val tvLabel = itemView.findViewById<TextView>(R.id.item_home_label)
        val ivStatus = itemView.findViewById<ImageView>(R.id.iv_home)

        fun bindItem(items: DocItems, listener: (DocItems) -> Unit) {
            tvTitle.text = items.title
            tvLabel.text = items.label
            Glide.with(itemView.context)
                    .load(items.path.toString())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(ivStatus)

            itemView.setOnClickListener { listener(items) }
        }
    }
}