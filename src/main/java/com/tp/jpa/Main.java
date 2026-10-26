package com.tp.jpa;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.util.JPAUtil;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final CategoriaRepository categoriaRepo = new CategoriaRepository();

    public static void main(String[] args) {

        //Filtrar el nivel de logs que la librería de Hibernate imprime en la consola
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        Scanner sc = new Scanner(System.in);
        boolean ejecutando = true;

        System.out.println("#############################################");
        System.out.println("====    SISTEMA DE GESTIÓN DE PEDIDOS    ====");
        System.out.println("#############################################");

        while(ejecutando){
            mostrarMenuPrincipal();
            int opc = leerEntero("Seleccione una opción: ");

            switch (opc){
                case 1 -> menuCategorias();
                case 2 -> menuProductos();
                case 3 -> menuReportes();
                case 0 -> ejecutando = false;
                default -> System.out.println("  [!] Opción inválida. Intente de nuevo.");
            }
        }
        System.out.println("\n  Cerrando conexión con la base de datos...");
        JPAUtil.close();
        System.out.println("  ¡Hasta luego!");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n========================");
        System.out.println("     MENÚ PRINCIPAL     ");
        System.out.println("========================");
        System.out.println(" 1. Gestionar Categorías");
        System.out.println(" 2. Gestionar Productos");
        System.out.println(" 3. Reportes");
        System.out.println(" 0. Salir");
        System.out.println("------------------------");
    }

    // Metodos Menu Principal
    public static void menuCategorias(){
        boolean ejecutando = true;
        while(ejecutando){
            System.out.println("\n  -- Submenu: Categorías ------");
            System.out.println("  1. Alta de categoría");
            System.out.println("  2. Modificar categoría");
            System.out.println("  3. Baja lógica de categoría");
            System.out.println("  4. Listar categorías activas");
            System.out.println("  0. Volver al menú principal");
            System.out.println("  -------------------------------");
            int opc = leerEntero("Seleccione una opción: ");

            switch (opc){
                case 1 -> altaCategoria();
                case 2 -> modificarCategoria();
                case 3 -> bajaCategoria();
                case 4 -> listarCategorias();
                case 0 -> ejecutando = false;
                default -> System.out.println("  [!] Opción inválida. Intente de nuevo.");
            }
        }
    }

    public static void menuProductos(){
        boolean ejecutando = true;
        while(ejecutando){
            System.out.println("\n  -- Submenu: Productos ------");
            System.out.println("  1. Alta de producto");
            System.out.println("  2. Modificar producto");
            System.out.println("  3. Baja lógica de producto");
            System.out.println("  4. Listar productos activos");
            System.out.println("  0. Volver al menú principal");
            System.out.println("  -------------------------------");
            int opc = leerEntero("Seleccione una opción: ");

            switch (opc){
                case 1 -> altaProducto();
                case 0 -> ejecutando = false;
                default -> System.out.println("  [!] Opción inválida. Intente de nuevo.");
            }
        }
    }

    public static void menuReportes(){
        boolean ejecutando = true;
        while(ejecutando){
            System.out.println("\n  -- Submenu: Reportes ------");
            System.out.println("  1. Productos por categoría (JPQL)");
            System.out.println("  0. Volver al menú principal");
            System.out.println("  -------------------------------");
            int opc = leerEntero("Seleccione una opción: ");

            switch (opc){
                case 1 -> System.out.println("Aca mostras el reporte");
                case 0 -> ejecutando = false;
                default -> System.out.println("  [!] Opción inválida. Intente de nuevo.");
            }
        }
    }

    // Metodos Categorias
    public static void altaCategoria(){
        System.out.println("\n[ALTA DE CATEGORÍA]");
        System.out.print("Ingrese el nombre de la categoría (Obligatorio): ");
        String nombre = scanner.nextLine().trim();

        if(nombre.isEmpty()){
            System.out.println("  [!] ERROR: El nombre de la categoría no puede estar vacío.");
            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
            return;
        }

        System.out.print("Ingrese descripción (Opcional): ");
        String descripcion = scanner.nextLine().trim();

        Categoria nuevaCategoria = Categoria.builder()
                .nombre(nombre)
                .descripcion(descripcion.isEmpty() ? null : descripcion)
                .build();

        try {
            Categoria catPersistida = categoriaRepo.guardar(nuevaCategoria);
            System.out.println("\n  -- Categoría creada con éxito [Id: " + catPersistida.getId() + "]");
        } catch (Exception e) {
            System.out.println("\n  [!] ERROR: No se pudo guardar la categoría.");
            System.out.println("  Mas Info: " + e.getMessage());
        }

        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    public static void modificarCategoria(){
        if(!listarCategoriasActivas()){
            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
            return;
        }
        Long id = (long)leerEntero("Ingrese el ID de la categoría a modificar: ");
        Optional<Categoria> optCategoria = categoriaRepo.buscarPorId(id);

        if(optCategoria.isEmpty() || optCategoria.get().isEliminado()){
            System.out.println("\n  [!] No se encontró una categoría activa con el ID " + id);
            System.out.println("  Presione Enter para continuar...");
            scanner.nextLine();
            return;
        }

        Categoria cat = optCategoria.get();
        System.out.println("\nValores actuales -> [Nombre: " + cat.getNombre() + " | Descripción: " + cat.getDescripcion() + "]");
        System.out.println("(Deje el campo en blanco y presione ENTER si no desea modificar el valor)");

        System.out.print("Nuevo Nombre: ");
        String nuevoNombre = scanner.nextLine().trim();
        if(!nuevoNombre.isEmpty()){
            cat.setNombre(nuevoNombre);
        }

        System.out.print("Nueva Descripción: ");
        String nuevaDescripcion = scanner.nextLine().trim();
        if(!nuevaDescripcion.isEmpty()){
            cat.setDescripcion(nuevaDescripcion);
        }

        categoriaRepo.guardar(cat);
        System.out.println("\n  [*] Categoría modificada con éxito.");
        System.out.println("  Presione Enter para continuar...");
        scanner.nextLine();
    }

    public static void bajaCategoria(){
        if(!listarCategoriasActivas()){
            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
            return;
        }

        Long id = (long)leerEntero("Ingrese el ID de la categoría a eliminar: ");

        Optional<Categoria> optCategoria = categoriaRepo.buscarPorId(id);

        if (optCategoria.isEmpty() || optCategoria.get().isEliminado()) {
            System.out.println("\n  [!] Error: Categoría no encontrada o ya dada de baja.");
            System.out.println("  Presione Enter para continuar...");
            scanner.nextLine();
            return;
        }

        Categoria cat = optCategoria.get();

        List<Producto> productosActivos = categoriaRepo.buscarProductosPorCategoria(id);
        if(!productosActivos.isEmpty()){
            System.out.println("\n  [!] La categoría no se puede dar de baja porque tiene "
                    + productosActivos.size() + " producto(s) activo(s) asociado(s).");
            System.out.println("  Debe dar de baja o reasignar los productos primero.");
            System.out.println("  Presione Enter para continuar...");
            scanner.nextLine();
            return;
        }

        try {
            boolean exito = categoriaRepo.eliminarLogico(id);

            if (exito) {
                System.out.println("\n  [✓] Baja lógica confirmada para la categoría: " + cat.getNombre());
            } else {
                System.out.println("\n  [!] Error: Categoría no encontrada o ya dada de baja");
            }
        }catch (Exception e){
            System.out.println("\n  [!] ERROR al intentar la baja lógica.");
            System.out.println("  Mas info: " + e.getMessage());
        }

        System.out.println("  Presione Enter para continuar...");
        scanner.nextLine();

    }

    public static void listarCategorias(){
        System.out.println("\n>> [Listado de Categorías Activas]");
        listarCategoriasActivas();
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    public static boolean listarCategoriasActivas(){
        List<Categoria> catActivas = categoriaRepo.listarActivos();

        if(catActivas.isEmpty()){
            System.out.println("  [!] No hay categorías activas registradas.");
            return false;
        }

        System.out.println("\n>> [Listado de Categorías Activas]");
        System.out.printf("%-5s %-20s %-30s%n", "ID", "Nombre", "Descripción");
        System.out.println("------------------------------------------------------------------");

        for (Categoria cat : catActivas){
            String descripcion = Optional.ofNullable(cat.getDescripcion())
                    .filter(d -> !d.isBlank())
                    .orElse("Sin descripción");

            System.out.printf("%-5d %-20s %-30s%n", cat.getId(), cat.getNombre(), descripcion);
        }
        System.out.println("------------------------------------------------------------------");
        return true;
    }

    // Metodos Productos
    public static void altaProducto(){
        if(!listarCategoriasActivas()){
            System.out.println("  (Agregue una Categoría antes de agregar un Producto)");
            System.out.println("  Presione Enter para continuar...");
            scanner.nextLine();
            return;
        }

        System.out.println("=== INGRESAR NUEVO PRODUCTO:");
        System.out.println("(Presione Enter para dejar vacío");
        Long idCategoria = (long)leerEntero("\nID Categoría (Obligatorio):  ");
        Optional<Categoria> optCategoria = categoriaRepo.buscarPorId(idCategoria);

        if(optCategoria.isEmpty() || optCategoria.get().isEliminado()){
            System.out.println("\n  [!] Error: La categoría seleccionada no existe o está dada de baja.");
            System.out.println("  Presione Enter para continuar...");
            scanner.nextLine();
            return;
        }

        Categoria categoria = optCategoria.get();


        String nombre = leerCadena("  Nombre: ");

        System.out.print("  Descripción: ");
        String descripcion = scanner.nextLine().trim();

        Double precio = leerDouble("  Precio: ");

        System.out.print("  URL Imagen: ");
        String imagen = scanner.nextLine().trim();

        System.out.print("  ¿Está disponible? (S/N) [Por Defecto S]: ");
        String respuesta = scanner.nextLine().trim().toUpperCase();

        boolean disponible = !respuesta.equals("N");

        Producto nuevoProducto = Producto.builder()
                .nombre(nombre)
                .descripcion(descripcion.isBlank() ? null : descripcion)
                .precio(precio)
                .imagen(imagen.isBlank() ? null : imagen)
                .disponible(disponible)
                .build();

        try {
            Producto productoPersistido = categoriaRepo.agregarProducto(idCategoria, nuevoProducto);
            System.out.println("\n  [✓] Producto agregado con éxito. ");
            System.out.println("  ID generado: " + productoPersistido.getId());
            System.out.println("  Categoría asignada: " + categoria.getNombre());
        } catch (Exception e) {
            System.out.println("\n  [!] Error al dar de alta el producto: " + e.getMessage());
        }
    }

    // Metodos auxiliares
    private static int leerEntero(String mensaje){
        while(true){
            try{
                System.out.print(mensaje);
                String entrada = scanner.nextLine().trim();
                int entradaInt = Integer.parseInt(entrada);
                if(entradaInt >= 0){
                    return entradaInt;
                }else{
                    System.out.println("  [!] Entrada inválida. Debe ingresar un número mayor o igual a 0.");
                }
            }catch(NumberFormatException e){
                System.out.println("  [!] Entrada inválida. Debe ingresar un número entero.");
            }
            System.out.println();
        }
    }

    private static Double leerDouble(String mensaje){
        while(true){
            try {
                System.out.print(mensaje);
                String entrada = scanner.nextLine().trim();
                Double entradaDouble = Double.parseDouble(entrada);
                if(entradaDouble > 0) {
                    return entradaDouble;
                }else{
                    System.out.println("  [!] Entrada inválida. Debe ingresar un número positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Entrada inválida. Debe ingresar un número entero o decimal.");
            }
        }
    }

    private static String leerCadena(String mensaje){
        while(true){
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();

            if(!entrada.isBlank()){
                return entrada;
            }

            System.out.println("  [!] ERROR: Este campo no puede estar vacío. Intente de nuevo.\n");
        }
    }
}
