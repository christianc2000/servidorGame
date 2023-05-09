/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaquetCliente;

import PaqueteInterfaceCliente.EventClientConnected;
import PaqueteInterfaceCliente.InterfaceCliente;
import PaqueteInterfaceServidorSocket.EventMensaje;
import PaqueteInterfaceServidorSocket.InterfaceServidorSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class HiloClienteConexion extends Thread {

    EventListenerList listenerList = new EventListenerList();
   private final String host;
    private final int puerto;
    

    public HiloClienteConexion(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
       
    }

    @Override
    public void run() {
        Socket socket;
        while (true) {
            try {
                socket = new Socket(host, puerto);
                EventClientConnected ecc=new EventClientConnected(socket, this);
                this.notificarOnConexion(ecc);
               // cliente.setSocket(socket);
               // cliente.firePropertyChange("conexion", false, true);
                break;
            } catch (IOException e) {
                // Si hay un error al conectar, seguimos intentando
            }
        }
        System.out.println("finaliza Hilo Conexion cliente");
    }
//********************************************************************************
    public void addMyEventListener(InterfaceCliente listener) {
        listenerList.add(InterfaceCliente.class, listener);
    }

    public void removeMyEventListener(InterfaceCliente listener) {
        listenerList.remove(InterfaceCliente.class, listener);
    }

    void notificarOnConexion(EventClientConnected evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceCliente.class) {
                ((InterfaceCliente) listeners[i + 1]).OnConnecting(evt);
            }
        }
    }

}
