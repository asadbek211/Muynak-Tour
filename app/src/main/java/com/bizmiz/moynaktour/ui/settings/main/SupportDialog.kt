package com.bizmiz.moynaktour.ui.settings.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bizmiz.moynaktour.R
import com.bizmiz.moynaktour.core.utils.onClick
import com.bizmiz.moynaktour.databinding.DialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SupportDialog(private val mFragment: SettingsMainFragment) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBinding
    private var savedViewInstance: View? = null

    init {
        show(mFragment.requireActivity().supportFragmentManager, "tag")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return if (savedInstanceState != null) {
            savedViewInstance
        } else {
            savedViewInstance =
                inflater.inflate(R.layout.dialog, container, true)
            savedViewInstance
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogBinding.bind(view)

        binding.apply {
            dev.onClick {
                selectDev()
                dismiss()
            }

            organiser.onClick {
                selectOrganizer()
                dismiss()
            }
        }
    }

    private var selectDev: () -> Unit = {}
    private var selectOrganizer: () -> Unit = {}

    fun onDevSelected(selectDev: () -> Unit) {
        this.selectDev = selectDev
    }

    fun onOrganizerSelected(selectOrganizer: () -> Unit) {
        this.selectOrganizer = selectOrganizer
    }
}

