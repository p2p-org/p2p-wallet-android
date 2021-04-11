package com.p2p.wowlet.entities.local

data class SendTransactionModel(
    val toPublickKey: String,
    val lamports: Long = 0,
    var fromPublicKey: String = "",
    var secretKey: String = "",
    var tokenSymbol: String

)