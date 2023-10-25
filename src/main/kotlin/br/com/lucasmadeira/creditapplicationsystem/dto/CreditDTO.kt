package br.com.lucasmadeira.creditapplicationsystem.dto

import br.com.lucasmadeira.creditapplicationsystem.entity.Credit
import br.com.lucasmadeira.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(
    @field:NotNull(message = "creditValue cannot be null") val creditValue: BigDecimal,
    @field:Future(message= "day first installment must be in future") val dayFirstInstallment: LocalDate,
    @field:Min(value = 3, message = "number of installments must be greater than 2")val numberOfInstallments: Int,
    @field:NotNull(message = "customerId cannot be null")val customerId:Long) {
    fun toEntity(): Credit  =  Credit(
        null,
        creditValue =  this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}