package com.p2p.wallet.rpc.api

import com.p2p.wallet.infrastructure.network.CommonResponse
import org.p2p.solanaj.kits.MultipleAccountsInfo
import org.p2p.solanaj.kits.transaction.ConfirmedTransactionParsed
import org.p2p.solanaj.model.types.AccountInfo
import org.p2p.solanaj.model.types.ProgramAccount
import org.p2p.solanaj.model.types.RecentBlockhash
import org.p2p.solanaj.model.types.RpcRequest
import org.p2p.solanaj.model.types.RpcResultTypes
import org.p2p.solanaj.model.types.SignatureInformation
import org.p2p.solanaj.model.types.TokenAccountBalance
import org.p2p.solanaj.model.types.TokenAccounts
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface RpcApi {

    @POST
    suspend fun getTokenAccountBalance(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<TokenAccountBalance>

    @POST
    suspend fun getRecentBlockhash(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<RecentBlockhash>

    @POST
    suspend fun sendTransaction(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<String>

    @POST
    suspend fun getAccountInfo(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<AccountInfo>

    @POST
    suspend fun getProgramAccounts(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<List<ProgramAccount>>

    @POST
    suspend fun getBalance(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<RpcResultTypes.ValueLong>

    @POST
    suspend fun getTokenAccountsByOwner(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<TokenAccounts>

    @POST
    suspend fun getConfirmedSignaturesForAddress2(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<List<SignatureInformation>>

    @POST
    suspend fun getConfirmedTransactions(
        @Body rpcRequest: List<RpcRequest>,
        @Url url: String = ""
    ): List<CommonResponse<ConfirmedTransactionParsed>>

    @POST
    suspend fun getMinimumBalanceForRentExemption(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<Long>

    @POST
    suspend fun getMultipleAccounts(
        @Body rpcRequest: RpcRequest,
        @Url url: String = ""
    ): CommonResponse<MultipleAccountsInfo>
}