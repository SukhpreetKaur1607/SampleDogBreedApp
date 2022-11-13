package com.visualdx.dogbreedapp.ui.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.visualdx.dogbreedapp.AppApplication
import com.visualdx.dogbreedapp.databinding.FragmentMainBinding
import com.visualdx.dogbreedapp.network.model.DogBreed
import com.visualdx.dogbreedapp.network.repositories.MyModelFactory
import com.visualdx.dogbreedapp.ui.viewModel.MainFragmentViewModel
import javax.inject.Inject


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Inject
    lateinit var factory: MyModelFactory
    lateinit var viewModel: MainFragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as AppApplication).applicationComponent.inject(this)
        viewModel = ViewModelProvider(this, factory).get(MainFragmentViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.tvAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedBreed = parent.adapter.getItem(position) as DogBreed
            viewModel.setSelection(selectedBreed)
            binding.tvAutoComplete.setText("")

            if (selectedBreed.subBreed.size > 0) viewModel.getRandomImagesByBreed() else viewModel.getRandomImages()

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }
}


