package br.com.lucasmadeira.creditapplicationsystem.repository

import br.com.lucasmadeira.creditapplicationsystem.entity.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CreditRepository: JpaRepository<Credit, Long> {

    fun findByCreditCode(creditCode:UUID):Credit?

    @Query("SELECT * FROM credit WHERE customer_id= ?1 ", nativeQuery = true)
    fun findAllByCustomerID(customerID:Long): List<Credit>


}