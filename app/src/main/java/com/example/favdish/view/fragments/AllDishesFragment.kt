package com.example.favdish.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentAllDishesBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.view.adapters.FavDishAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory


class AllDishesFragment : Fragment(), FavDishAdapter.ItemClicked {

    private lateinit var mBinding: FragmentAllDishesBinding

    private val favDishAdapter: FavDishAdapter = FavDishAdapter(this@AllDishesFragment, this)

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
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
        mBinding = FragmentAllDishesBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        mBinding.rvDishesList.adapter = favDishAdapter
        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_dish -> {
                 findNavController().navigate(AllDishesFragmentDirections.actionNavigationAllDishesToAddUpdateFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDelete(favDish: FavDish) {
//        executor.execute{
//            FavDishRoomDatabase.getDatabase(requireContext()).favDishDao().onDelete(favDish)
//        }
        mFavDishViewModel.delete(favDish)
    }

    override fun onUpdate(favDish: FavDish) {
         findNavController().navigate(AllDishesFragmentDirections.actionNavigationAllDishesToEditDishFragment(favDish))
    }

    override fun onItemClicked(favDish: FavDish) {
        findNavController().navigate(
            AllDishesFragmentDirections.actionNavigationAllDishesToDetailDishFragment(
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