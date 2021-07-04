package it.dbortoluzzi.tuttiapposto.ui.activities

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.ViewAvailabilityItemBinding
import it.dbortoluzzi.tuttiapposto.model.Availability
import javax.inject.Inject
import kotlin.properties.Delegates

class AvailabilitiesAdapter @Inject constructor(): RecyclerView.Adapter<AvailabilitiesAdapter.ViewHolder>() {

    var items: List<Availability> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewAvailabilityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ViewAvailabilityItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(availability: Availability) {
            with(availability) {
                binding.tableName.text = availability.tableName
                binding.roomName.text = availability.roomName

                if (availabilityNumber > 10) {
                    binding.availabilityNumber.text = "$availabilityNumber ${itemView.context.getString(R.string.a_lot_of_tables)}"
                } else if (availabilityNumber >= 2){
                    binding.availabilityNumber.text = "${itemView.context.getString(R.string.only_tables)} $availabilityNumber"
                } else if (availabilityNumber == 1) {
                    binding.availabilityNumber.setTextColor(Color.RED)
                    binding.availabilityNumber.text = itemView.context.getString(R.string.last_table)
                }
            }
        }
    }
}
