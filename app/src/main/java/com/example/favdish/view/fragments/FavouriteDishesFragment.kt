package com.example.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentFavouriteDishesBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.view.adapters.FavDishAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory

class FavouriteDishesFragment : Fragment(),FavDishAdapter.ItemClicked {

    private lateinit var mBinding: FragmentFavouriteDishesBinding
    private val favDishAdapter: FavDishAdapter = FavDishAdapter(this, this)

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireContext().applicationContext as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFavouriteDishesBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        mBinding.rvDishesList.adapter = favDishAdapter
        mFavDishViewModel.favDishesList.observe(viewLifecycleOwner) { dishes ->

            if (dishes.isNotEmpty()) {
                mBinding.rvDishesList.visibility = View.VISIBLE
                mBinding.tvNoDishesAddedYet.visibility = View.GONE
                favDishAdapter.dishesList(dishes)
            } else {
                mBinding.rvDishesList.visibility = View.GONE
                mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
            }

        }
    }

    override fun onDelete(favDish: FavDish) {
        mFavDishViewModel.delete(favDish)
    }

    override fun onUpdate(favDish: FavDish) {
        findNavController().navigate(AllDishesFragmentDirections.actionNavigationAllDishesToEditDishFragment(favDish))
    }

    override fun onItemClicked(favDish: FavDish) {
        findNavController().navigate(
            FavouriteDishesFragmentDirections.actionNavigationFavouriteDishesToDetailDishFragment(
                favDish.image,
                favDish.title,
                favDish.type,
                favDish.category,
                favDish.ingredients,
                favDish.cookingTime,
                favDish.directionToCook,
                favDish.imageSource,
                favDish.favouriteDish,
                favDish.id
            )
        )
    }


}