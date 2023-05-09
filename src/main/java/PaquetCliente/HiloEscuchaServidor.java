/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaquetCliente;

import PaqueteInterfaceCliente.EventClientConnected;
import PaqueteInterfaceCliente.EventMessageServer;
import PaqueteInterfaceCliente.InterfaceCliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class HiloEscuchaServidor extends Thread {

    EventListenerList listenerList = new EventListenerList();

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
                System.out.println("S: " + mensaje);
                EventMessageServer ems = new EventMessageServer(mensaje, this);
                this.notificarOnMessageServer(ems);
            }
            //System.out.println("se sale del hilo");
        } catch (SocketException e) {

            System.err.println("Se ha perdido la conexión con el servidor: " + e.getMessage());

        } catch (IOException e) {
            if (e.getMessage().equals("Socket closed")) {

            } else {
                e.printStackTrace();
            }
        } finally {
            System.out.println("Cliente desconectado desde el hilo escucha mensaje, cerrando el hilo...");

            try {
                entrada.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
//********************************************************************************

    public void addMyEventListener(InterfaceCliente listener) {
        listenerList.add(InterfaceCliente.class, listener);
    }

    public void removeMyEventListener(InterfaceCliente listener) {
        listenerList.remove(InterfaceCliente.class, listener);
    }

    void notificarOnMessageServer(EventMessageServer evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceCliente.class) {
                ((InterfaceCliente) listeners[i + 1]).OnMessageServer(evt);
            }
        }
    }
}
