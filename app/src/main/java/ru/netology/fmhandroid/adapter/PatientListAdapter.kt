package ru.netology.fmhandroid.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.fmhandroid.databinding.PatientsListCardBinding
import ru.netology.fmhandroid.dto.Patient

interface OnInterractionListener {
    fun onOpenCard(patient: Patient) {}
}

class PatientListAdapter(
        private val onInterractionListener: OnInterractionListener,
) : ListAdapter<Patient, PatientListAdapter.PatientViewHolder>(PatientDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = PatientsListCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PatientViewHolder(binding, onInterractionListener)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class PatientViewHolder(
            private val binding: PatientsListCardBinding,
            private val onInterractionListener: OnInterractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(patient: Patient) {
            binding.apply {
                (patient.lastName + " " + patient.firstName + " " + patient.middleName).also { patientName.text = it }
                patientLocation.text = if (patient.inHospice) "Да" else "Нет"
                patientName.setOnClickListener {
                    onInterractionListener.onOpenCard(patient)
                }
            }
        }
    }

    class PatientDiffCallBack : DiffUtil.ItemCallback<Patient>() {
        override fun areItemsTheSame(oldItem: Patient, newItem: Patient): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Patient, newItem: Patient): Boolean {
            return oldItem == newItem
        }
    }
}