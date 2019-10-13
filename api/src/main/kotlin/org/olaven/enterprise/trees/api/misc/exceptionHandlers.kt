package org.olaven.enterprise.trees.api.misc

import org.olaven.enterprise.trees.api.dto.WrappedResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
open class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    private val INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error"

    @ExceptionHandler(value = [IllegalArgumentException::class])
    protected fun handleIllegalArgument(exception: Exception, request: WebRequest)
            : ResponseEntity<Any> {

        //TODO: perhaps should be combined by passing more exception classses into annotation?
        return handleExceptionInternal(
                exception, null, HttpHeaders(), HttpStatus.valueOf(400), request)
    }

    //NOTE: written by Andrea, TODO: go through / rewrite
    @ExceptionHandler(value = [ConstraintViolationException::class])
    protected fun handleFrameworkExceptionsForUserInputs(exception: Exception, request: WebRequest)
            : ResponseEntity<Any> {

        if(exception is ConstraintViolationException) {

            val messages = StringBuilder()
            for (violation in exception.constraintViolations) {
                messages.append(violation.message + "\n")
            }

            val msg = exception.constraintViolations.map { it.propertyPath.toString() + " " + it.message }
                    .joinToString("; ")

            return handleExceptionInternal(
                    RuntimeException(msg), null, HttpHeaders(), HttpStatus.valueOf(400), request)
        }

        return handleExceptionInternal(
                exception, null, HttpHeaders(), HttpStatus.valueOf(400), request)
    }

    @ExceptionHandler(Exception::class)
    fun `handle unexpected bugs`(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(
                RuntimeException(INTERNAL_SERVER_ERROR_MESSAGE),
                null, HttpHeaders(),
                HttpStatus.valueOf(500), request)
    }


    //NOTE: written by Andrea, TODO: go through / rewrite
    override fun handleExceptionInternal(
            ex: Exception,
            body: Any?,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ) : ResponseEntity<Any> {

        val dto = WrappedResponse<Any>(
                code  = status.value(),
                message = ex.message
        )

        return ResponseEntity(dto, headers, status)
    }
}