package br.com.lucasmadeira.creditapplicationsystem.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionDetails> {
        val errors : MutableMap<String,String?> = HashMap()
        ex.bindingResult.allErrors.stream().forEach { erro: ObjectError ->
            val fieldName: String = (erro as FieldError).field
            val errorMessage: String? = erro.getDefaultMessage()
            errors[fieldName] = errorMessage
        }

        return ResponseEntity.badRequest().body(ExceptionDetails(title = "Bad Request! Consult the documentation",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            exception = ex.javaClass.toString(),
            details = errors))

    }

    @ExceptionHandler(DataAccessException::class)
    fun handleValidationException(ex: DataAccessException): ResponseEntity<ExceptionDetails> {


        return ResponseEntity(ExceptionDetails(title = "Conflict! Consult the documentation",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.CONFLICT.value(),
            exception = ex.javaClass.toString(),
            details = mutableMapOf(ex.cause.toString() to ex.message)
        ),HttpStatus.CONFLICT)
    }

    @ExceptionHandler(BussinessException::class)
    fun handleValidationException(ex: BussinessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity(ExceptionDetails(title = "Bad Request! Consult the documentation",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            exception = ex.javaClass.toString(),
            details = mutableMapOf(ex.cause.toString() to ex.message)
        ),HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleValidationException(ex: IllegalArgumentException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity(ExceptionDetails(title = "Bad Request! Consult the documentation",
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            exception = ex.javaClass.toString(),
            details = mutableMapOf(ex.cause.toString() to ex.message)
        ),HttpStatus.BAD_REQUEST)
    }
}