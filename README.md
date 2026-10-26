# Sistema de Gestión de Pedidos - JPA / Hibernate & H2

Este proyecto es un sistema de consola desarrollado en **Java 21** para la asignatura **Programación III** (Tecnicatura Universitaria en Programación - UTN). Su objetivo es administrar el ciclo de vida completo de Productos y Categorías utilizando la especificación **JPA (Jakarta Persistence API)** con **Hibernate** como proveedor de persistencia y la base de datos relacional **H2**.

---

## 🚀 Características Principales

* **Arquitectura Limpia & Repositorio Genérico:** Implementación de `BaseRepository<T>` abstracto para el manejo genérico de transacciones y operaciones CRUD.
* **Baja Lógica Garantizada:** Ninguna entidad se elimina físicamente de la base de datos; la baja opera cambiando el estado a `eliminado = true`.
* **Relaciones Unidireccionales:** Mapeo `@OneToMany` desde `Categoria` hacia `Producto` administrado directamente en la base de datos.
* **Consultas JPQL Avanzadas:** Proyecciones y consultas tipadas con `JOIN` navegando la colección unidireccional y agrupando resultados en arreglos de datos (`List<Object[]>`).
* **Interfaz de Consola Robusta:** Navegación por menús interactivos, manejo de excepciones defensivo, soporte para campos opcionales e impresiones tabuladas mediante `System.out.printf`.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Persistencia:** JPA 3.0 / Jakarta EE
* **Proveedor ORM:** Hibernate 6.x
* **Base de Datos:** H2 Database Engine (modo archivo local: `./data/jpa_db`)
* **Herramientas de Utilidad:** Lombok (`@SuperBuilder`, `@Getter`, `@Setter`)
* **Gestor de Construcción:** Gradle

---

## 📋 Requisitos Previos

Para compilar y ejecutar el proyecto necesitas tener instalado en tu equipo:
* **JDK 21** o superior instalado y configurado en tus variables de entorno (`JAVA_HOME`).
* Un entorno de desarrollo integrado (IDE) como **IntelliJ IDEA**, **Eclipse** o **VS Code**.
* Plugin de **Lombok** activado en tu IDE.

---

## ⚙️ Configuración del Entorno de Persistencia

El archivo `persistence.xml` (ubicado en `src/main/resources/META-INF/persistence.xml`) configura la unidad de persistencia `parcial2PU` conectándose a H2 en modo archivo:

```xml
<property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>
<property name="jakarta.persistence.jdbc.url" value="jdbc:h2:file:./data/jpa_db;AUTO_SERVER=TRUE"/>
<property name="jakarta.persistence.jdbc.user" value="sa"/>
<property name="jakarta.persistence.jdbc.password" value=""/>
<property name="hibernate.hbm2ddl.auto" value="update"/>
```

---

## 💻 Instrucciones para Ejecutar el Proyecto

### Opción 1: Desde IntelliJ IDEA / Eclipse

1. Abre el proyecto en tu IDE importándolo como un proyecto **Gradle**.
2. Espera a que se descarguen e indexen todas las dependencias declaradas.
3. Navega en el árbol de archivos hasta la clase principal:  
   `src/main/java/com/tp/jpa/Main.java`
4. Haz clic derecho sobre el archivo `Main.java` y selecciona **Run 'Main.main()'**.

---

### Opción 2: Desde la Terminal de Comandos


```bash
# Limpiar y compilar el proyecto
./gradlew clean build

# Ejecutar la aplicación
./gradlew run
```

---

## 📖 Guía de Uso del Menú interactivo

Al iniciar la aplicación, se mostrará el menú principal:

```text
#############################################
====    SISTEMA DE GESTIÓN DE PEDIDOS    ====
#############################################

========================
     MENÚ PRINCIPAL     
========================
 1. Gestionar Categorías
 2. Gestionar Productos
 3. Reportes
 0. Salir
------------------------
```

### Funcionalidades Destacadas:
1. **Gestionar Categorías:**
    * **Alta:** Requiere un nombre obligatorio y una descripción opcional.
    * **Modificación:** Permite actualizar campos de forma individual (presionar *Enter* conserva el valor previo).
    * **Baja Lógica:** Verifica automáticamente si existen productos activos asociados. Si los hay, impedirá la baja e informará la cantidad de productos vinculados.
2. **Gestionar Productos:**
    * **Alta:** Exige seleccionar una categoría activa existente y cargar nombre, precio ($>0$), stock ($\ge 0$) e imagen.
    * **Baja Lógica:** Marca el campo `eliminado = true` en la base de datos sin romper la integridad referencial.
3. **Reportes:**
    * Ejecuta la consulta JPQL uniendo `Categoria` y `Producto` mediante un `JOIN` para traer el listado completo de productos activos especificando el nombre de su categoría asociada.

---

## 🧑‍💻 Autor

* **Desarrollador:** Juan Franco Cavallieri
* **Materia:** Programación III
* **Carrera:** Tecnicatura Universitaria en Programación a Distancia - **UTN**