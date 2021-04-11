package com.p2p.wowlet.backupwallat.manualsecretkeys.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.p2p.wowlet.deprecated.viewmodel.BaseViewModel
import com.p2p.wowlet.backupwallat.interactor.ManualSecretKeyInteractor
import com.p2p.wowlet.common.network.Result
import com.p2p.wowlet.dashboard.model.local.SecretKeyItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManualSecretKeyViewModel(private val manualSecretKeyInteractor: ManualSecretKeyInteractor) :
    BaseViewModel() {

    private val _getPhraseData by lazy { MutableLiveData<SecretKeyItem>() }
    val getPhraseData: LiveData<SecretKeyItem> get() = _getPhraseData
    private val _resultResponseData by lazy { MutableLiveData<Boolean>() }
    val resultResponseData: LiveData<Boolean> get() = _resultResponseData

    fun randomItemClickListener(secretKeyItem: SecretKeyItem) {
        getResult(secretKeyItem)
        _getPhraseData.value = secretKeyItem
    }

    private fun getResult(secretKeyItem: SecretKeyItem) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val data = manualSecretKeyInteractor.sortingSecretKey(secretKeyItem)) {
                is Result.Success -> withContext(Dispatchers.Main) {
                    _resultResponseData.value = data.data
                }
            }
        }
    }
}