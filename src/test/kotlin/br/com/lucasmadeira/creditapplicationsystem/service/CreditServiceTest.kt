package br.com.lucasmadeira.creditapplicationsystem.service

import br.com.lucasmadeira.creditapplicationsystem.ennumaration.Status
import br.com.lucasmadeira.creditapplicationsystem.entity.Address
import br.com.lucasmadeira.creditapplicationsystem.entity.Credit
import br.com.lucasmadeira.creditapplicationsystem.entity.Customer
import br.com.lucasmadeira.creditapplicationsystem.exception.BussinessException
import br.com.lucasmadeira.creditapplicationsystem.repository.CreditRepository
import br.com.lucasmadeira.creditapplicationsystem.repository.CustomerRepository
import br.com.lucasmadeira.creditapplicationsystem.service.impl.CreditService
import br.com.lucasmadeira.creditapplicationsystem.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerRepository: CustomerRepository

    @InjectMockKs lateinit var creditService:CreditService

    @Test
    fun `should create credit`(){
        //given
        val fakeCustomer = buildCustomer()
        every { customerRepository.findById(any()) } returns Optional.of(fakeCustomer)

        val fakeCredit = buildCredit()
        every { creditRepository.save(any()) } returns fakeCredit


        //when
        val savedCredit: Credit = creditService.save(fakeCredit)

        //then
        assertThat(savedCredit).isNotNull()
        assertThat(savedCredit).isSameAs(fakeCredit)
        verify ( exactly = 1 ) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should not save a credit by invalid customer id and throw BussinessException`(){

        //given
        val fakeCredit = buildCredit()
        val customer : Customer? = fakeCredit.customer

        every { customer?.id?.let { customerRepository.findById(it) } } returns Optional.empty()

        //when
        //then
        Assertions.assertThatExceptionOfType(BussinessException::class.java)
            .isThrownBy { creditService.save(fakeCredit) }
            .withMessage("Customer not found")
    }

    @Test
    fun `should find credits by customer id` (){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        val listCredits = listOf(buildCredit(customer = fakeCustomer),
            buildCredit(id = Random().nextLong(),creditCode = UUID.randomUUID(), customer = fakeCustomer))
        every { creditRepository.findAllByCustomerID(fakeId) } returns listCredits

        //when
        val searchedCredits = creditService.findAllByCustomer(fakeId)

        //then
        assertThat(searchedCredits).isNotEmpty()
        assertThat(searchedCredits.size).isEqualTo(2)
        verify ( exactly = 1 ) { creditRepository.findAllByCustomerID(fakeId) }
    }

    @Test
    fun `should find credits by code` (){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        val credit = buildCredit(customer = fakeCustomer)

        every { creditRepository.findByCreditCode(credit.creditCode)} returns credit

        //when
        val searchedCredit = creditService.findByCreditCode(fakeId,credit.creditCode)

        //then
        assertThat(searchedCredit).isNotNull()
        assertThat(searchedCredit).isEqualTo(credit)
        verify ( exactly = 1 ) { creditRepository.findByCreditCode(credit.creditCode) }
    }

    @Test
    fun `should not find credits by code and  throw ILLegalException` (){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        val credit = buildCredit(customer = fakeCustomer)

        every { creditRepository.findByCreditCode(credit.creditCode)} returns null

        //when
        //then
        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeId, credit.creditCode) }
            .withMessage("Credit not found with creditcode ${credit.creditCode}")

    }

    @Test
    fun `should not find credits by code and  throw RuntimeException` (){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        val credit = buildCredit(customer = fakeCustomer)

        every { creditRepository.findByCreditCode(credit.creditCode)} returns credit

        //when
        //then
        Assertions.assertThatExceptionOfType(RuntimeException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeId+1, credit.creditCode) }
            .withMessage("Contact admin")

    }


    private fun buildCustomer(
        firstName: String = "Lucas",
        lastName: String = "Madeira",
        cpf: String = "12345678910",
        email: String = "lucas@gmail.com",
        password: String = "12345",
        zipCode: String = "12345",
        street: String = "Rua da Lucas",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        id: Long = 1L): Customer{
        return Customer(
            firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            email = email,
            password = password,
            address = Address(
                zipCode = zipCode,
                street = street),
            income = income,
            id = id
        )
    }


    private fun buildCredit(
        creditCode: UUID = UUID.randomUUID(),
        customer: Customer = buildCustomer(),
        id: Long = 1L,
        status: Status = Status.IN_PROGRESS,
        creditValue: BigDecimal = BigDecimal.valueOf(10000),
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(30),
        numberOfInstallments: Int = 3): Credit{
        return Credit(
            creditCode = creditCode,
            customer = customer,
            id = id,
            status = status,
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments
        )
    }
}