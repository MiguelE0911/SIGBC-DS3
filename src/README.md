# Sistema Integral de Gestión y Boletería para Cine (SIGBC)

Este es el repositorio oficial del proyecto para la materia de Desarrollo de Software III.

## Arquitectura
- **Patrón:** MVC con arquitectura híbrida.
- **Módulos:**
    - Espectador
    - Personal
    - Administrador
- **Capa Transversal:** Infraestructura (DB, Seguridad, Sesión).

## Configuración inicial
1. Instalar JDK 21.
2. Crear un archivo `.env` en la raíz (usar `env.example` como guía) con las variables: `DB_URL`, `DB_USER`, `DB_PASSWORD`.
3. Cargar el proyecto en IntelliJ IDEA mediante el `pom.xml`.

## Flujo de Trabajo (Git)
- **Regla de oro:** Nunca trabajar directo en `main`.
- Trabajar dentro de su rama: `feature/<nombre-del-modulo>`
- Al finalizar una meta, hacer merge a la rama `main`.
- Ejecutar `git pull` antes de empezar cada sesión de trabajo.