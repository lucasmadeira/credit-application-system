package br.com.lucasmadeira.creditapplicationsystem.repository

import br.com.lucasmadeira.creditapplicationsystem.entity.Credit
import br.com.lucasmadeira.creditapplicationsystem.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {
}