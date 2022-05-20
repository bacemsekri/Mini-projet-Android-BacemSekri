package com.mbds.bmp.newsletter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mbds.bmp.newsletter.MainActivity
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.adapters.CountryAdapter
import com.mbds.bmp.newsletter.data.Data
import com.mbds.bmp.newsletter.databinding.FragmentSelectorsBinding
import com.mbds.bmp.newsletter.model.Category
import com.mbds.bmp.newsletter.model.Country

class CountriesFragment: Fragment() {

    private lateinit var binding: FragmentSelectorsBinding
    private var category: Category? = null
    private lateinit var countries: List<Country>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getSerializable(ARG_CATEGORY) as Category
        }
    }

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

        //create and bind list of countries

        val recyclerView = binding.recyclerView
        countries = Data.getCountryList()
        countries.find { country -> country.countryCode == null }?.active = true
        val countriesAdapter = CountryAdapter(countries)

        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.tag = R.layout.item_country
        recyclerView.hasFixedSize()
        recyclerView.adapter = countriesAdapter

        binding.nextButton.text = context?.getText(R.string.select_editors)
        binding.nextButton.setOnClickListener {
            goToNextSelector()
        }

        activity?.setTitle(R.string.country)
    }

    private fun goToNextSelector() {
        //Go To Editor Fragment
        val result = countries.find { country -> country.active } ?: Country(null)
        val mainActivity = activity as MainActivity
        mainActivity.changeFragment(
            EditorsFragment.newInstance(
                category ?: Category(
                    R.string.all,
                    null,
                    ""
                ), result
            )
        )
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        @JvmStatic
        fun newInstance(category: Category) =
            CountriesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CATEGORY, category)
                }
            }
    }
}