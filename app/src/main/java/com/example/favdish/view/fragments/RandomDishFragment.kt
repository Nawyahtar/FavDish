package com.example.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.background.WorkNotification
import com.example.favdish.databinding.FragmentRandomDishBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.utils.Constants
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import com.example.favdish.viewmodel.RandomDishViewModel
import com.example.favdish.viewmodel.RandomDishViewModelFactory
import java.util.concurrent.TimeUnit

class RandomDishFragment : Fragment() {
    private lateinit var mBinding: FragmentRandomDishBinding

    private var mImagePath: String = ""

    private lateinit var randomDishDetail: FavDish

    private val mFavSDishViewModel: RandomDishViewModel by viewModels {
        RandomDishViewModelFactory((requireContext().applicationContext as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentRandomDishBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFavSDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            if (dishes.isNotEmpty()) {
                bindRandomDish()
                mBinding.swiperFresh.visibility = View.VISIBLE
                mBinding.tvNoDishesAddedYet.visibility = View.GONE
            } else {
                mBinding.swiperFresh.visibility = View.GONE
                mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
            }

        }
        mBinding.swiperFresh.setOnRefreshListener {
            mFavSDishViewModel.randomDishLoad()
            mBinding.swiperFresh.isRefreshing = false
        }
        mBinding.ivFavoriteDish.setOnClickListener {
            val dish = randomDishDetail.copy(favouriteDish = !randomDishDetail.favouriteDish)
            mFavSDishViewModel.update(dish)
        }

    }

    private fun bindRandomDish() {

        mFavSDishViewModel.randomDish.observe(viewLifecycleOwner) { randomDish ->
            randomDishDetail = randomDish
            Glide.with(requireContext()).load(randomDish.image).into(mBinding.ivDishImage)
            if (randomDish.favouriteDish) {
                mBinding.ivFavoriteDish.setImageResource(R.drawable.ic__heart_full)

            } else {
                mBinding.ivFavoriteDish.setImageResource(R.drawable.ic__heart_blank)

            }
            mImagePath = randomDish.image

            mBinding.etTitle.setText(randomDish?.title)
            mBinding.etType.setText(randomDish?.type)
            mBinding.etCategory.setText(randomDish?.category)
            mBinding.etIngredients.setText(randomDish?.ingredients)
            mBinding.etCookingTimeInMinutes.setText(randomDish?.cookingTime)
            mBinding.etDirectionToCook.setText(randomDish?.directionToCook)
        }

    }

}


