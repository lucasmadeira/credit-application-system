package br.com.lucasmadeira.creditapplicationsystem.repository
import br.com.lucasmadeira.creditapplicationsystem.ennumaration.Status
import br.com.lucasmadeira.creditapplicationsystem.entity.Address
import br.com.lucasmadeira.creditapplicationsystem.entity.Credit
import br.com.lucasmadeira.creditapplicationsystem.entity.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {

    @Autowired lateinit var creditRepository: CreditRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @BeforeEach
    fun setup(){
        customer = testEntityManager.persist(buildCustomer())
        credit1 = testEntityManager.persist(buildCredit(customer = customer))
        credit2 = testEntityManager.persist(buildCredit(customer = customer))
    }


    @Test
    fun `should find credit by credit code`(){
        //given
         val creditCode1 = credit1.creditCode
         val creditCode2 = credit2.creditCode
        //when
        val fakeCredit = creditRepository.findByCreditCode(creditCode1)
        val fakeCredit2 = creditRepository.findByCreditCode(creditCode2)
        //then
        Assertions.assertThat(fakeCredit).isNotNull()
        Assertions.assertThat(fakeCredit2).isNotNull()
    }

    @Test
    fun `should find credit by customer id`(){
        //given
        val customerId:Long = customer.id!!
        //when
        val credits : List<Credit> = creditRepository.findAllByCustomerID(customerId)
        //then
        Assertions.assertThat(credits).isNotEmpty()
        Assertions.assertThat(credits.size).isEqualTo(2)
        Assertions.assertThat(credits).contains(credit1,credit2)
    }


    private fun buildCredit(
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(60),
        numberOfInstallments: Int = 5,
        customer: Customer
    ): Credit = Credit(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer
    )
    private fun buildCustomer(
        firstName: String = "Lucas",
        lastName: String = "Madeira",
        cpf: String = "28475934625",
        email: String = "lucas@gmail.com",
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
}