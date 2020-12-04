package com.p2p.wowlet.fragment.dashboard.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.p2p.wowlet.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.p2p.wowlet.databinding.DialogTansactionBinding
import com.p2p.wowlet.fragment.detailwallet.viewmodel.DetailWalletViewModel
import com.p2p.wowlet.utils.copyClipboard
import com.wowlet.entities.local.ActivityItem
import com.wowlet.entities.local.TransactionInfoModel
import kotlinx.android.synthetic.main.dialog_tansaction.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionBottomSheet(private val dataInfo: ActivityItem) : BottomSheetDialogFragment() {

    lateinit var binding: DialogTansactionBinding

    companion object {
        const val TRANSACTION_DIALOG="transactionDialog"
        fun newInstance(dataInfo: ActivityItem): TransactionBottomSheet =
            TransactionBottomSheet(dataInfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_tansaction, container, false
        )
        binding.model = dataInfo
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vClose.setOnClickListener {
            dismiss()
        }
        copyToUserKey.setOnClickListener {
            context?.copyClipboard(dataInfo.to)
        }
        copyFromUserKey.setOnClickListener {
            context?.copyClipboard(dataInfo.from)
        }
        copyTransaction.setOnClickListener {
            context?.copyClipboard(dataInfo.signature)
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.run {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }
}