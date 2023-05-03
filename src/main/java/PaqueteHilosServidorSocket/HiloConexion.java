/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaqueteHilosServidorSocket;

import PaqueteInterfaceServidorSocket.EventSesion;
import PaqueteInterfaceServidorSocket.InterfaceServidorSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class HiloConexion extends Thread {

    ServerSocket servidor;
    EventListenerList listenerList = new EventListenerList();

    public HiloConexion(ServerSocket servidor) {
        this.servidor = servidor;
    }

    public void iniciar() {
        while (true) {
            try {
                Socket clienteSocket = servidor.accept();
                EventSesion evento=new EventSesion(clienteSocket);
                notificarOnSesionEvent(evento);
                
            } catch (IOException ex) {
                Logger.getLogger(HiloConexion.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void run() {
        iniciar();
    }
 public void addMyEventListener(InterfaceServidorSocket listener) {
    listenerList.add(InterfaceServidorSocket.class, listener);
  }
  public void removeMyEventListener(InterfaceServidorSocket listener) {
    listenerList.remove(InterfaceServidorSocket.class, listener);
  }
  void notificarOnSesionEvent(EventSesion evt) {
    Object[] listeners = listenerList.getListenerList();
    for (int i = 0; i < listeners.length; i = i+2) {
      if (listeners[i] == InterfaceServidorSocket.class) {
        ((InterfaceServidorSocket) listeners[i+1]).onSesionCliente(evt);
      }
    }
  }
}
