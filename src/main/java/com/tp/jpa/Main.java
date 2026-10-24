package com.tp.jpa;

import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Consultar sobre
        //Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        Scanner sc = new Scanner(System.in);
        boolean ejecutando = true;

        System.out.println("#############################################");
        System.out.println("====    SISTEMA DE GESTIÓN DE PEDIDOS    ====");
        System.out.println("#############################################");

        while(ejecutando){
            mostrarMenuPrincipal();
            int opc = Integer.parseInt(sc.nextLine());

            if(opc == 1) {
                System.out.println("Sos un capo Juan!");

            }
            if(opc == 0) ejecutando = false;

        }
        System.out.println("Gracias por usar nuestro software!");

    }

    public static void mostrarMenuPrincipal(){
        System.out.println("========================");
        System.out.println("     MENÚ PRINCIPAL     ");
        System.out.println("========================");
        System.out.println(" 1. Gestionar Categorías");
        System.out.println(" 2. Gestionar Productos");
        System.out.println(" 3. Reportes");
        System.out.println(" 0. Salir");
        System.out.println("------------------------");
        System.out.print("Seleccione una opción: ");
    }


}
