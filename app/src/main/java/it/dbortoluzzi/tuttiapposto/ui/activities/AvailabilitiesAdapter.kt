package it.dbortoluzzi.tuttiapposto.ui.activities

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.ViewAvailabilityItemBinding
import it.dbortoluzzi.tuttiapposto.model.Availability
import kotlin.properties.Delegates

// TODO: refactor with BookingsAdapter using a custom Adapter
class AvailabilitiesAdapter(private val emptyView: View) : RecyclerView.Adapter<AvailabilitiesAdapter.ViewHolder>() {

    var items: List<Availability> by Delegates.observable(emptyList()) { _, _, items ->
        run {
            notifyDataSetChanged()
            if (items.isEmpty()) {
                emptyView.visibility = View.VISIBLE
            } else {
                emptyView.visibility = View.GONE
            }
        }
    }

    var onItemClick: ((Availability, View) -> Unit)? = null
    var onItemLongPress: ((Availability, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewAvailabilityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick, onItemLongPress)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ViewAvailabilityItemBinding,
                     var onItemClick: ((Availability, View) -> Unit)? = null,
                     private val onItemLongPress: ((Availability, View) -> Unit)? = null) : RecyclerView.ViewHolder(binding.root) {

        fun bind(availability: Availability) {
            with(availability) {
                binding.tableName.text = availability.tableName
                binding.roomName.text = availability.roomName

                binding.root.setOnClickListener {
                    onItemClick?.invoke(availability, binding.root)
                }

                binding.root.setOnLongClickListener {
                    onItemLongPress?.invoke(availability, binding.root)
                    return@setOnLongClickListener true
                }

                if (availabilityNumber > 5) {
                    binding.availabilityNumber.text = "$availabilityNumber ${itemView.context.getString(R.string.a_lot_of_tables)}"
                } else if (availabilityNumber >= 2){
                    binding.availabilityNumber.text = "${itemView.context.getString(R.string.only_tables)} $availabilityNumber"
                } else if (availabilityNumber == 1) {
                    binding.availabilityNumber.setTextColor(binding.root.resources.getColor(R.color.colorAccent))
                    binding.availabilityNumber.text = itemView.context.getString(R.string.last_table)
                }

                if (availability.tableAvailabilityResponseDto.reported) {
                    binding.reportImageView.visibility = View.VISIBLE
                } else {
                    binding.reportImageView.visibility = View.GONE
                }
            }
        }
    }
}
