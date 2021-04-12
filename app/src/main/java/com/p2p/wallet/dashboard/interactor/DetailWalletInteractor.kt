package com.p2p.wallet.dashboard.interactor

import com.github.mikephil.charting.data.Entry
import com.p2p.wallet.dashboard.repository.DashboardRepository
import com.p2p.wallet.dashboard.repository.DetailActivityRepository
import com.p2p.wallet.dashboard.repository.WowletApiCallRepository
import com.p2p.wallet.domain.extentions.fromHistoricalPricesToChartItem
import com.p2p.wallet.domain.extentions.transferInfoToActivityItem
import com.p2p.wallet.domain.extentions.walletItemToQrCode
import com.p2p.wallet.utils.getActivityDate
import com.p2p.wallet.utils.secondToDate
import com.p2p.wallet.common.network.CallException
import com.p2p.wallet.common.network.Constants
import com.p2p.wallet.common.network.Result
import com.p2p.wallet.dashboard.model.local.ActivityItem
import com.p2p.wallet.dashboard.model.local.EnterWallet
import com.p2p.wallet.dashboard.model.local.WalletItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class DetailWalletInteractor(
    private val wowletApiCallRepository: WowletApiCallRepository,
    private val detailActivityRepository: DetailActivityRepository,
    private val dashboardRepository: DashboardRepository,
) {

    suspend fun getActivityList(
        publicKey: String,
        icon: String,
        tokenName: String,
        tokenSymbol: String
    ): Result<List<ActivityItem>> {
        return try {
            val walletsList = wowletApiCallRepository.getDetailActivityData(publicKey).map {
                it.transferInfoToActivityItem(publicKey, icon, tokenName, tokenSymbol)
            }
            walletsList.map {
                val time = wowletApiCallRepository.getBlockTime(it.slot)
                val secondToDate = time.secondToDate()
                it.date = secondToDate?.getActivityDate() ?: ""
            }

            coroutineScope {
                walletsList.map { activityItem ->
                    val time = wowletApiCallRepository.getBlockTime(activityItem.slot)
                    async {
                        when (
                            val historicalPrices = detailActivityRepository.getHistoricalPricesByDate(
                                activityItem.tokenSymbol + "USDT",
                                time * 1000,
                                time * 1000,

                            )
                        ) {
                            is Result.Success -> {
                                historicalPrices.data?.let {
                                    if (it.isNotEmpty()) {
                                        val close = it[0].close
                                        activityItem.currency = close
                                    }
                                }
                            }
                            is Result.Error -> {
                                Result.Error(
                                    CallException(
                                        Constants.ERROR_NULL_DATA,
                                        null,
                                        "Can't load OrderBook data at server"
                                    )
                                )
                            }
                        }
                    }
                }.awaitAll()
            }

            Result.Success(walletsList)
        } catch (e: java.lang.Exception) {
            Result.Error(CallException(Constants.ERROR_TIME_OUT, e.message))
        }
    }

    suspend fun blockTime(slot: Long): Result<String> {
        return try {
            val time = wowletApiCallRepository.getBlockTime(slot)
            val stringDate = time.secondToDate()
            Result.Success(stringDate)
        } catch (e: Exception) {
            Result.Error(CallException(Constants.REQUEST_EXACTION, e.message))
        }
    }

    suspend fun getChatListByDate(
        tokenSymbol: String,
        startTime: Long,
        endTime: Long
    ): Result<List<Entry>> {
        return when (
            val data = detailActivityRepository.getHistoricalPricesByDate(
                tokenSymbol + "USDT",
                startTime,
                endTime
            )
        ) {
            is Result.Success -> {
                val chartData = data.data?.mapIndexed { index, historicalPrices ->
                    historicalPrices.fromHistoricalPricesToChartItem(index)
                }
                Result.Success(chartData)
            }
            is Result.Error -> {
                Result.Error(CallException(Constants.REQUEST_EXACTION, "Error char data load"))
            }
        }
    }

    suspend fun getChatList(tokenSymbol: String): Result<List<Entry>> {
        return when (
            val data =
                detailActivityRepository.getAllHistoricalPrices(tokenSymbol + "USDT")
        ) {
            is Result.Success -> {
                val chartData = data.data?.mapIndexed { index, historicalPrices ->
                    historicalPrices.fromHistoricalPricesToChartItem(index)
                }
                Result.Success(chartData)
            }
            is Result.Error -> {
                Result.Error(CallException(Constants.REQUEST_EXACTION, "Error char data load"))
            }
        }
    }

    suspend fun getPercentages(walletItem: WalletItem): Double {
        var change24hInPercentages = 0.0
        coroutineScope {
            withContext(Dispatchers.IO) {
                when (val historicalPrice = dashboardRepository.getHistoricalPrices(walletItem.tokenSymbol + "USDT")) {
                    is Result.Success -> {
                        historicalPrice.data?.let {
                            if (it.isNotEmpty()) {
                                val close = it[0].close
                                val open = it[0].open
                                val change24h = close - open
                                change24hInPercentages = if (open == 0.0) {
                                    change24h / 1
                                } else {
                                    change24h / open
                                }
                            }
                        }
                    }
                    else -> {
                        Result.Error(
                            CallException(
                                Constants.ERROR_NULL_DATA,
                                null,
                                "Can't load OrderBook data at server"
                            )
                        )
                    }
                }
            }
        }
        return change24hInPercentages
    }

    fun generateQRrCode(walletItem: WalletItem): EnterWallet =
        walletItem.walletItemToQrCode(detailActivityRepository.getQrCode(walletItem.depositAddress))
}