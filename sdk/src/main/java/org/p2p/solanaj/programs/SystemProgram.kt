package org.p2p.solanaj.programs

import org.bitcoinj.core.Utils
import org.p2p.solanaj.model.core.AccountMeta
import org.p2p.solanaj.model.core.PublicKey
import org.p2p.solanaj.model.core.TransactionInstruction
import java.util.ArrayList

object SystemProgram {
    val PROGRAM_ID = PublicKey("11111111111111111111111111111111")
    val SPL_TOKEN_PROGRAM_ID = PublicKey("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA")
    private val SYSVAR_RENT_ADDRESS = PublicKey("SysvarRent111111111111111111111111111111111")
    private const val PROGRAM_INDEX_CREATE_ACCOUNT = 0
    private const val PROGRAM_INDEX_TRANSFER = 2

    fun transfer(
        fromPublicKey: PublicKey,
        toPublickKey: PublicKey,
        lamports: Long
    ): TransactionInstruction {
        val keys = ArrayList<AccountMeta>()
        keys.add(AccountMeta(fromPublicKey, isSigner = true, isWritable = true))
        keys.add(AccountMeta(toPublickKey, isSigner = false, isWritable = true))

        // 4 byte instruction index + 8 bytes lamports
        val data = ByteArray(4 + 8)
        Utils.uint32ToByteArrayLE(PROGRAM_INDEX_TRANSFER.toLong(), data, 0)
        Utils.int64ToByteArrayLE(lamports, data, 4)
        return TransactionInstruction(PROGRAM_ID, keys, data)
    }

    fun createAccount(
        fromPublicKey: PublicKey,
        newAccountPublikkey: PublicKey,
        lamports: Long,
        space: Long,
        programId: PublicKey
    ): TransactionInstruction {
        val keys = ArrayList<AccountMeta>()
        keys.add(AccountMeta(fromPublicKey, isSigner = true, isWritable = true))
        keys.add(AccountMeta(newAccountPublikkey, isSigner = true, isWritable = true))
        val data = ByteArray(4 + 8 + 8 + 32)
        Utils.uint32ToByteArrayLE(PROGRAM_INDEX_CREATE_ACCOUNT.toLong(), data, 0)
        Utils.int64ToByteArrayLE(lamports, data, 4)
        Utils.int64ToByteArrayLE(space, data, 12)
        System.arraycopy(programId.toByteArray(), 0, data, 20, 32)
        return TransactionInstruction(PROGRAM_ID, keys, data)
    }

    fun initializeAccountInstruction(
        account: PublicKey,
        mint: PublicKey,
        owner: PublicKey
    ): TransactionInstruction {
        val keys = ArrayList<AccountMeta>()
        keys.add(AccountMeta(account, isSigner = false, isWritable = true))
        keys.add(AccountMeta(mint, isSigner = false, isWritable = false))
        keys.add(AccountMeta(owner, isSigner = false, isWritable = false))
        keys.add(AccountMeta(SYSVAR_RENT_ADDRESS, isSigner = false, isWritable = false))
        val data = byteArrayOf(1)
        return TransactionInstruction(SPL_TOKEN_PROGRAM_ID, keys, data)
    }
}