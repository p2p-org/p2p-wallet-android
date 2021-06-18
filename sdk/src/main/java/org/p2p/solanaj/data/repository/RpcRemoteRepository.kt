package org.p2p.solanaj.data.repository

import android.util.Base64
import org.p2p.solanaj.data.RpcRepository
import org.p2p.solanaj.data.api.RpcApi
import org.p2p.solanaj.kits.MultipleAccountsInfo
import org.p2p.solanaj.kits.Pool
import org.p2p.solanaj.model.core.Account
import org.p2p.solanaj.model.core.PublicKey
import org.p2p.solanaj.model.core.TransactionRequest
import org.p2p.solanaj.model.types.AccountInfo
import org.p2p.solanaj.model.types.ConfigObjects
import org.p2p.solanaj.model.types.ConfirmedTransaction
import org.p2p.solanaj.model.types.RecentBlockhash
import org.p2p.solanaj.model.types.RpcRequest
import org.p2p.solanaj.model.types.RpcSendTransactionConfig
import org.p2p.solanaj.model.types.SignatureInformation
import org.p2p.solanaj.model.types.TokenAccountBalance
import org.p2p.solanaj.model.types.TokenAccounts
import org.p2p.solanaj.programs.TokenProgram
import java.util.HashMap

class RpcRemoteRepository(
    private val rpcApi: RpcApi
) : RpcRepository {

    override suspend fun getTokenAccountBalance(account: PublicKey): TokenAccountBalance {
        val params = listOf(account.toString())
        val rpcRequest = RpcRequest("getTokenAccountBalance", params)
        return rpcApi.getTokenAccountBalance(rpcRequest)
    }

    override suspend fun getRecentBlockhash(): RecentBlockhash {
        val rpcRequest = RpcRequest("getRecentBlockhash", null)
        return rpcApi.getRecentBlockhash(rpcRequest)
    }

    override suspend fun sendTransaction(
        recentBlockhash: RecentBlockhash,
        transaction: TransactionRequest,
        signers: List<Account>
    ): String {

        transaction.setRecentBlockHash(recentBlockhash.recentBlockhash)
        transaction.sign(signers)
        val serializedTransaction = transaction.serialize()

        val base64Trx = Base64
            .encodeToString(serializedTransaction, Base64.DEFAULT)
            .replace("\n", "")

        val params = mutableListOf<Any>()

        params.add(base64Trx)
        params.add(RpcSendTransactionConfig())

        val rpcRequest = RpcRequest("sendTransaction", params)
        return rpcApi.sendTransaction(rpcRequest)
    }

    override suspend fun getAccountInfo(account: PublicKey): AccountInfo {
        val params = listOf(
            account.toString(),
            RpcSendTransactionConfig()
        )
        val rpcRequest = RpcRequest("getAccountInfo", params)
        return rpcApi.getAccountInfo(rpcRequest)
    }

    override suspend fun getPools(account: PublicKey): List<Pool.PoolInfo> {
        val params = listOf(
            account.toString(),
            ConfigObjects.ProgramAccountConfig(RpcSendTransactionConfig.Encoding.base64)
        )
        val rpcRequest = RpcRequest("getProgramAccounts", params)
        val response = rpcApi.getProgramAccounts(rpcRequest)
        return response.map { Pool.PoolInfo.fromProgramAccount(it) }
    }

    override suspend fun getBalance(account: PublicKey): Long {
        val params = listOf(account.toString())
        val rpcRequest = RpcRequest("getBalance", params)
        return rpcApi.getBalance(rpcRequest).value
    }

    override suspend fun getTokenAccountsByOwner(owner: PublicKey): TokenAccounts {
        val programId = TokenProgram.PROGRAM_ID
        val programIdParam = HashMap<String, String>()
        programIdParam["programId"] = programId.toBase58()

        val encoding = HashMap<String, String>()
        encoding["encoding"] = "jsonParsed"

        val params = listOf(
            owner.toBase58(),
            programIdParam,
            encoding
        )

        val rpcRequest = RpcRequest("getTokenAccountsByOwner", params)
        return rpcApi.getTokenAccountsByOwner(rpcRequest)
    }

    override suspend fun getConfirmedSignaturesForAddress2(account: PublicKey, limit: Int): List<SignatureInformation> {
        val params = listOf(
            account.toString(),
            ConfigObjects.ConfirmedSignFAddr2(limit)
        )

        val rpcRequest = RpcRequest("getConfirmedSignaturesForAddress2", params)
        return rpcApi.getConfirmedSignaturesForAddress2(rpcRequest)
    }

    override suspend fun getConfirmedTransaction(signature: String): ConfirmedTransaction {
        val params = listOf(signature)
        val rpcRequest = RpcRequest("getConfirmedTransaction", params)
        return rpcApi.getConfirmedTransaction(rpcRequest)
    }

    override suspend fun getBlockTime(block: Long): Long {
        val params = listOf(block)
        val rpcRequest = RpcRequest("getBlockTime", params)
        return rpcApi.getBlockTime(rpcRequest)
    }

    override suspend fun getMinimumBalanceForRentExemption(dataLength: Long): Long {
        val params = listOf(dataLength)
        val rpcRequest = RpcRequest("getMinimumBalanceForRentExemption", params)
        return rpcApi.getMinimumBalanceForRentExemption(rpcRequest)
    }

    override suspend fun getMultipleAccounts(publicKeys: List<PublicKey>): MultipleAccountsInfo {
        val keys = publicKeys.map { it.toBase58() }

        val encoding = HashMap<String, String>()
        encoding["encoding"] = "jsonParsed"

        val params = listOf(
            keys,
            encoding
        )

        val rpcRequest = RpcRequest("getMultipleAccounts", params)

        return rpcApi.getMultipleAccounts(rpcRequest)
    }
}