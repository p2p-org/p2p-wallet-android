package com.p2p.wowlet.root

import android.os.Bundle
import com.p2p.wowlet.R
import com.p2p.wowlet.auth.ui.OnboardingFragment
import com.p2p.wowlet.auth.ui.pincode.view.PinCodeFragment
import com.p2p.wowlet.common.mvp.BaseMvpActivity
import com.p2p.wowlet.auth.model.LaunchMode
import com.p2p.wowlet.utils.popBackStack
import com.p2p.wowlet.utils.replaceFragment
import org.koin.android.ext.android.inject

class RootActivity : BaseMvpActivity<RootContract.View, RootContract.Presenter>(), RootContract.View {

    override val presenter: RootContract.Presenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        if (savedInstanceState == null) {
            presenter.openRootScreen()
        }
    }

    override fun navigateToOnboarding() {
        replaceFragment(OnboardingFragment())
    }

    override fun navigateToSignIn() {
        val target = PinCodeFragment.create(
            openSplashScreen = true,
            isBackupDialog = false,
            type = LaunchMode.VERIFY
        )
        replaceFragment(target)
    }

    override fun onBackPressed() {
        popBackStack()
    }
}