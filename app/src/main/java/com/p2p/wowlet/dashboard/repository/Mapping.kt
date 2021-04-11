package com.p2p.wowlet.domain.extentions

import android.graphics.Bitmap
import com.github.mikephil.charting.data.Entry
import com.p2p.wowlet.dashboard.model.local.ActivityItem
import com.p2p.wowlet.dashboard.model.local.AddCoinItem
import com.p2p.wowlet.dashboard.model.local.BalanceInfo
import com.p2p.wowlet.dashboard.model.local.ConstWalletItem
import com.p2p.wowlet.dashboard.model.local.EnterWallet
import com.p2p.wowlet.dashboard.model.local.WalletItem
import com.p2p.wowlet.common.network.HistoricalPrices
import org.p2p.solanaj.rpc.types.TransferInfo
import kotlin.math.pow

fun ConstWalletItem.constWalletToWallet(walletsList: List<BalanceInfo>): WalletItem {
    var walletItem = WalletItem()
    walletsList.forEach {
        if (it.mint == mintAddress) {
            if (tokenSymbol == "USDT" || tokenSymbol == "USDC") {
                walletItem = WalletItem(
                    tokenSymbol = tokenSymbol,
                    mintAddress = mintAddress,
                    tokenName = tokenName,
                    depositAddress = it.depositAddress,
                    decimals = it.decimals,
                    icon = icon,
                    price = it.amount.toDouble(),
                    amount = it.amount.toDouble()
                )
            } else {
                walletItem = WalletItem(
                    tokenSymbol = tokenSymbol,
                    mintAddress = mintAddress,
                    tokenName = tokenName,
                    depositAddress = it.depositAddress,
                    decimals = it.decimals,
                    icon = icon,
                    amount = it.amount.toDouble()
                )
            }
        } else {
            walletItem = WalletItem(
                tokenSymbol = tokenSymbol,
                mintAddress = mintAddress,
                tokenName = tokenName,
                depositAddress = it.depositAddress,
                decimals = it.decimals,
                icon = icon,
                amount = it.amount.toDouble() * it.decimals
            )
        }
    }
    return walletItem
}

fun BalanceInfo.walletToWallet(walletsList: List<ConstWalletItem>): WalletItem {
    var walletItem = WalletItem()
    walletsList.forEach {
        if (it.mintAddress == mint) {
            if (it.tokenSymbol == "USDT" || it.tokenSymbol == "USDC") {
                walletItem = WalletItem(
                    tokenSymbol = it.tokenSymbol,
                    mintAddress = it.mintAddress,
                    tokenName = it.tokenName,
                    depositAddress = depositAddress,
                    decimals = decimals,
                    icon = it.icon,
                    price = amount.toDouble() / (10.0.pow(decimals)),
                    amount = amount.toDouble() / (10.0.pow(decimals)),
                    walletBinds = 1.0
                )
            } else {
                walletItem = WalletItem(
                    tokenSymbol = it.tokenSymbol,
                    mintAddress = it.mintAddress,
                    tokenName = it.tokenName,
                    depositAddress = depositAddress,
                    decimals = decimals,
                    icon = it.icon,
                    amount = amount.toDouble() / (10.0.pow(decimals))
                )
            }
        }
    }
    return walletItem
}

fun ConstWalletItem.fromConstWalletToAddCoinItem(
    change24h: Double,
    change24hInPercentages: Double,
    currency: Double
): AddCoinItem {
    return AddCoinItem(
        tokenName = tokenSymbol,
        mintAddress = mintAddress,
        tokenSymbol = tokenSymbol,
        icon = icon,
        change24hPrice = change24h,
        change24hPercentages = change24hInPercentages,
        currency = currency
    )
}

fun WalletItem.walletItemToQrCode(qrCode: Bitmap): EnterWallet {
    return EnterWallet(
        qrCode = qrCode,
        walletAddress = depositAddress,
        icon = icon,
        name = tokenName
    )
}

fun HistoricalPrices.fromHistoricalPricesToChartItem(index: Int): Entry {
    return Entry(index.toFloat(), close.toFloat())
}

fun TransferInfo.transferInfoToActivityItem(
    publicKey: String,
    icon: String,
    tokenName: String,
    tokenSymbol: String,
    isCreating: Boolean = false
): ActivityItem {
    val symbolPrice: String
    val sendOrReceive = when {
        isCreating -> {
            symbolPrice = "+"
            "Create account"
        }
        from == publicKey -> {
            symbolPrice = "-"
            "Send Coin"
        }
        else -> {
            symbolPrice = "+"
            "Receive Coin"
        }
    }
    return ActivityItem(
        icon = icon,
        name = sendOrReceive,
        symbolsPrice = symbolPrice,
        price = lamports.toDouble() / (10.0.pow(9)),
        lamports = lamports.toDouble() / (10.0.pow(9)),
        slot = slot,
        signature = signature,
        fee = fee,
        from = from,
        to = to,
        tokenName = tokenName,
        tokenSymbol = tokenSymbol,
        date = ""
    )
}