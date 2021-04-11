package com.p2p.wowlet.detailwallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.p2p.wowlet.deprecated.viewcommand.Command
import com.p2p.wowlet.deprecated.viewmodel.BaseViewModel
import com.p2p.wowlet.dashboard.interactor.DetailWalletInteractor
import com.p2p.wowlet.common.network.Result
import com.p2p.wowlet.dashboard.model.local.ActivityItem
import com.p2p.wowlet.dashboard.model.local.WalletItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailWalletViewModel(val detailWalletInteractor: DetailWalletInteractor) :
    BaseViewModel() {

    val chartList = mutableListOf(
        Entry(0f, 0f),
        Entry(1f, 10f),
        Entry(2f, 7f),
        Entry(3f, 13f),
        Entry(5f, 11f),
        Entry(5f, 20f),
        Entry(6f, 18f),
        Entry(7f, 21f),
        Entry(8f, 18f),
        Entry(9f, 21f),
        Entry(10f, 30f),
        Entry(11f, 17f),
        Entry(12f, 25f),
        Entry(13f, 27f)
    )

    private val _getActivityData by lazy { MutableLiveData<List<ActivityItem>>() }
    val getActivityData: LiveData<List<ActivityItem>> get() = _getActivityData
    private val _getChartData by lazy { MutableLiveData<List<Entry>>() }
    val getChartData: LiveData<List<Entry>> get() = _getChartData
    private val _getActivityDataError by lazy { MutableLiveData<String>() }
    val getActivityDataError: LiveData<String> get() = _getActivityDataError
    private val _blockTime by lazy { MutableLiveData<String>() }
    val blockTime: LiveData<String> get() = _blockTime
    private val _blockTimeError by lazy { MutableLiveData<String>() }
    val blockTimeError: LiveData<String> get() = _blockTimeError

    private val activityItemList = mutableListOf<ActivityItem>()

    fun getActivityList(publicKey: String, icon: String, tokenName: String, tokenSymbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val detailList = detailWalletInteractor.getActivityList(publicKey, icon, tokenName, tokenSymbol)
            when (detailList) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    activityItemList.clear()
                    /*    detailList.data?.forEach {
                            getBlockTime(it)
                        }*/
                    _getActivityData.value = detailList.data
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    _getActivityDataError.value = detailList.errors.errorMessage
                }
            }
        }
    }

    fun goToQrScanner(walletItem: WalletItem) {
        val enterWallet = detailWalletInteractor.generateQRrCode(walletItem)
        _command.value = Command.YourWalletDialogViewCommand(enterWallet)
    }

    fun getBlockTime(slot: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val data = detailWalletInteractor.blockTime(
                    slot
                )
            ) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _blockTime.value = data.data
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    _blockTimeError.value = data.errors.errorMessage
                }
            }
        }
    }

    fun getChartDataByDate(symbol: String, startTime: Long, endTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            when (
                val data =
                    detailWalletInteractor.getChatListByDate(symbol, startTime, endTime)
            ) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getChartData.value = data.data
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    _getActivityDataError.value = data.errors.errorMessage
                }
            }
        }
    }

    fun getChartData(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val data = detailWalletInteractor.getChatList(symbol)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _getChartData.value = data.data
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    _getActivityDataError.value = data.errors.errorMessage
                }
            }
        }
    }
}