/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaqueteHilosServidorSocket;

import PaqueteInterfaceServidorSocket.EventMensaje;
import PaqueteInterfaceServidorSocket.EventSesion;
import PaqueteInterfaceServidorSocket.InterfaceServidorSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class HiloMensaje extends Thread {

    private BufferedReader entradaCliente;
    private PrintWriter salidaCliente;
    Socket socket;
    EventListenerList listenerList = new EventListenerList();

    public HiloMensaje(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Obtenemos los streams de entrada y salida del socket para comunicarnos con el cliente
            entradaCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salidaCliente = new PrintWriter(socket.getOutputStream(), true);
            String mensaje;
            // Leemos los mensajes del cliente y los imprimimos en la consola
            while ((mensaje = entradaCliente.readLine()) != null) {
                // System.out.println("Hilo Mensaje Servidor: Mensaje recibido del cliente");
                EventMensaje evento = new EventMensaje(mensaje);
                notificarOnMensajeEvent(evento);
            }
        } catch (SocketException e) {
            // Manejo de la excepción SocketException
            System.out.println("Se ha cerrado la conexión del cliente de manera abrupta.");
            // Puedes cerrar los recursos y finalizar el hilo de manera adecuada aquí
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cerrar el hilo cuando el cliente se desconecta
           // System.out.println("Cerrando el hilo Mensaje del servidor con el cliente...");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMyEventListener(InterfaceServidorSocket listener) {
        listenerList.add(InterfaceServidorSocket.class, listener);
    }

    public void removeMyEventListener(InterfaceServidorSocket listener) {
        listenerList.remove(InterfaceServidorSocket.class, listener);
    }

    void notificarOnMensajeEvent(EventMensaje evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceServidorSocket.class) {
                ((InterfaceServidorSocket) listeners[i + 1]).onMessageCliente(evt);
            }
        }
    }
}
