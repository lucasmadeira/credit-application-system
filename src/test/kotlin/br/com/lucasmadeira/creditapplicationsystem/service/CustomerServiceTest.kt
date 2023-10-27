package br.com.lucasmadeira.creditapplicationsystem.service

import br.com.lucasmadeira.creditapplicationsystem.entity.Address
import br.com.lucasmadeira.creditapplicationsystem.entity.Customer
import br.com.lucasmadeira.creditapplicationsystem.exception.BussinessException
import br.com.lucasmadeira.creditapplicationsystem.repository.CustomerRepository
import br.com.lucasmadeira.creditapplicationsystem.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK lateinit var customerRepository: CustomerRepository
    @InjectMockKs lateinit var customerService:CustomerService

    @Test
    fun `should create customer`(){
        //given
        val fakeCustomer = buildCustomer()
        every { customerRepository.save(any()) } returns fakeCustomer

        //when
        val savedCustomer: Customer = customerService.save(fakeCustomer)

        //then
        assertThat(savedCustomer).isNotNull()
        assertThat(savedCustomer).isSameAs(fakeCustomer)
        verify ( exactly = 1 ) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should find customer by id` (){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)

        //then
         val searchedCustomer = customerService.findById(fakeId)

        //when
        assertThat(searchedCustomer).isNotNull()
        assertThat(searchedCustomer).isEqualTo(fakeCustomer)
        verify ( exactly = 1 ) { customerRepository.findById(fakeId)}
    }

    @Test
    fun `should not find customer by invalid id and throw BussinessException`(){
        //given
         val fakeId: Long = Random().nextLong()
        every { customerRepository.findById(fakeId) } returns Optional.empty()
        //when
        //then
        assertThatExceptionOfType(BussinessException::class.java)
            .isThrownBy { customerService.findById(fakeId) }
            .withMessage("Id $fakeId not found")

    }

    @Test
    fun `should delete customer by id`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId)} returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer)} just runs

        //when
        customerService.delete(fakeId)

        //then
        verify ( exactly = 1 ) { customerRepository.findById(fakeId)}
        verify ( exactly = 1 ) { customerRepository.delete(fakeCustomer)}
    }

    private fun buildCustomer(
        firstName: String = "Cami",
        lastName: String = "Camilo",
        cpf: String = "12345678910",
        email: String = "camila@gmail.com",
        password: String = "12345",
        zipCode: String = "12345",
        street: String = "Rua da Cami",
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
}