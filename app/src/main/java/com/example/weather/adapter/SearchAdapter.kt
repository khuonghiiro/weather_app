package com.example.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.here.sdk.search.Place
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(context: Context, private val listSearch: List<Place>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    private lateinit var itemClickListener: ItemClickListener


    fun setOnItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(item: View, listener: ItemClickListener) : RecyclerView.ViewHolder(item) {
        val tvSearch: TextView = item.tv_search

        init {
            item.setOnClickListener {
                val adapterPosition = adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(adapterPosition)
                }
            }
        }
    }

    interface ItemClickListener {
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_search, parent, false)
        return ViewHolder(view, itemClickListener)
    }


    override fun getItemCount(): Int = listSearch.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = listSearch[position]

        holder.tvSearch.text = place.address.addressText

    }
}