package com.example.favdish.view.fragments


import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar


import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.navArgs

import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentDetailDishBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.utils.Constants
import com.example.favdish.view.activities.MainActivity

import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory

class DetailDishFragment : Fragment() {

    private val args : DetailDishFragmentArgs by navArgs()

    private var mImagePath: String = ""

    private var favouriteDish: Boolean = false

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireContext().applicationContext as FavDishApplication).repository)
    }

    private lateinit var mBinding: FragmentDetailDishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDetailDishBinding.inflate(layoutInflater, container, false)

        (activity as MainActivity).supportActionBar?.setTitle(R.string.toolbar_detail_fragment_name)

        favouriteDish = args.favouriteDish
        if (favouriteDish){
            mBinding.ivFavoriteDish.setImageResource(R.drawable.ic__heart_full)
            favouriteDish = true
        }else{
            mBinding.ivFavoriteDish.setImageResource(R.drawable.ic__heart_blank)
        }

        Glide.with(requireContext()).load(args.image).into(mBinding.ivDishImage)

        mImagePath = args.image
        mBinding.etTitle.setText(args.title)
        mBinding.etType.setText(args.type)
        mBinding.etCategory.setText(args.category)
        mBinding.etIngredients.setText(args.ingredients)
        mBinding.etCookingTimeInMinutes.setText(args.cookingTime)
        mBinding.etDirectionToCook.setText(args.cookingInstructions)

        mBinding.ivFavoriteDish.setOnClickListener{
            favouriteDish = if (favouriteDish){
                val favDishFavorite: FavDish = FavDish(
                    mImagePath,
                    Constants.DISH_IMAGE_SOURCE_LOCAL,
                    args.title,
                    args.type,
                    args.category,
                    args.ingredients,
                    args.cookingTime,
                    args.cookingInstructions,
                    false,
                    args.id
                )
                mFavDishViewModel.update(favDishFavorite)
                mBinding.ivFavoriteDish.setImageResource(R.drawable.ic__heart_blank)
                false
            }else{
                val favDishNOFavourite: FavDish = FavDish(
                    mImagePath,
                    Constants.DISH_IMAGE_SOURCE_LOCAL,
                    args.title,
                    args.type,
                    args.category,
                    args.ingredients,
                    args.cookingTime,
                    args.cookingInstructions,
                    true,
                    args.id
                )
                mFavDishViewModel.update(favDishNOFavourite)
                mBinding.ivFavoriteDish.setImageResource(R.drawable.ic__heart_full)
                true
            }
        }

        return mBinding.root
    }

}