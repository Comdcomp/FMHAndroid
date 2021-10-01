package ru.netology.fmhandroid.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.netology.fmhandroid.R
import ru.netology.fmhandroid.databinding.FragmentFilterNewsBinding
import ru.netology.fmhandroid.dto.NewsFilterArgs
import ru.netology.fmhandroid.utils.Events
import ru.netology.fmhandroid.utils.Utils.saveDateTime
import ru.netology.fmhandroid.utils.Utils.updateDateLabel
import ru.netology.fmhandroid.viewmodel.NewsViewModel
import java.util.*

@AndroidEntryPoint
class FilterNewsListFragment : Fragment(R.layout.fragment_filter_news) {
    private lateinit var binding: FragmentFilterNewsBinding
    private lateinit var vPublishDateStartPicker: TextInputEditText
    private lateinit var vPublishDateEndPicker: TextInputEditText
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilterNewsBinding.bind(view)

        lifecycleScope.launch {
            viewModel.getAllNewsCategories().collect {
                val newsCategoryItems = it

                val adapter = ArrayAdapter(requireContext(), R.layout.menu_item, newsCategoryItems)
                binding.newsItemCategoryTextAutoCompleteTextView.setAdapter(adapter)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            Events.events.collect {
                viewModel.loadNewsCategoriesExceptionEvent
                val activity = activity ?: return@collect
                val dialog = android.app.AlertDialog.Builder(activity)
                dialog.setMessage(R.string.error)
                    .setPositiveButton(R.string.fragment_positive_button) { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
        }

        val calendar = Calendar.getInstance()

        vPublishDateStartPicker = binding.newsItemPublishDateStartTextInputEditText

        val publishDateStartPicker =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateLabel(calendar, vPublishDateStartPicker)
            }

        vPublishDateStartPicker.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                publishDateStartPicker,
                calendar.get(Calendar.YEAR),
                calendar.get(
                    Calendar.MONTH
                ),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        vPublishDateEndPicker = binding.newsItemPublishDateEndTextInputEditText

        val publishDateEndPicker =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateLabel(calendar, vPublishDateEndPicker)
            }

        vPublishDateEndPicker.setOnClickListener {
            DatePickerDialog(
                this.requireContext(),
                publishDateEndPicker,
                calendar.get(Calendar.YEAR),
                calendar.get(
                    Calendar.MONTH
                ),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        with(binding) {
            filterButton.setOnClickListener {
                val activity = activity ?: return@setOnClickListener
                val dialog = android.app.AlertDialog.Builder(activity)
                if (newsItemCategoryTextAutoCompleteTextView.text.isNullOrBlank()) {
                    dialog.setMessage(R.string.empty_news_category)
                        .setPositiveButton(R.string.fragment_positive_button) { dialog, _ ->
                            dialog.cancel()
                        }
                        .create()
                        .show()
                }
            }
        }

        binding.newsItemCategoryTextAutoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
                val category = if (position != 0) parent.getItemAtPosition(position).toString() else null

                var dates: List<Long>? = null
                binding.filterButton.setOnClickListener {
                    if (vPublishDateStartPicker.text.toString().isNotBlank() &&
                        vPublishDateEndPicker.text.toString().isNotBlank()
                    ) {
                        dates = listOf(
                            saveDateTime(vPublishDateStartPicker.text.toString(), "00-00"),
                            saveDateTime(vPublishDateEndPicker.text.toString(), "23-59")
                        )
                        navigateUp(category, dates)

                    } else if (vPublishDateStartPicker.text.toString().isNotBlank() &&
                        vPublishDateEndPicker.text.isNullOrBlank() ||
                        vPublishDateStartPicker.text.isNullOrBlank() &&
                        vPublishDateEndPicker.text.toString().isNotBlank()
                    ) {
                        val activity = activity ?: return@setOnClickListener
                        val dialog = android.app.AlertDialog.Builder(activity)
                            dialog.setMessage(R.string.wrong_news_date_period)
                                .setPositiveButton(R.string.fragment_positive_button) { dialog, _ ->
                                    dialog.cancel()
                                }
                                .create()
                                .show()

                    } else navigateUp(category, dates)
                }
            }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateUp(category: String?, dates: List<Long>?) {
        val newsFilterArgs = NewsFilterArgs(
            category,
            dates
        )
        setFragmentResult("requestKey", bundleOf("filterArgs" to newsFilterArgs))
        findNavController().navigateUp()
    }
}
