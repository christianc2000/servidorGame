/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaqueteHilosServidorSocket;

import PaqueteInterfaceServidorSocket.EventPing;
import PaqueteInterfaceServidorSocket.EventSesion;
import PaqueteInterfaceServidorSocket.InterfaceServidorSocket;
import com.mycompany.servidorgame.ServidorSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class HiloPing extends Thread {

    EventListenerList listenerList = new EventListenerList();
    private ServidorSocket SS;

    public HiloPing(ServidorSocket SS) {
        this.SS = SS;
    }

    public synchronized void run() {
        boolean b = true;
        while (true) {

            ConcurrentHashMap<String, Socket> Clients = SS.getClientes();

            for (ConcurrentHashMap.Entry<String, Socket> entry : Clients.entrySet()) {
                
                String key = entry.getKey();
                Socket socket = entry.getValue();
                try {
                    InetAddress addr = socket.getInetAddress();

                    boolean reachable = addr.isReachable(3000); // 5 segundos de tiempo de espera
                    if (socket.isClosed() || !socket.isConnected()) {
                        System.out.println("address: " + addr);
                        System.out.println("se perdió la conexion con un cliente");
                        EventPing ping = new EventPing(key);
                        notificarDesconexion(ping);
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Error al verificar la conexión del cliente " + key + ": " + e.getMessage());
                }
            }
        }
    }

    //**********************************************
    public void addMyEventListener(InterfaceServidorSocket listener) {
        listenerList.add(InterfaceServidorSocket.class, listener);
    }

    public void removeMyEventListener(InterfaceServidorSocket listener) {
        listenerList.remove(InterfaceServidorSocket.class, listener);
    }

    synchronized void notificarDesconexion(EventPing evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceServidorSocket.class) {
                ((InterfaceServidorSocket) listeners[i + 1]).onDesconexionCliente(evt);
            }
        }
    }
}
