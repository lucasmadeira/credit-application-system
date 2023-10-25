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
    var id:Long?,
    @Column(nullable = false, unique = true)
    var creditCode: UUID = UUID.randomUUID(),
    @Column(nullable = false)
    var creditValue: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false)
    var dayFirstInstallment: LocalDate,
    @Column(nullable = false)
    var numberOfInstallments: Int,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.IN_PROGRESS,
    @ManyToOne
    var customer: Customer?,
)