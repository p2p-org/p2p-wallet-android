package com.p2p.wallet.token

import com.p2p.wallet.common.di.InjectionModule
import com.p2p.wallet.token.interactor.TokenInteractor
import com.p2p.wallet.token.repository.TokenRemoteRepository
import com.p2p.wallet.token.repository.TokenRepository
import com.p2p.wallet.token.ui.TokenDetailsContract
import com.p2p.wallet.token.ui.TokenDetailsPresenter
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

object TokenModule : InjectionModule {

    override fun create(): Module = module {

        factory { TokenRemoteRepository(get()) } bind TokenRepository::class
        factory { TokenInteractor(get()) }
        single { TokenDetailsPresenter(get(), get(), get()) } bind TokenDetailsContract.Presenter::class
    }
}