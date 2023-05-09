/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaqueteInterfaceServidorSocket;

import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class Escuchadores {
     protected EventListenerList listenerList = new EventListenerList();

    public void addMyEventListener(InterfaceServidorSocket listener) {
        listenerList.add(InterfaceServidorSocket.class, listener);
    }

    public void removeMyEventListener(InterfaceServidorSocket listener) {
        listenerList.remove(InterfaceServidorSocket.class, listener);
    }

    void notificarDesconexion(EventPing evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceServidorSocket.class) {
                ((InterfaceServidorSocket) listeners[i + 1]).onDesconexionCliente(evt);
            }
        }
    }

    void notificarOnSesionEvent(EventSesion evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceServidorSocket.class) {
                ((InterfaceServidorSocket) listeners[i + 1]).onSesionCliente(evt);
            }
        }
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
