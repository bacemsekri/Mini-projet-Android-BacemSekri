package com.mbds.bmp.newsletter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mbds.bmp.newsletter.MainActivity
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.adapters.CategoryAdapter
import com.mbds.bmp.newsletter.data.Data
import com.mbds.bmp.newsletter.databinding.FragmentSelectorsBinding
import com.mbds.bmp.newsletter.model.Category


class CategoriesFragment: Fragment() {

    private lateinit var binding: FragmentSelectorsBinding
    private lateinit var categories: List<Category>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //create and bind list of categories

        val recyclerView = binding.recyclerView
        categories = Data.getCategoryList()
        categories.find { category -> category.key.isNullOrBlank() }?.active = true
        val categoriesAdapter = CategoryAdapter(categories)

        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.tag = R.layout.item_category
        recyclerView.hasFixedSize()
        recyclerView.adapter = categoriesAdapter

        activity?.setTitle(R.string.category)
        binding.nextButton.text = context?.getText(R.string.select_country)
        binding.nextButton.setOnClickListener {
            goToNextSelector()
        }
    }

    private fun goToNextSelector() {
        //Go To Countries Fragment
        val result =
            categories.find { category -> category.active } ?: Category(R.string.all, null, "")
        val mainActivity = activity as MainActivity
        mainActivity.changeFragment(CountriesFragment.newInstance(result))
    }
}