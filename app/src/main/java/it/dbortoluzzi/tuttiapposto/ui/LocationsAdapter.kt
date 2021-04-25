package it.dbortoluzzi.tuttiapposto.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.dbortoluzzi.data.databinding.ViewLocationItemBinding
import it.dbortoluzzi.tuttiapposto.ui.data.Location
import javax.inject.Inject
import kotlin.properties.Delegates

class LocationsAdapter @Inject constructor(): RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    var items: List<Location> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewLocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ViewLocationItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: Location) {
            with(location) {
                binding.locationCoordinates.text = coordinates
                binding.locationDate.text = date
            }
        }
    }
}
