package com.p2p.wallet.main.repository

import com.p2p.wallet.dashboard.model.local.Token
import com.p2p.wallet.main.model.TokenPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainInMemoryRepository : MainLocalRepository {

    private val tokensFlow = MutableStateFlow<List<Token>>(emptyList())

    override suspend fun setTokens(tokens: List<Token>) {
        tokensFlow.value = tokens
    }

    override suspend fun getTokensFlow(): Flow<List<Token>> = tokensFlow

    override suspend fun getTokens(): List<Token> = tokensFlow.value

    private val pricesFlow = MutableStateFlow<List<TokenPrice>>(emptyList())

    override suspend fun setTokenPrices(tokens: List<TokenPrice>) {
        pricesFlow.value = tokens
    }

    override suspend fun getTokenPrices(): List<TokenPrice> = pricesFlow.value
}