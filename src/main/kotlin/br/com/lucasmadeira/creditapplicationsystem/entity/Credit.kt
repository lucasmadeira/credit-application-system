package br.com.lucasmadeira.creditapplicationsystem.entity

import br.com.lucasmadeira.creditapplicationsystem.ennumaration.Status
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "credit")
data class Credit(
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    val id:Long?,
    @Column(nullable = false, unique = true)
    val creditCode: UUID = UUID.randomUUID(),
    @Column(nullable = false)
    val creditValue: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false)
    val dayFirstInstallment: LocalDate,
    @Column(nullable = false)
    val numberOfInstallments: Int,

    @Enumerated(EnumType.STRING)
    val status: Status = Status.IN_PROGRESS,
    @ManyToOne
    var customer: Customer?,
)