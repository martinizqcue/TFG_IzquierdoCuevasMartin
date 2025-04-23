package com.example.sevillanasmaneras

import org.junit.Assert.*
import org.junit.Test

class RegistroValidatorTest {

    @Test
    fun `cuando todos los campos son válidos retorna null`() {
        val resultado = RegistroValidator.validarDatos("Juan", "juan@email.com", "123456")
        assertNull(resultado)
    }

    @Test
    fun `cuando el nombre está vacío retorna error`() {
        val resultado = RegistroValidator.validarDatos("", "juan@email.com", "123456")
        assertEquals("Por favor complete todos los campos", resultado)
    }

    @Test
    fun `cuando el correo está vacío retorna error`() {
        val resultado = RegistroValidator.validarDatos("Juan", "", "123456")
        assertEquals("Por favor complete todos los campos", resultado)
    }

    @Test
    fun `cuando la contraseña está vacía retorna error`() {
        val resultado = RegistroValidator.validarDatos("Juan", "juan@email.com", "")
        assertEquals("Por favor complete todos los campos", resultado)
    }

    @Test
    fun `cuando la contraseña tiene menos de 6 caracteres retorna error`() {
        val resultado = RegistroValidator.validarDatos("Juan", "juan@email.com", "123")
        assertEquals("La contraseña debe tener al menos 6 caracteres", resultado)
    }

    @Test
    fun `cuando todos los campos están vacíos retorna error`() {
        val resultado = RegistroValidator.validarDatos("", "", "")
        assertEquals("Por favor complete todos los campos", resultado)
    }
}