package com.p2p.wowlet.domain

import com.p2p.wowlet.auth.AuthInteractor
import com.p2p.wowlet.domain.interactors.*
import com.p2p.wowlet.domain.usecases.*
import org.koin.dsl.module

val interactorsModule = module {
    single<RegLoginInteractor> { RegLoginUseCase(get(), get()) }
    factory<SecretKeyInteractor> { SecretKeyUseCase(get(), get()) }
    factory<ManualSecretKeyInteractor> { ManualSecretKeyUseCase() }
    factory<EnterPinCodeInteractor> { EnterPinCodeUseCase(get()) }
    single<NotificationInteractor> { NotificationUseCase(get()) }
    single<SendCoinInteractor> { SendCoinUseCase(get(), get()) }
    single<DashboardInteractor> { DashBoardUseCase(get(), get(), get(), get()) }
    single<RegFinishInteractor> { RegFinishUseCase(get()) }
    single<CompleteBackupWalletInteractor> { CompleteBackupWalletUseCase(get()) }
    single<SplashScreenInteractor> { AuthInteractor(get()) }
    single<DetailWalletInteractor> { DetailWalletUseCase(get(), get(), get()) }
    single<FingerPrintInteractor> { FingerPrintUseCase(get()) }
    factory<CreateWalletInteractor> { CreateWalletUseCase(get(), get()) }
    factory<PinCodeVerificationInteractor> { PinCodeVerificationUseCase(get()) }
    factory<PinCodeInteractor> { PinCodeUseCase(get()) }
    factory<QrScannerInteractor> { QrScannerUseCase(get()) }
    factory<NetworksInteractor> { NetworksUseCase(get()) }
    factory<SwapInteractor> { SwapUseCase() }
}