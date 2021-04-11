package com.p2p.wowlet.dashboard.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.p2p.wowlet.R
import com.p2p.wowlet.databinding.DialogBackupBinding
import com.p2p.wowlet.utils.viewbinding.viewBinding

class BackupDialog(private val openPinPage: (() -> Unit)) : DialogFragment() {

    companion object {

        const val TAG_BACKUP_DIALOG = "BackupDialog"
        fun newInstance(openPinPage: (() -> Unit)): BackupDialog {
            return BackupDialog(openPinPage)
        }
    }

    private val binding: DialogBackupBinding by viewBinding()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.dialog_backup, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            vClose.setOnClickListener {
                dismiss()
            }
            vDone.setOnClickListener {
                dismiss()
            }
            btBackupManual.setOnClickListener {
                openPinPage.invoke()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.run {
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }
}