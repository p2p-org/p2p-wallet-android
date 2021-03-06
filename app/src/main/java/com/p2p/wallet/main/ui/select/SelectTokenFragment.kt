package com.p2p.wallet.main.ui.select

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.p2p.wallet.R
import com.p2p.wallet.common.mvp.BaseFragment
import com.p2p.wallet.databinding.FragmentSelectTokenBinding
import com.p2p.wallet.token.model.Token
import com.p2p.wallet.utils.args
import com.p2p.wallet.utils.attachAdapter
import com.p2p.wallet.utils.popBackStack
import com.p2p.wallet.utils.viewbinding.viewBinding
import com.p2p.wallet.utils.withArgs

class SelectTokenFragment(
    private val onSelected: ((Token) -> Unit)?
) : BaseFragment(R.layout.fragment_select_token) {

    companion object {
        const val REQUEST_KEY = "SELECT_TOKEN_KEY"
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
        private const val EXTRA_ALL_TOKENS = "EXTRA_ALL_TOKENS"
        fun create(tokens: List<Token>) = SelectTokenFragment(null).withArgs(
            EXTRA_ALL_TOKENS to tokens
        )

        /**
         * Callback for individual callback catch
         * */
        fun create(tokens: List<Token>, onSelected: ((Token) -> Unit)?) = SelectTokenFragment(onSelected).withArgs(
            EXTRA_ALL_TOKENS to tokens
        )
    }

    private val tokens: List<Token> by args(EXTRA_ALL_TOKENS)

    private val binding: FragmentSelectTokenBinding by viewBinding()

    private val tokenAdapter: SelectTokenAdapter by lazy {
        SelectTokenAdapter {
            onSelected?.invoke(it)
            setFragmentResult(REQUEST_KEY, bundleOf(EXTRA_TOKEN to it))
            parentFragmentManager.popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setNavigationOnClickListener { popBackStack() }
            tokenRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            tokenRecyclerView.attachAdapter(tokenAdapter)
            tokenAdapter.setItems(tokens)
        }
    }
}