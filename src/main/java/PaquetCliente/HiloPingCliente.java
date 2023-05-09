/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaquetCliente;

import PaqueteInterfaceCliente.EventClientConnected;
import PaqueteInterfaceCliente.EventClientPing;
import PaqueteInterfaceCliente.InterfaceCliente;
import java.net.Socket;
import javax.swing.event.EventListenerList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Christian
 */
public class HiloPingCliente extends Thread {

    Socket socket;
    EventListenerList listenerList = new EventListenerList();

    public HiloPingCliente(Socket socket) {
        this.socket = socket;
    }

    public void verifica() {
        while (true) {
            
            if (socket.isClosed() || !socket.isConnected()) {
                
                System.out.println("socket desconectado");
               EventClientPing evento=new EventClientPing(false, this);
               this.notificarOnDesconexion(evento);
               break;
            }
        }

    }

    public void run() {
        verifica();
    }

    //*******************************************
    public void addMyEventListener(InterfaceCliente listener) {
        listenerList.add(InterfaceCliente.class, listener);
    }

    public void removeMyEventListener(InterfaceCliente listener) {
        listenerList.remove(InterfaceCliente.class, listener);
    }

    void notificarOnDesconexion(EventClientPing evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceCliente.class) {
                ((InterfaceCliente) listeners[i + 1]).OnPing(evt);
            }
        }
    }
}
