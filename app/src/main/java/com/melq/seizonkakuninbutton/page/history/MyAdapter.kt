package com.melq.seizonkakuninbutton.page.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.melq.seizonkakuninbutton.R
import java.util.*

class MyAdapter(private val historyList: MutableList<Timestamp>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPushedTime: TextView = view.findViewById(R.id.tv_pushed_time)
        val tvHoursAgo: TextView = view.findViewById(R.id.tv_hours_ago)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyData = historyList[position]

        holder.run {
            val calendar = Calendar.getInstance()
            calendar.time = historyData.toDate()
            val pushedTimeStr = "${calendar.get(Calendar.MONTH) + 1}月" +
                        "${calendar.get(Calendar.DATE)}日 " +
                        "${calendar.get(Calendar.HOUR)}:" +
                        "${calendar.get(Calendar.MINUTE)}:" +
                        "${calendar.get(Calendar.SECOND)}"
            tvPushedTime.text = pushedTimeStr

            val now = Calendar.getInstance().apply { time = Date() }
            val diffTime = now.timeInMillis - calendar.timeInMillis
            val millisOfHours = 1000 * 60 * 60
            val diffHours = diffTime / millisOfHours
            tvHoursAgo.text = "$diffHours 時間前"
        }
    }

    override fun getItemCount(): Int = historyList.size
}