package com.nammaplatform

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainAdapter(
    private val context: Context,
    private val trains: List<Train>
) : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    inner class TrainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTrainNumber: TextView = itemView.findViewById(R.id.tvTrainNumber)
        val tvTrainName: TextView = itemView.findViewById(R.id.tvTrainName)
        val tvDestination: TextView = itemView.findViewById(R.id.tvDestination)
        val tvPlatformNumber: TextView = itemView.findViewById(R.id.tvPlatformNumber)
        val tvArrivalTime: TextView = itemView.findViewById(R.id.tvArrivalTime)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val llCoachPreview: LinearLayout = itemView.findViewById(R.id.llCoachPreview)
        val btnViewDetails: Button = itemView.findViewById(R.id.btnViewDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_train, parent, false)
        return TrainViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val train = trains[position]

        holder.tvTrainNumber.text = train.number
        holder.tvTrainName.text = train.nameKannada
        holder.tvDestination.text = "→ ${train.destinationKannada}"
        holder.tvPlatformNumber.text = train.platform.toString()
        holder.tvArrivalTime.text = "ಬರುವ ಸಮಯ: ${train.arrivalTime}"
        holder.tvStatus.text = train.statusKannada

        // Status color
        if (train.status == "on_time") {
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#C62828"))
        }

        // Coach preview (compact mode)
        CoachViewHelper.populateCoachLayout(
            context = context,
            container = holder.llCoachPreview,
            coaches = train.coaches,
            compact = true
        )

        // Navigate to detail screen
        holder.btnViewDetails.setOnClickListener {
            val intent = Intent(context, PlatformDetailActivity::class.java).apply {
                putExtra(PlatformDetailActivity.EXTRA_TRAIN_INDEX, position)
                putExtra(PlatformDetailActivity.EXTRA_STATION_ID, (context as TrainListActivity).stationId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = trains.size
}
