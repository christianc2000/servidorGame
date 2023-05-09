/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaqueteInterfaceCliente;

import java.net.Socket;
import java.util.EventObject;

/**
 *
 * @author Christian
 */
public class EventClientConnected extends EventObject{
    Socket socket;

    public EventClientConnected(Socket socket, Object source) {
        super(source);
        System.out.println("se inicializa el evento");
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
    
    
    
}
