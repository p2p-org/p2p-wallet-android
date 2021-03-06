package org.p2p.solanaj.kits

import android.util.Base64
import org.p2p.solanaj.model.core.PublicKey
import org.p2p.solanaj.model.core.PublicKey.Companion.findProgramAddress
import org.p2p.solanaj.model.types.AccountInfo
import org.p2p.solanaj.programs.TokenProgram
import org.p2p.solanaj.programs.TokenProgram.AccountInfoData
import org.p2p.solanaj.rpc.RpcException
import java.util.ArrayList

object TokenTransaction {

    @Throws(RpcException::class)
    fun getAccountInfoData(
        accountInfo: AccountInfo?,
        programId: PublicKey
    ): AccountInfoData {
        if (accountInfo?.value == null) {
            throw RpcException("Failed to find account")
        }
        require(accountInfo.value.owner == programId.toString()) { "Invalid account owner" }
        val base64Data = accountInfo.value.data!![0]
        val data = Base64.decode(base64Data, Base64.DEFAULT)
        return AccountInfoData.decode(data)
    }

    fun parseAccountInfoData(
        accountInfo: AccountInfo?,
        programId: PublicKey
    ): AccountInfoData? {
        if (accountInfo?.value == null) return null

        val isValid = accountInfo.value.owner == programId.toString()
        if (!isValid) return null

        val base64Data = accountInfo.value.data!![0]
        val data = Base64.decode(base64Data, Base64.DEFAULT)
        return AccountInfoData.decode(data)
    }

    fun getMultipleAccounts(
        accounts: MultipleAccountsInfo,
        keys: List<String>
    ): MultipleAccountsInfo {
        val parsedAccounts: MutableList<AccountInfoParsed> = ArrayList()
        for (i in accounts.accountsInfoParsed.indices) {
            val current = accounts.accountsInfoParsed[i]
            current.address = keys[i]
            parsedAccounts.add(current)
        }
        accounts.accountsInfoParsed = parsedAccounts
        return accounts
    }

    @Throws(Exception::class)
    fun getAssociatedTokenAddress(
        mint: PublicKey,
        owner: PublicKey
    ): PublicKey {
        return getAssociatedTokenAddress(
            TokenProgram.ASSOCIATED_TOKEN_PROGRAM_ID, TokenProgram.PROGRAM_ID, mint,
            owner
        )
    }

    @Throws(Exception::class)
    fun getAssociatedTokenAddress(
        associatedProgramId: PublicKey?,
        programId: PublicKey,
        mint: PublicKey,
        owner: PublicKey
    ): PublicKey {
        return findProgramAddress(
            listOf(owner.toByteArray(), programId.toByteArray(), mint.toByteArray()),
            associatedProgramId!!
        )
            .address
    }
}