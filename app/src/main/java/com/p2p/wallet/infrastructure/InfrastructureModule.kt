package com.p2p.wallet.infrastructure

import android.content.Context
import android.os.Build
import androidx.room.Room
import com.p2p.wallet.common.crypto.keystore.EncoderDecoderMarshmallow
import com.p2p.wallet.common.crypto.keystore.EncoderDecoderPreMarshmallow
import com.p2p.wallet.common.crypto.keystore.KeyStoreWrapper
import com.p2p.wallet.common.di.InjectionModule
import com.p2p.wallet.infrastructure.db.WalletDatabase
import com.p2p.wallet.infrastructure.db.WalletDatabase.Companion.DATABASE_NAME
import com.p2p.wallet.infrastructure.security.SecureStorage
import org.koin.dsl.module
import java.util.concurrent.Executors

object InfrastructureModule : InjectionModule {

    override fun create() = module {

        single {
            Room
                .databaseBuilder(get(), WalletDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .setQueryExecutor(Executors.newCachedThreadPool())
                .build()
        }

        single { get<WalletDatabase>().tokenDao() }

        single {
            val context = get<Context>()
            val name = "${context.packageName}.prefs"
            context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }

        single {
            val encoderDecoder =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    EncoderDecoderMarshmallow(get())
                } else {
                    EncoderDecoderPreMarshmallow(get())
                }

            return@single KeyStoreWrapper(encoderDecoder)
        }

        factory { SecureStorage(get(), get()) }
    }
}