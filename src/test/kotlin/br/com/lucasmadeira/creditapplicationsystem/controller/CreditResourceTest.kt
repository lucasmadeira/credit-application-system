package br.com.lucasmadeira.creditapplicationsystem.controller

import br.com.lucasmadeira.creditapplicationsystem.dto.CreditDto
import br.com.lucasmadeira.creditapplicationsystem.dto.CustomerDTO
import br.com.lucasmadeira.creditapplicationsystem.dto.CustomerUpdateDto
import br.com.lucasmadeira.creditapplicationsystem.ennumaration.Status
import br.com.lucasmadeira.creditapplicationsystem.entity.Address
import br.com.lucasmadeira.creditapplicationsystem.entity.Credit
import br.com.lucasmadeira.creditapplicationsystem.entity.Customer
import br.com.lucasmadeira.creditapplicationsystem.repository.CreditRepository
import br.com.lucasmadeira.creditapplicationsystem.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {

    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var creditRepository: CreditRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper
               private lateinit var  customer:Customer
               private lateinit var  credit1:Credit
               private lateinit var  credit2:Credit

    companion object{
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup(){
        customerRepository.deleteAll()
        creditRepository.deleteAll()
        customer = customerRepository.save(buildCustomer())
        credit1 = creditRepository.save(buildCredit(customer = customer))
        credit2 = creditRepository.save(buildCredit(customer = customer))
    }

    @AfterEach
    fun tearDown(){
        customerRepository.deleteAll()
        creditRepository.deleteAll()
    }

    @Test
    fun `should create a credit and return 201 status`(){
        //given
        val creditDto = builderCreditDto()
        val valueAsString =  objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").isNotEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(BigDecimal.valueOf(10000)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.IN_PROGRESS.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("lucas@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(BigDecimal.valueOf(1000.0)))
            .andDo(MockMvcResultHandlers.print())

    }

   @Test
    fun `should not save a credit without customer  and return 400 status`(){
       //given
       val creditDto = builderCreditDto(customerId = 10)
       val valueAsString =  objectMapper.writeValueAsString(creditDto)
       //when
       //then
       mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class br.com.lucasmadeira.creditapplicationsystem.exception.BussinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }


    @Test
    fun `should find credits by id customer return 200 status`() {
        //given
        val customerId: Long? = customer.id
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .param("customerId", customerId.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").isNotEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(BigDecimal.valueOf(500)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value(5))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find credit by id customer and credit code return 200 status`() {
        //given
        val customerId: Long? = customer.id
        val creditCode: UUID = credit1.creditCode
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$creditCode")
                .param("customerId", customerId.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").isNotEmpty())
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(BigDecimal.valueOf(500)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.IN_PROGRESS.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value("lucas@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(BigDecimal.valueOf(1000.0)))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find credit by valid id customer and credit code return 400 status`() {
        //given
        val customerId: Long? = customer.id
        val creditCode: UUID = UUID.randomUUID()
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$creditCode")
                .param("customerId", customerId.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class java.lang.IllegalArgumentException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find credit by invalid id customer and credit code return 400 status`() {
        //given
        val customerId: Long? = customer.id?.plus(10)
        val creditCode: UUID = credit1.creditCode
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$creditCode")
                .param("customerId", customerId.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class br.com.lucasmadeira.creditapplicationsystem.exception.BussinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }


    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(10000),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusDays(60),
        numberOfInstallments: Int = 1,
        customerId: Long? = customer.id
    ) = customerId?.let {
          CreditDto(
            creditValue = creditValue,
            dayFirstOfInstallment = dayFirstOfInstallment,
            numberOfInstallments = numberOfInstallments,
            customerId = it,
        )
    }

    private fun buildCustomer(
        firstName: String = "Lucas",
        lastName: String = "Madeira",
        cpf: String = "28475934625",
        email: String = "lucas@email.com",
        password: String = "12345",
        zipCode: String = "12345",
        street: String = "Rua do Lucas",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street,
        ),
        income = income,
    )
    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        dayFirstInstallment: LocalDate = LocalDate.of(2024, Month.APRIL, 22),
        numberOfInstallments: Int = 5,
        customer: Customer
    ): Credit = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )
}