package com.example.campaccessoriesinformation

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campaccessoriesinformation.R

class IteamAdapter(
    private val iteamList: List<Iteam>,
    private val context: Context,
    private val userRole: String,  // Added userRole parameter
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<IteamAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.iteam_image)
        val name: TextView = view.findViewById(R.id.iteam_name)
        val type: TextView = view.findViewById(R.id.iteam_type)
        val description: TextView = view.findViewById(R.id.iteam_description)
        val pricePerDay: TextView = view.findViewById(R.id.iteam_price_per_day)
        val editButton: ImageButton = view.findViewById(R.id.edit_button)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_iteam, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val iteam = iteamList[position]
        holder.name.text = iteam.name
        holder.type.text = iteam.type
        holder.description.text = iteam.description
        holder.pricePerDay.text = "Price per day: ${iteam.pricePerDay}"
        Glide.with(context).load(iteam.imagePath).into(holder.image)

        // Show or hide edit and delete buttons based on user role
        if (userRole == "admin") {
            holder.editButton.visibility = View.VISIBLE
            holder.deleteButton.visibility = View.VISIBLE

            holder.editButton.setOnClickListener {
                val intent = Intent(context, EditIteamActivity::class.java).apply {
                    putExtra("id", iteam.id)
                    putExtra("name", iteam.name)
                    putExtra("type",iteam.type)
                    putExtra("description", iteam.description)
                    putExtra("pricePerDay", iteam.pricePerDay)
                    putExtra("image", iteam.imagePath)
                }
                context.startActivity(intent)
            }

            holder.deleteButton.setOnClickListener {
                showDeleteConfirmationDialog(iteam.id)
            }
        } else {
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        }
    }

    private fun showDeleteConfirmationDialog(iteamId: Int) {
        AlertDialog.Builder(context).apply {
            setTitle("Delete iteam")
            setMessage("Are you sure you want to delete this iteam?")
            setPositiveButton("Yes") { _, _ -> onDelete(iteamId) }
            setNegativeButton("No", null)
        }.create().show()
    }

    override fun getItemCount(): Int {
        return iteamList.size
    }
}

