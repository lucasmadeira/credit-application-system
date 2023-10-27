package br.com.lucasmadeira.creditapplicationsystem.controller

import br.com.lucasmadeira.creditapplicationsystem.dto.*
import br.com.lucasmadeira.creditapplicationsystem.service.impl.CreditService
import br.com.lucasmadeira.creditapplicationsystem.service.impl.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/credits")
class CreditResource(
    private val creditService: CreditService
) {

    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDTO: CreditDto): ResponseEntity<CreditView> {
        val savedCredit = this.creditService.save(creditDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(CreditView(savedCredit))
    }

    @GetMapping
    fun findByCustomerId(@RequestParam(value ="customerId") customerId:Long): ResponseEntity<List<CreditViewList>>{
        val credits = this.creditService.findAllByCustomer(customerId);
        return ResponseEntity.ok(credits.map { CreditViewList(it) }.toList())
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId:Long,
                         @PathVariable creditCode:UUID) : ResponseEntity<CreditView>  {
        val credit = this.creditService.findByCreditCode(customerId, creditCode) ?:return ResponseEntity.notFound().build()
        return ResponseEntity.ok(CreditView(credit))
    }

}