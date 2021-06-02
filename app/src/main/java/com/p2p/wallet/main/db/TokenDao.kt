package com.p2p.wallet.main.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.p2p.solanaj.core.PublicKey

@Dao
interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(entities: List<TokenEntity>)

    @Query("SELECT * FROM token_table")
    fun getTokensFlow(): Flow<List<TokenEntity>>

    @Query("SELECT * FROM token_table")
    suspend fun getTokens(): List<TokenEntity>

    @Query(
        """
        UPDATE token_table
        SET is_hidden = :isHidden
        WHERE public_key = :publicKey
    """
    )
    suspend fun updateVisibility(publicKey: String, isHidden: Boolean)

    @Query("DELETE FROM token_table")
    suspend fun clearAll()
}