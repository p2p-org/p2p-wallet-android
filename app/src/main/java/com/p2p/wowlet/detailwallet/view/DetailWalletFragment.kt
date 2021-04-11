package com.p2p.wowlet.detailwallet.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.p2p.wowlet.R
import com.p2p.wowlet.common.mvp.BaseFragment
import com.p2p.wowlet.dashboard.ui.dialog.TransactionBottomSheet
import com.p2p.wowlet.dashboard.ui.dialog.swap.SwapBottomSheet
import com.p2p.wowlet.databinding.FragmentDetailActivityBinding
import com.p2p.wowlet.deprecated.viewcommand.Command
import com.p2p.wowlet.deprecated.viewcommand.ViewCommand
import com.p2p.wowlet.detailwallet.adapter.ActivityAdapter
import com.p2p.wowlet.detailwallet.dialog.YourWalletBottomSheet
import com.p2p.wowlet.detailwallet.viewmodel.DetailWalletViewModel
import com.p2p.wowlet.dashboard.ui.dialog.sendcoins.view.SendCoinsBottomSheet
import com.p2p.wowlet.dashboard.ui.dialog.sendcoins.view.SendCoinsBottomSheet.Companion.TAG_SEND_COIN
import com.p2p.wowlet.dashboard.model.local.WalletItem
import com.p2p.wowlet.utils.changeTextColor
import com.p2p.wowlet.utils.copyClipboard
import com.p2p.wowlet.utils.getMonthly
import com.p2p.wowlet.utils.getOneHour
import com.p2p.wowlet.utils.getWeekly
import com.p2p.wowlet.utils.getYesterday
import com.p2p.wowlet.utils.initChart
import com.p2p.wowlet.utils.replaceFragment
import com.p2p.wowlet.utils.roundCurrencyValue
import com.p2p.wowlet.utils.viewbinding.viewBinding
import com.p2p.wowlet.blockchain.BlockChainExplorerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class DetailWalletFragment : BaseFragment(R.layout.fragment_detail_activity) {

    private lateinit var activityAdapter: ActivityAdapter
    private val viewModel: DetailWalletViewModel by viewModel()
    private val binding: FragmentDetailActivityBinding by viewBinding()
    private var walletItem: WalletItem? = null
    private var selectedTextView: AppCompatTextView? = null
    private val cal = Calendar.getInstance()

    companion object {
        const val WALLET_ITEM = "walletItem"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        arguments?.let {
            walletItem = it.getParcelable(WALLET_ITEM)
            println("debug: icon = ${walletItem?.icon}")
        }
        walletItem?.run {
            viewModel.getActivityList(this.depositAddress, icon, tokenName, this.tokenSymbol)
            viewModel.getChartData(this.tokenSymbol)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.run {
            Glide.with(this@DetailWalletFragment)
                .load(walletItem?.icon)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(38)))
                .into(currencyIcon)
            vTitle.text = walletItem?.tokenName
            vWalletAddress.text = walletItem?.depositAddress
            vPrice.text = "$${walletItem?.price?.roundCurrencyValue()}"
            vTokenValue.text = "${walletItem?.amount} ${walletItem?.tokenSymbol}"
            vWalletAddress.setOnClickListener {
                context?.run { copyClipboard(vWalletAddress.text.toString()) }
            }
            getChartByDay.setOnClickListener {
                getChartByDay.changeTextColor(selectedTextView)
                val yesterday = getYesterday()
                walletItem?.tokenSymbol?.run {
                    this@DetailWalletFragment.viewModel.getChartDataByDate(
                        this,
                        yesterday,
                        cal.timeInMillis
                    )
                }
                selectedTextView = getChartByDay
            }
            getChartByWeek.setOnClickListener {
                getChartByWeek.changeTextColor(selectedTextView)
                val weekly = getWeekly()
                walletItem?.tokenSymbol?.run {
                    this@DetailWalletFragment.viewModel.getChartDataByDate(
                        this,
                        weekly,
                        cal.timeInMillis
                    )
                }
                selectedTextView = getChartByWeek
            }
            getChartByMonth.setOnClickListener {
                getChartByMonth.changeTextColor(selectedTextView)
                val monthly = getMonthly()
                walletItem?.tokenSymbol?.run {
                    this@DetailWalletFragment.viewModel.getChartDataByDate(
                        this,
                        monthly,
                        cal.timeInMillis
                    )
                }
                selectedTextView = getChartByMonth
            }
            getChartByYear.setOnClickListener {
                getChartByYear.changeTextColor(selectedTextView)
                val year = getOneHour()
                walletItem?.tokenSymbol?.run {
                    this@DetailWalletFragment.viewModel.getChartDataByDate(
                        this,
                        year,
                        cal.timeInMillis
                    )
                }
                selectedTextView = getChartByYear
            }
            getChartByAll.setOnClickListener {
                getChartByAll.changeTextColor(selectedTextView)
                walletItem?.tokenSymbol?.run {
                    this@DetailWalletFragment.viewModel.getChartData(this)
                }
                selectedTextView = getChartByAll
            }
//            sendCoinAddress.setOnClickListener {
//                walletItem?.let { this@DetailWalletFragment.viewModel.goToSendCoin(it) }
//            }
            vQrScanner.setOnClickListener {
                walletItem?.let { item ->
                    this@DetailWalletFragment.viewModel.goToQrScanner(item)
                }
            }
        }
        activityAdapter = ActivityAdapter(mutableListOf(), viewModel) {
            TransactionBottomSheet
                .newInstance(it) {
                    replaceFragment(BlockChainExplorerFragment.createScreen(it))
                }
                .show(childFragmentManager, TransactionBottomSheet::javaClass.name)
        }
        binding.vRvActivity.adapter = activityAdapter
        binding.vRvActivity.layoutManager = LinearLayoutManager(context)

        observeData()
    }

    private fun observeData() {
        viewModel.getChartData.observe(viewLifecycleOwner) {
            binding.vChartData.initChart(it)
        }
        viewModel.getActivityData.observe(viewLifecycleOwner) {
            activityAdapter.updateList(it)
        }

        viewModel.getActivityDataError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.command.observe(viewLifecycleOwner) {
            processViewCommand(it)
        }
    }

    private fun processViewCommand(command: ViewCommand) {
        when (command) {
            is Command.OpenSendCoinDialogViewCommand -> {
                SendCoinsBottomSheet.newInstance(
                    command.walletItem,
                    command.walletAddress,
                ).show(
                    childFragmentManager,
                    TAG_SEND_COIN
                )
            }
            is Command.OpenSwapBottomSheetViewCommand -> {
                SwapBottomSheet.newInstance(
                    allMyWallets = command.allMyWallets,
                    selectedWalletItems = command.walletData
                ).show(
                    childFragmentManager,
                    SwapBottomSheet.TAG_SWAP_BOTTOM_SHEET
                )
            }
            is Command.OpenTransactionDialogViewCommand -> {
                TransactionBottomSheet.newInstance(command.itemActivity) {
                    replaceFragment(BlockChainExplorerFragment.createScreen(it))
                }.show(childFragmentManager, TransactionBottomSheet.TRANSACTION_DIALOG)
            }
            is Command.YourWalletDialogViewCommand -> {
                YourWalletBottomSheet.newInstance(command.enterWallet).show(
                    childFragmentManager,
                    YourWalletBottomSheet.ENTER_YOUR_WALLET
                )
            }
        }
    }
}