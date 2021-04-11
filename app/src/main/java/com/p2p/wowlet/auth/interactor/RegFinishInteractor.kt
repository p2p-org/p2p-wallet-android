package com.p2p.wowlet.auth.interactor

import com.p2p.wowlet.datastore.PreferenceService

class RegFinishInteractor(private val preferenceService: PreferenceService) {
    fun finishLoginReg(regFinish: Boolean) {
        preferenceService.finishLoginReg(regFinish)
    }

    fun isCurrentLoginReg(): Boolean = preferenceService.isCurrentLoginReg()
}