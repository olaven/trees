package org.olaven.enterprise.trees.dto

import io.swagger.annotations.ApiModelProperty


enum class Status {
        SUCCESS, ERROR, FAIL
}

private fun statusFromCode(code: Int) =
        when (code) {
                in 100..399 -> Status.SUCCESS
                in 400..499 -> Status.ERROR
                in 500..599 -> Status.FAIL
                else -> throw IllegalStateException("invalid HTTP code $code")
        }

/**
 * A wrapped response
 */
data class WrappedResponse<T>(
        @ApiModelProperty(value = "The status HTTP status code")
        val code: Int,
        @ApiModelProperty(value = "The data object")
        val data: T,
        @ApiModelProperty(value = "Error message if any")
        val message: String? = null,
        val status: Status = statusFromCode(code)
)