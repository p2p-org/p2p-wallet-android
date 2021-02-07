package com.wowlet.domain.usecases

import com.wowlet.data.datastore.WowletApiCallRepository
import com.wowlet.domain.interactors.QrScannerInteractor
import com.wowlet.entities.CallException
import com.wowlet.entities.Constants
import com.wowlet.entities.Result
import org.json.JSONException
import org.json.JSONObject
import org.p2p.solanaj.core.PublicKey

class QrScannerUseCase(
    private val wowletApiCallRepository: WowletApiCallRepository,
) : QrScannerInteractor {

    override suspend fun getAccountInfo(publicKey: String): Result<String> {
        return try {
            val accountInfo = wowletApiCallRepository.getQRAccountInfo(PublicKey(publicKey))
            if (accountInfo.value != null) {
                try {
                    (accountInfo.value.data as? ArrayList<*>)?.let {
                        if (it.isNotEmpty() && it[0] == "") {
                            if (accountInfo.value.owner == Constants.OWNER_SOL) {
                                return Result.Success(accountInfo.value.owner)
                            }
                        }
                    }
                    val jsonObject = JSONObject(accountInfo.value.data.toString())
                    val parsed = jsonObject.getString("parsed")
                    if (parsed != "null") {
                        val parsedJson = JSONObject(parsed)
                        val info = parsedJson.getString("info")
                        val infoJson = JSONObject(info)
                        val mint = infoJson.getString("mint")
                        Result.Success(mint)
                    } else {
                        Result.Success(accountInfo.value.owner)
                    }
                } catch (e: JSONException) {
                    Result.Error(CallException(Constants.ERROR_NULL_DATA, e.message))
                }
            } else {
                Result.Error(CallException(Constants.ERROR_NULL_DATA, "Wallet not found"))
            }
        } catch (e: Exception) {
            Result.Error(CallException(Constants.REQUEST_EXACTION, e.message))
        }
    }
}
