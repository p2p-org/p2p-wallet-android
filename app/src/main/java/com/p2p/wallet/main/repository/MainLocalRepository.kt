package com.p2p.wallet.main.repository

import com.p2p.wallet.token.model.Token
import kotlinx.coroutines.flow.Flow

interface MainLocalRepository {
    suspend fun setTokens(tokens: List<Token>)
    suspend fun updateTokens(tokens: List<Token>)
    fun getTokensFlow(): Flow<List<Token>>
    suspend fun getTokens(): List<Token>
    suspend fun setTokenHidden(mintAddress: String, visibility: String)
    suspend fun clear()
}