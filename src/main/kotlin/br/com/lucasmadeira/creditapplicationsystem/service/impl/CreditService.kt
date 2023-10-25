package br.com.lucasmadeira.creditapplicationsystem.service.impl

import br.com.lucasmadeira.creditapplicationsystem.entity.Credit
import br.com.lucasmadeira.creditapplicationsystem.repository.CreditRepository
import br.com.lucasmadeira.creditapplicationsystem.repository.CustomerRepository
import br.com.lucasmadeira.creditapplicationsystem.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditService(
    private val creditRepository:CreditRepository,
    private val customerRepository:CustomerRepository):ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerRepository.findById(credit.customer?.id!!)
                .orElseThrow{throw RuntimeException("Customer not found")}
        }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerID: Long): List<Credit>  =
        this.creditRepository.findAllByCustomerID(customerID)

    override fun findByCreditCode(customerID:Long,creditCode: UUID): Credit {
      val credit: Credit = this.creditRepository.findByCreditCode(creditCode)
          ?: throw IllegalArgumentException("Credit not found with creditcode $creditCode")

      return if(credit.customer?.id == customerID)credit else throw RuntimeException("Contact admin")
    }
}