package com.example.lenz

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchRVAdapter(private val context: ArrayList<SearchRVModel>, private val searchRVModels: MainActivity):RecyclerView.Adapter<SearchRVAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: TextView = itemView.findViewById(R.id.TVTitle)
        val descTV: TextView = itemView.findViewById(R.id.TVDescription)
        val snippetTV: TextView = itemView.findViewById(R.id.TVSnippet)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_results_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modal = searchRVModels[position]
        holder.titleTV.text = modal.title
        holder.snippetTV.text = modal.displayedLink
        holder.descTV.text = modal.snippet

        holder.itemView.setOnClickListener {
            // Opening a link in the default browser.
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(modal.link)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return searchRVModels.size
    }


}