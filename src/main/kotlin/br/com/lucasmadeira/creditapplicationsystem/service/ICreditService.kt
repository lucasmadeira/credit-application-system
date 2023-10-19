package br.com.lucasmadeira.creditapplicationsystem.service

import br.com.lucasmadeira.creditapplicationsystem.entity.Credit
import java.util.UUID

interface ICreditService {
    fun save(credit:Credit):Credit
    fun findAllByCustomer(customerID:Long):List<Credit>
    fun findByCreditCode(customerID:Long,creditCode: UUID): Credit
}