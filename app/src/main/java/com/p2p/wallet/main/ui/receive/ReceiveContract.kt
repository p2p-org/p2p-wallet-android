package com.p2p.wallet.main.ui.receive

import android.graphics.Bitmap
import com.p2p.wallet.common.mvp.MvpPresenter
import com.p2p.wallet.common.mvp.MvpView
import com.p2p.wallet.token.model.Token

interface ReceiveContract {

    interface View : MvpView {
        fun renderQr(qrBitmap: Bitmap?)
        fun showReceiveToken(token: Token)
        fun showSolAddress(token: Token)
        fun showFullScreenLoading(isLoading: Boolean)
        fun showQrLoading(isLoading: Boolean)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadData()
    }
}