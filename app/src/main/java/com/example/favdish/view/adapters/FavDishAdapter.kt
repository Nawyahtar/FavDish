package com.example.favdish.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.databinding.ItemDishLayoutBinding
import com.example.favdish.model.entities.FavDish


class FavDishAdapter(private val fragment: Fragment,private val itemClicked:ItemClicked) :
    RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = listOf()


    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ivMore = view.ivMoreVertical
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding =
            ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvTitle.text = dish.title
        holder.itemView.setOnClickListener{
            itemClicked.onItemClicked(dish)
        }
        holder.ivMore.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(fragment.requireContext(), holder.ivMore)
            popupMenu.menuInflater.inflate(R.menu.popup_menu_more, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        itemClicked.onUpdate(dish)
                    }
                    R.id.action_delete -> {
                        itemClicked.onDelete(dish)
                    }

                }
                true
            }
            popupMenu.show()

        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dishesList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }

    interface ItemClicked{
        fun onDelete(favDish: FavDish)
        fun onUpdate(favDish: FavDish)
        fun onItemClicked(favDish: FavDish)
    }


}