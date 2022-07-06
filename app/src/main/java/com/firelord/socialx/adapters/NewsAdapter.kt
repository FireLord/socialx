package com.firelord.socialx.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firelord.socialx.R
import com.firelord.socialx.models.Article
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder> (){

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var ivNewsImage : ImageView = itemView.findViewById(R.id.ivNewsImage)
        var tvTime : TextView = itemView.findViewById(R.id.tvTime)
        var tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        var tvSource : TextView = itemView.findViewById(R.id.tvSource)
        var tvDetails : TextView = itemView.findViewById(R.id.tvDetails)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val  differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.news_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun String.getPrettyDateTime(): String {
        val readFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        readFormatter.timeZone = TimeZone.getDefault()

        var result = ""
        val suffix = "ago"
        try {
            val pastTime = readFormatter.parse(this)
            val now = Date()

            val diff = now.time - (pastTime?.time ?: 0)
            val second = TimeUnit.MILLISECONDS.toSeconds(diff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(diff)
            val hour = TimeUnit.MILLISECONDS.toHours(diff)
            val day = TimeUnit.MILLISECONDS.toDays(diff)

            if (second < 60) {
                result = "$second Seconds $suffix"
            } else if (minute < 60) {
                result = "$minute Minutes $suffix"
            } else if (hour < 24) {
                result = "$hour Hours $suffix"
            } else if (day >= 7) {
                result = if (day > 360) {
                    "${(day / 360)} Years $suffix"
                } else if (day > 30) {
                    "${(day / 30)} Months $suffix"
                } else {
                    "${(day / 7)} Week $suffix"
                }
            } else if (day < 7) {
                result = "$day Days $suffix"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            result = this
        }

        return result
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.ivNewsImage)
            holder.tvSource.text = article.source?.name
            holder.tvTitle.text = article.title
            holder.tvTime.text = article.publishedAt?.getPrettyDateTime()
            holder.tvDetails.text = article.description
            setOnClickListener {
                val uri: Uri = Uri.parse(article.url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        }
    }
}