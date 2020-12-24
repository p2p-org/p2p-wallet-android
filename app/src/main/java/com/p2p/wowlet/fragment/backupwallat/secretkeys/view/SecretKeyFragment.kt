package com.p2p.wowlet.fragment.backupwallat.secretkeys.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.p2p.wowlet.R
import com.p2p.wowlet.appbase.FragmentBaseMVVM
import com.p2p.wowlet.appbase.utils.dataBinding
import com.p2p.wowlet.appbase.viewcommand.Command
import com.p2p.wowlet.appbase.viewcommand.ViewCommand
import com.p2p.wowlet.databinding.FragmentSecretKeyBinding
import com.p2p.wowlet.fragment.backupwallat.secretkeys.adapter.SecretPhraseAdapter
import com.p2p.wowlet.fragment.backupwallat.secretkeys.viewmodel.SecretKeyViewModel
import com.wowlet.entities.local.Keyword
import org.koin.androidx.viewmodel.ext.android.viewModel

class SecretKeyFragment : FragmentBaseMVVM<SecretKeyViewModel, FragmentSecretKeyBinding>() {
    override val viewModel: SecretKeyViewModel by viewModel()
    override val binding: FragmentSecretKeyBinding by dataBinding(R.layout.fragment_secret_key)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            viewModel = this@SecretKeyFragment.viewModel
        }

        binding.rvSecretPhrase.adapter = SecretPhraseAdapter(requireContext(), viewModel.phrase)
        binding.phraseET.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.rvSecretPhrase.apply {
                    (adapter as SecretPhraseAdapter).addItem(Keyword(""))
                    v.visibility = View.GONE
                    visibility = View.VISIBLE
                }
            }
        }
    }

    override fun observes() {
        observe(viewModel.isCurrentCombination) {
            viewModel.goToPinCodeFragment()
        }
        observe(viewModel.invadedPhrase) { errorMessage ->
            context?.let {
                Toast.makeText(it, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun processViewCommand(command: ViewCommand) {
        when (command) {
            is Command.NavigateUpViewCommand -> navigateFragment(command.destinationId)
            is Command.NavigateCompleteBackupViewCommand -> navigateFragment(command.destinationId)
            is Command.NavigateManualSecretKeyViewCommand -> {
                navigateFragment(command.destinationId, command.bundle)
            }
        }
    }

    override fun navigateUp() {
        navigateBackStack()
    }

}