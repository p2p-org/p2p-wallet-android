package com.p2p.wallet.token.model

import androidx.annotation.StringRes
import com.p2p.wallet.R

enum class PeriodHistory(@StringRes val resourceId: Int, val value: Int) {
    ONE_HOUR(R.string.details_1h, 1),
    FOUR_HOURS(R.string.details_4h, 4),
    ONE_DAY(R.string.details_1d, 1),
    ONE_WEEK(R.string.details_1w, 7),
    ONE_MONTH(R.string.details_1m, 30)
}