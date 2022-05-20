package com.mbds.bmp.newsletter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mbds.bmp.newsletter.MainActivity
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.adapters.EditorAdapter
import com.mbds.bmp.newsletter.data.Data
import com.mbds.bmp.newsletter.databinding.FragmentSelectorsBinding
import com.mbds.bmp.newsletter.model.Category
import com.mbds.bmp.newsletter.model.Country
import com.mbds.bmp.newsletter.model.Editor
import com.mbds.bmp.newsletter.services.ArticleOnlineService
import com.mbds.bmp.newsletter.services.EditorService
import com.mbds.bmp.newsletter.tools.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorsFragment : Fragment() {

    private var category: Category? = null
    private var country: Country? = null
    private lateinit var binding: FragmentSelectorsBinding
    private lateinit var editorAdapter: EditorAdapter
    private lateinit var editors: List<Editor>
    private val editorService = EditorService()
    private var errorSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getSerializable(ARG_CATEGORY) as Category
            country = it.getSerializable(ARG_COUNTRY) as Country
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
        binding.nextButton.text = binding.root.context.getText(R.string.show_articles)
        binding.nextButton.isEnabled = false
        binding.nextButton.setOnClickListener {
            goToArticles()
        }

        lifecycleScope.launch {
            getData()
        }
    }

    private suspend fun getData() {
        withContext(Dispatchers.IO)
        {
            if (context?.isOnline() == true) {
                val result =
                    editorService.getEditors(category?.key ?: "", country?.countryCode?.name ?: "")
                if (result != null) {
                    bindData(result)
                } else {
                    displayError(R.string.request_error)
                }
            } else {
                displayError(R.string.no_internet_error)
            }
        }
    }

    private suspend fun bindData(result: List<Editor>) {
        withContext(Dispatchers.Main)
        {

            editors = result.toMutableList().apply {
                add(0, Data.getEditorForAll(binding.root.context))
            }

            editors.forEach { editor ->
                editor.active = true

                //listen change on category to block button when no category is selected.
                editor.addOnPropertyChangedCallback(object :
                    Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(observable: Observable, i: Int) {
                        binding.nextButton.isEnabled = editors.map { editor -> editor.active }
                            .reduce { e1, e2 -> e1 || e2 }
                    }
                })
            }

            val recyclerView = binding.recyclerView

            editorAdapter = EditorAdapter(editors)

            recyclerView.layoutManager = LinearLayoutManager(view?.context)
            recyclerView.hasFixedSize()
            recyclerView.adapter = editorAdapter

            activity?.setTitle(R.string.editors)
            binding.nextButton.isEnabled = true
        }
    }

    private suspend fun displayError(@StringRes errorId: Int) {
        withContext(Dispatchers.Main)
        {
            errorSnackBar = Snackbar.make(
                binding.root,
                errorId,
                BaseTransientBottomBar.LENGTH_INDEFINITE
            )
            errorSnackBar?.setAction(R.string.retry) {
                lifecycleScope.launch {
                    errorSnackBar?.dismiss()
                    getData()
                }
            }
            errorSnackBar?.show()
        }
    }

    private fun goToArticles() {
        //Go To Editor Fragment
        val result = editors.filter { country -> country.active && country.id != null }
        val articleOnlineService = ArticleOnlineService(
            category ?: Category(R.string.all, null, ""),
            country ?: Country(null),
            result
        )
        val mainActivity = activity as MainActivity
        mainActivity.changeFragment(
            ArticlesFragment.newInstance(articleOnlineService)
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        errorSnackBar?.dismiss()
    }


    companion object {
        private const val ARG_CATEGORY = "category"
        private const val ARG_COUNTRY = "country"

        @JvmStatic
        fun newInstance(category: Category, country: Country) =
            EditorsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CATEGORY, category)
                    putSerializable(ARG_COUNTRY, country)
                }
            }
    }
}