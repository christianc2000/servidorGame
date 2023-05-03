/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaquetCliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class Cliente {

    Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    String id;

    public void setId(String id) {
        this.id = id;
    }

    public void iniciar(String host, int puerto) {

        try {
            // Creamos un socket para conectarnos al servidor
            socket = new Socket(host, puerto);
            // Obtenemos los streams de entrada y salida del socket
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            setId(entrada.readLine());

            System.out.println("id cliente: " + id);
            // Creamos un hilo para escuchar los mensajes del servidor

            // Leemos los mensajes desde el teclado y los enviamos al servidor
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            HiloEscuchaServidor hes = new HiloEscuchaServidor(entrada);
            hes.start();
            // System.out.println("Escribe un mensaje");
            String mensaje;
            boolean b = true;
            String i = null;
            int c = 1;
            while ((mensaje = teclado.readLine()) != null) {
                if (b) {
                    if (mensaje.equals("1")) {
                        i = mensaje;
                        b = false;
                        mensaje = "<ID:" + id + ",DATA:*" + mensaje + "*>";
                        salida.println(mensaje);
                    } else if (mensaje.equals("2")) {
                        i = mensaje;
                        b = false;
                        mensaje = "<ID:" + id + ",DATA:*" + mensaje + "*>";
                        salida.println(mensaje);
                    } else {
                        System.out.println("ERROR OPCION NO VALIDA");
                    }
                } else {

                    if (i.equals("1")) {
                        
                        mensaje = "<ID:" + id + ",DATA:"+mensaje+",TIPO:R,C:" + c + ">";
                        c++;
                       // System.out.println("c: "+c);
                        salida.println(mensaje);
                    } else if (i.equals("2")) {
                        mensaje = "<ID:" + id + ",DATA:"+mensaje+",TIPO:L,C:" + c + ">";
                        c++;
                        salida.println(mensaje);
                    } else {
                        System.out.println("************************************");
                        System.out.println("Yo: " + mensaje);
                        mensaje = "<ID:" + id + ",DATA:" + mensaje + ">";
                        salida.println(mensaje);
                    }
                    if(c==3){
                        i="3";
                    }
                }
            }
            // Cerramos el socket y los streams
            socket.close();
            entrada.close();
            salida.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void protocoloRegister(BufferedReader entrada, PrintWriter salida, BufferedReader teclado) {
        try {
            System.out.println(entrada.readLine());//nombre
            String nombre = teclado.readLine();
            salida.println(nombre);
            System.out.println(entrada.readLine());//password
            String password = teclado.readLine();
            salida.println(password);

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void protocoloLogin() {

    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.iniciar("localhost", 5000);
    }
}
