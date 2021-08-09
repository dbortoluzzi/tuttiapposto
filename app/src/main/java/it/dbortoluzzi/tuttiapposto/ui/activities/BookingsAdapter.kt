package it.dbortoluzzi.tuttiapposto.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.dbortoluzzi.data.databinding.ViewBookingItemBinding
import it.dbortoluzzi.tuttiapposto.model.BookingAggregate
import kotlin.properties.Delegates

class BookingsAdapter : RecyclerView.Adapter<BookingsAdapter.ViewHolder>() {

    var items: List<BookingAggregate> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }
    var onItemClick: ((BookingAggregate, View) -> Unit)? = null
    var onItemLongPress: ((BookingAggregate, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick, onItemLongPress)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ViewBookingItemBinding,
                     var onItemClick: ((BookingAggregate, View) -> Unit)? = null,
                     private val onItemLongPress: ((BookingAggregate, View) -> Unit)? = null) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookingAggregate: BookingAggregate) {
            with(bookingAggregate) {
                binding.companyName.text = bookingAggregate.companyName
                binding.tableName.text = bookingAggregate.tableName
                binding.roomName.text = bookingAggregate.roomName
                binding.dateFirstPart.text = bookingAggregate.dateFirstDescription
                binding.dateSecondPart.text = bookingAggregate.dateSecondDescription

                binding.root.setOnClickListener {
                    onItemClick?.invoke(bookingAggregate, binding.root)
                }

                binding.root.setOnLongClickListener {
                    onItemLongPress?.invoke(bookingAggregate, binding.root)
                    return@setOnLongClickListener true
                }
            }
        }
    }
}
