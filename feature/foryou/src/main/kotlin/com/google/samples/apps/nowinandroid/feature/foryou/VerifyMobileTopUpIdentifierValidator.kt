package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.annotation.StringRes
import java.io.Serializable
import javax.inject.Inject

abstract class AddMobileTopUpTemplateValidator : ValidatorWithErrorType<
    AddMobileTopUpTemplateValidator.Params,
    AddMobileTopUpTemplateValidator.Params,
    AddMobileTopUpTemplateValidator.Error
    >() {

    data class Params(
        val mobileNumber: String?,
        val serviceProvider: MTUServiceProviderFace?,
        val paymentService: MTUPaymentServiceFace?,
        val isAllDataRequired: Boolean? = null
    )

    data class Error(
        @StringRes var emptyMobileNumber: Int? = null,
        @StringRes var emptyServiceProvider: Int? = null,
        @StringRes var emptyPaymentService: Int? = null,
    )
}

class AddMobileTopUpTemplateValidatorImpl @Inject constructor() : AddMobileTopUpTemplateValidator() {
    override fun doValidation(data: Params): ValidatorResult<Params, Error> {
        var isValid = true
        val error = Error()
        if (data.mobileNumber.isNullOrEmpty()) {
            error.emptyMobileNumber = R.string.feature_foryou_done
            isValid = false
        }

        if (data.serviceProvider == null && data.isAllDataRequired == true) {
            error.emptyServiceProvider = R.string.feature_foryou_done
            isValid = false
        }

        if (data.paymentService == null && data.isAllDataRequired == true) {
            error.emptyPaymentService = R.string.feature_foryou_done
            isValid = false
        }

        return if (isValid) {
            ValidatorResult.Success(data)
        } else {
            ValidatorResult.Failure(error)
        }
    }
}

data class MTUServiceProviderFace(
    val id: Long,
    val name: String,
    val shortName: String
) : Serializable

data class MTUPaymentServiceFace(
    val id: Long,
    val name: String,
    val isMobilePackage: Boolean,
    val allowedPaymentAmount: Long?,
    val packageDescriptions: Map<String, String>?,
    val serviceProvider: MTUServiceProviderFace? = null
) : Serializable
