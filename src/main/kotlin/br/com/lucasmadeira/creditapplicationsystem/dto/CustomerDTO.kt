package br.com.lucasmadeira.creditapplicationsystem.dto

import br.com.lucasmadeira.creditapplicationsystem.entity.Address
import br.com.lucasmadeira.creditapplicationsystem.entity.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDTO(
   @field:NotEmpty(message = "firstName cannot be null") val firstName:String,
   @field:NotEmpty(message = "lastName cannot be null") val lastName: String,
   @field:CPF(message = "cpf cannot be invalid") val cpf: String,
   @field:NotEmpty(message = "email cannot be null")
   @field:Email
   val email:String,
   @field:NotNull(message = "income cannot be null") val income: BigDecimal,
   @field:NotEmpty(message = "password cannot be null") val password: String,
   @field:NotEmpty(message = "zipCode cannot be null") val zipCode: String,
   @field:NotEmpty(message = "street cannot be null") val street: String
) {
    fun toEntity(): Customer = Customer(
        null,
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        email = this.email,
        income = this.income,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )
}

