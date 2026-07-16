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
2. Crear un archivo `.env` en la raíz (usar `env.example` como guía) con las variables necesarias (`DB_URL`, `DB_USER`, `DB_PASSWORD`).
3. Cargar el proyecto en IntelliJ IDEA mediante el archivo `pom.xml`.

## Flujo de Trabajo (Git)
- **Regla de oro:** Nunca trabajar directo en `main`.
- Mantener la rama `main` como la versión estable y limpia.
- Hacer `git pull` siempre antes de empezar a trabajar.
- Al finalizar una meta, realizar el merge correspondiente a la rama `main`.

### Cómo empezar a trabajar en tu rama asignada
Las ramas de trabajo ya han sido creadas en el repositorio remoto. Para empezar a trabajar directamente, sigue estos pasos al clonar el proyecto por primera vez:

1. **Clonar el proyecto:**
   ```bash
   git clone https://github.com/MiguelE0911/SIGBC-DS3.git

2. **Actualizar la lista de ramas locales:**
    ```bash
    git fetch --all

3. **Cambiar a tu rama asignada:**
    ```bash
    git checkout feature/nombre-de-la-rama
   
**Ramas disponibles**
- feature/espectador
- feature/personal
- feature/admin
- feature/db