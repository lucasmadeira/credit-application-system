package br.com.lucasmadeira.creditapplicationsystem.dto

import br.com.lucasmadeira.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "firstName cannot be null") val firstName:String,
    @field:NotEmpty(message = "lastName cannot be null")val lastName:String,
    @field:NotNull(message = "income cannot be null")val income:BigDecimal,
    @field:NotEmpty(message = "zipCode cannot be null")val zipCode:String,
    @field:NotEmpty(message = "street cannot be null")val street:String
) {

    fun toEntity(customer:Customer):Customer{
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street
        return customer
    }

}
