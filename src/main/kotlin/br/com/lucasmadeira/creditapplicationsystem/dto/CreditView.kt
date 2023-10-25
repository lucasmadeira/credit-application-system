package br.com.lucasmadeira.creditapplicationsystem.dto

import br.com.lucasmadeira.creditapplicationsystem.ennumaration.Status
import br.com.lucasmadeira.creditapplicationsystem.entity.Credit
import java.math.BigDecimal
import java.util.UUID


data class CreditView(
    val creditCode: UUID,
    val creditValue:BigDecimal,
    val numberOfInstallments:Int,
    val status:Status,
    val emailCustomer:String?,
    val incomeCustomer: BigDecimal?
) {
    constructor(credit:Credit): this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments,
        status = credit.status,
        emailCustomer = credit.customer?.email,
        incomeCustomer = credit.customer?.income,
    )
}