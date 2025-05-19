# TFG_IzquierdoCuevasMartin
# 📱 Sevillanas Maneras

**Sevillanas Maneras** es una aplicación Android desarrollada como Proyecto Fin de Ciclo que permite al usuario descubrir lugares tradicionales, monumentos, cultura y curiosidades de Sevilla, incluyendo vídeos explicativos extraídos desde YouTube gracias a la integración con la **YouTube Data API v3**.

El usuario puede iniciar sesión, explorar lugares según su tipo, visualizar información detallada y guardar sus lugares favoritos. También puede ver recomendaciones y abrir la ubicación del lugar directamente en **Google Maps**.

---

## ✅ Características principales

- Autenticación de usuarios con **Firebase Authentication**
- Base de datos en **Firebase Firestore** (usuarios y lugares)
- Integración de vídeos explicativos mediante la **YouTube Data API v3**
- Visualización directa de los vídeos desde la app (embebidos)
- Gestión de favoritos personales
- Acceso al mapa del lugar desde **Google Maps**
- Vista de perfil con lista de favoritos
- Diseño adaptado a móviles y colores personalizados

---

## 🛠️ Requisitos para ejecutar la app

> ⚠️ Es **altamente recomendable usar un dispositivo móvil físico** en lugar de un emulador, ya que este último puede dar problemas de rendimiento, compatibilidad con Google Maps o reproducción de vídeos de YouTube.

### Requisitos técnicos:

- ✅ Android Studio **Hedgehog o superior**
- ✅ JDK **11**
- ✅ SDK mínimo: `24` / Target SDK: `35`
- ✅ Dispositivo físico **con YouTube y Google Maps instalados**
- ✅ Conexión a internet (Wi-Fi o datos)
- ✅ Clave de API de YouTube habilitada

---

## 🔐 Clave de API y restricción de uso

- Esta aplicación utiliza la **YouTube Data API v3** para obtener vídeos relacionados con cada lugar.
- La clave API **está restringida por defecto**. Si deseas probar la aplicación con esta funcionalidad activa, **debes contactar al desarrollador para solicitar el desbloqueo temporal** de la clave y poder realizar las búsquedas correctamente.
- **IMPORTANTE**: Tras las pruebas, la clave será restringida de nuevo por motivos de seguridad.

---

## 🧱 Tecnologías utilizadas

- Kotlin + Android Jetpack
- Firebase Authentication
- Firebase Firestore (modo de desarrollo seguro)
- Retrofit2 + Gson (consumo de APIs REST)
- YouTube Data API v3
- ViewBinding
- Material Design Components

---

## ⚙️ Funcionalidades

| Funcionalidad                  | Descripción                                                                 |
|-------------------------------|-----------------------------------------------------------------------------|
| Registro / inicio de sesión   | Permite autenticarse con email y contraseña                                |
| Explorar lugares              | Muestra una lista según categoría (tradiciones, monumentos, etc.)          |
| Ver detalles de un lugar      | Descripción, vídeo explicativo y recomendaciones                           |
| Añadir / eliminar favoritos   | Guarda o quita lugares en el perfil del usuario                            |
| Ver mapa del lugar            | Redirige a la app de Google Maps con la ubicación almacenada en Firestore |
| Ver perfil                    | Información personal + lugares favoritos                                   |

---

## 👤 Gestión por parte del administrador

- El administrador (tú) puede modificar, crear o eliminar elementos desde **Firestore** directamente (colección `elementos`).
- También puede administrar usuarios (colección `usuarios`).
- No se ha implementado una interfaz de gestión desde la app, pero es una **vía futura**.

---

## 🔄 Limitaciones y decisiones técnicas

- ❌ No se usa **Firebase Storage** por problemas de regiones europeas. Las imágenes están incluidas directamente en `/res/drawable` para evitar problemas de acceso desde regiones europeas.
- 🔒 El uso de Google Maps embebido fue descartado por necesidad de activación de **facturación**. Se ha implementado un botón que abre la app de Google Maps directamente si está instalada.
- ⚠️ El sistema de autenticación no tiene recuperación de contraseña implementado.

---

## 📋 Consideraciones para pruebas

1. Avisa al desarrollador si vas a probar la funcionalidad de vídeos embebidos (para desactivar temporalmente la restricción de clave API).
2. Asegúrate de usar un **dispositivo físico con Google Maps y YouTube instalados**.
3. La base de datos Firebase usada está **en modo de desarrollo**, sin reglas de seguridad estrictas (solo para pruebas locales).

---

## 📌 Licencia y uso

Este proyecto ha sido creado como trabajo de fin de ciclo. No se autoriza su distribución comercial ni su publicación sin autorización previa.

---

**Desarrollado por:**  
Martín Izquierdo Cuevas – ILERNA Sevilla  
Curso: 2º DAM – Curso 2024/2025  
