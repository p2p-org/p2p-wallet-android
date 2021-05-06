package com.p2p.wallet.main.model

import com.p2p.wallet.dashboard.model.local.Token

sealed class TokenItem {
    data class Shown(val token: Token) : TokenItem()
    data class Group(val hiddenTokens: List<Token>) : TokenItem()
}