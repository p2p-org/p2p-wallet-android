package com.p2p.wallet.common.network

class Constants {
    companion object {
        const val BASE_URL: String = "https://pokeapi.co/api/v2/"
        const val EXPLORER_SOLANA: String = "https://explorer.solana.com/tx/"
        const val EXPLORER_SOLANA_ADD_TOKEN: String = "https://explorer.solana.com/address/"
        const val PUBLIC_KEY_LENGTH: Int = 44
        const val OWNER_SOL = "11111111111111111111111111111111"
        const val WRAPPED_SOL_MINT = "So11111111111111111111111111111111111111112"
        const val ERROR_NULL_DATA = 1000
        const val REQUEST_EXACTION = 1001
        const val PREFERENCE_SAVED_ERROR = 1002
        const val VERIFY_PIN_CODE_ERROR = 1003
        const val ERROR_INCORRECT_PHRASE = 1004
        const val ERROR_TIME_OUT = 1005
        const val ERROR_EMPTY_ALL_WALLETS = 1006
        const val ERROR_WALLET_ITEM_IS_NULL = 1007

        const val SWAP_PROGRAM_ID = "DjVE6JNiYqPL2QXyCUUh8rNjHrbz9hXHNYt99MQ59qw1"
    }
}