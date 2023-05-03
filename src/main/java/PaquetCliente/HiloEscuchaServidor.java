/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaquetCliente;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author Christian
 */
public class HiloEscuchaServidor extends Thread {

    private BufferedReader entrada;

    public HiloEscuchaServidor(BufferedReader entrada) {
        this.entrada = entrada;
    }

    @Override
    public void run() {
        try {
            String mensaje;
            // Leemos los mensajes del servidor y los imprimimos en la consola

            while ((mensaje = entrada.readLine()) != null) {

                System.out.println(mensaje);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //************************************************************************
    public boolean ContieneSintaxis(String codigo, String data) {
        String cadena = "";
        for (int i = 0; i < codigo.length(); i++) {
            if (codigo.charAt(i) != data.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public void Parsear(String data) {
        data = data.substring(1, data.length() - 1);
        // System.out.println("data: " + data);
        String v[] = data.split(",");
        // System.out.println("v.length: " + v.length);
        String id;
        String mensaje;
        if (v.length >= 2) {
            if (ContieneSintaxis("tipo:", v[0]) && ContieneSintaxis("mensaje:", v[1])) {
                // System.out.println("mensaje correcto");
                id = v[0].substring(3, v[0].length());
                mensaje = v[1].substring(8, v[1].length());
                //sendMessageAll(id, mensaje);
            } else {
                System.out.println("ERROR FORMATO SINTAXIS NO VALIDA");
            }
        } else {
            System.out.println("ERROR MENSAJE SIN FORMATO");
        }
    }
}
