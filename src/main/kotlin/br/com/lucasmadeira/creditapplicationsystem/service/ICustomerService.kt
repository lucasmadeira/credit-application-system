package br.com.lucasmadeira.creditapplicationsystem.service

import br.com.lucasmadeira.creditapplicationsystem.entity.Customer

interface ICustomerService {
    fun save(customer:Customer):Customer
    fun findById(id: Long): Customer
    fun delete(id:Long)
}