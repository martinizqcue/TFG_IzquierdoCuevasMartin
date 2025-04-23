package com.example.sevillanasmaneras

object RegistroValidator {
    fun validarDatos(nombre: String, email: String, password: String): String? {
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            return "Por favor complete todos los campos"
        }
        if (password.length < 6) {
            return "La contraseña debe tener al menos 6 caracteres"
        }
        return null // Todo correcto
    }
}