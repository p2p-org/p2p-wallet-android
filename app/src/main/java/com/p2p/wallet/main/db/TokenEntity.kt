package com.p2p.wallet.main.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.p2p.wallet.main.db.TokenEntity.Companion.TABLE_NAME
import java.math.BigDecimal

@Entity(
    tableName = TABLE_NAME
)
data class TokenEntity(

    @PrimaryKey
    @ColumnInfo(name = COLUMN_PUBLIC_KEY)
    val publicKey: String,

    @ColumnInfo(name = COLUMN_SYMBOL)
    val tokenSymbol: String,

    @ColumnInfo(name = COLUMN_DECIMALS)
    val decimals: Int,

    @ColumnInfo(name = COLUMN_MINT_ADDRESS)
    val mintAddress: String,

    @ColumnInfo(name = COLUMN_TOKEN_NAME)
    val tokenName: String,

    @ColumnInfo(name = COLUMN_ICON_URL)
    val iconUrl: String?,

    @ColumnInfo(name = COLUMN_PRICE)
    val price: BigDecimal,

    @ColumnInfo(name = COLUMN_TOTAL)
    val total: BigDecimal,

    @ColumnInfo(name = COLUMN_COLOR)
    val color: Int,

    @ColumnInfo(name = COLUMN_EXCHANGE_RATE)
    val exchangeRate: String,

    @ColumnInfo(name = COLUMN_VISIBILITY)
    val visibility: String
) {
    companion object {
        const val TABLE_NAME = "token_table"
        const val COLUMN_PUBLIC_KEY = "public_key"
        const val COLUMN_SYMBOL = "symbol"
        const val COLUMN_DECIMALS = "decimals"
        const val COLUMN_MINT_ADDRESS = "mint_address"
        const val COLUMN_TOKEN_NAME = "token_name"
        const val COLUMN_ICON_URL = "icon_url"
        const val COLUMN_PRICE = "price"
        const val COLUMN_TOTAL = "total"
        const val COLUMN_COLOR = "color"
        const val COLUMN_EXCHANGE_RATE = "exchange_rate"
        const val COLUMN_VISIBILITY = "visibility"
    }
}