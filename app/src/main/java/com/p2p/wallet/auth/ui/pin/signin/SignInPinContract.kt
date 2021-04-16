package com.p2p.wallet.auth.ui.pin.signin

import com.p2p.wallet.common.mvp.MvpPresenter
import com.p2p.wallet.common.mvp.MvpView
import javax.crypto.Cipher

interface SignInPinContract {
    interface View : MvpView {
        fun onSignInSuccess()
        fun onLogout()
        fun showBiometricDialog(cipher: Cipher)
        fun showWrongPinError(attemptsLeft: Int)
        fun showWalletLocked()
        fun showLoading(isLoading: Boolean)
        fun vibrate(duration: Long)
        fun clearPin()
    }

    interface Presenter : MvpPresenter<View> {
        fun signIn(pinCode: String)
        fun signInByBiometric(cipher: Cipher)
        fun onBiometricSignInRequested()
        fun checkIfBiometricAvailable()
        fun logout()
    }
}