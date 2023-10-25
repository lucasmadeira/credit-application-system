package br.com.lucasmadeira.creditapplicationsystem.service.impl

import br.com.lucasmadeira.creditapplicationsystem.entity.Customer
import br.com.lucasmadeira.creditapplicationsystem.exception.BussinessException
import br.com.lucasmadeira.creditapplicationsystem.repository.CustomerRepository
import br.com.lucasmadeira.creditapplicationsystem.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository:CustomerRepository): ICustomerService {
    override fun save(customer: Customer): Customer =
        this.customerRepository.save(customer)


    override fun findById(id: Long): Customer =
        this.customerRepository.findById(id).orElseThrow{throw BussinessException("ID $id not found")}


    override fun delete(id: Long) {
        val customer: Customer = this.findById(id)
        this.customerRepository.delete(customer)
    }
}