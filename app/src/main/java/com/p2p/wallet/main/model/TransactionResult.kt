package com.p2p.wallet.main.model

import androidx.annotation.StringRes

sealed class TransactionResult {
    data class Success(val transactionId: String) : TransactionResult()

    object WrongWallet : TransactionResult()

    data class Error(@StringRes val messageRes: Int) : TransactionResult()
}