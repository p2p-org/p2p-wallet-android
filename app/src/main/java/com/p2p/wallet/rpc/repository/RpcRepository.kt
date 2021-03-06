package com.p2p.wallet.rpc.repository

import org.p2p.solanaj.kits.MultipleAccountsInfo
import org.p2p.solanaj.kits.Pool
import org.p2p.solanaj.kits.transaction.ConfirmedTransactionParsed
import org.p2p.solanaj.model.core.PublicKey
import org.p2p.solanaj.model.core.TransactionRequest
import org.p2p.solanaj.model.types.AccountInfo
import org.p2p.solanaj.model.types.RecentBlockhash
import org.p2p.solanaj.model.types.SignatureInformation
import org.p2p.solanaj.model.types.TokenAccountBalance
import org.p2p.solanaj.model.types.TokenAccounts

interface RpcRepository {
    suspend fun getTokenAccountBalance(account: PublicKey): TokenAccountBalance
    suspend fun getRecentBlockhash(): RecentBlockhash
    suspend fun sendTransaction(transaction: TransactionRequest): String

    suspend fun getAccountInfo(account: PublicKey): AccountInfo?
    suspend fun getPools(account: PublicKey): List<Pool.PoolInfo>
    suspend fun getBalance(account: PublicKey): Long
    suspend fun getTokenAccountsByOwner(owner: PublicKey): TokenAccounts
    suspend fun getMinimumBalanceForRentExemption(dataLength: Long): Long
    suspend fun getMultipleAccounts(publicKeys: List<PublicKey>): MultipleAccountsInfo

    /**
     * The history is being fetched from main-net despite the selected network
     * */
    suspend fun getConfirmedTransactions(signatures: List<String>): List<ConfirmedTransactionParsed>
    suspend fun getConfirmedSignaturesForAddress(
        account: PublicKey,
        before: String?,
        limit: Int
    ): List<SignatureInformation>
}