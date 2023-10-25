package br.com.lucasmadeira.creditapplicationsystem.controller

import br.com.lucasmadeira.creditapplicationsystem.dto.CustomerDTO
import br.com.lucasmadeira.creditapplicationsystem.dto.CustomerUpdateDto
import br.com.lucasmadeira.creditapplicationsystem.dto.CustomerView
import br.com.lucasmadeira.creditapplicationsystem.service.impl.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/customers")
class CustomerResource(
    private val customerService: CustomerService
) {

    @PostMapping
    fun saveCustomer(@RequestBody @Valid customerDTO: CustomerDTO): ResponseEntity<String>{
        val savedCustomer = this.customerService.save(customerDTO.toEntity())
        return ResponseEntity.ok("Customer ${savedCustomer.id} saved!")
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id:Long): ResponseEntity<CustomerView>{
        val customer = this.customerService.findById(id);
        if(customer == null){
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(CustomerView(customer))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id:Long) = this.customerService.delete(id)

    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customerId") id:Long,
                       @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
    ):ResponseEntity<CustomerView>{

        var  customer = this.customerService.findById(id)
        if(customer!=null){
           val customerSaved = this.customerService.save(customerUpdateDto.toEntity(customer))
            return ResponseEntity.ok(CustomerView(customer))
        }
        return ResponseEntity.notFound().build()
    }

}