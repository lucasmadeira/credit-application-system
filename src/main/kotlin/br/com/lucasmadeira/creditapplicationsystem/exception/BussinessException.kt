package br.com.lucasmadeira.creditapplicationsystem.exception

data class BussinessException(override val message: String?): RuntimeException(message)
