/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servidorgame;

import PaqueteHilosServidorSocket.HiloConexion;
import PaqueteHilosServidorSocket.HiloMensaje;
import PaqueteInterfaceServidorGame.EventUserConnected;
import PaqueteInterfaceServidorGame.EventUserData;
import PaqueteInterfaceServidorGame.InterfaceServidorGame;
import PaqueteInterfaceServidorSocket.EventMensaje;
import PaqueteInterfaceServidorSocket.EventSesion;
import PaqueteInterfaceServidorSocket.InterfaceServidorSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class ServidorSocket implements InterfaceServidorSocket {

    private int puerto;
    private ServerSocket servidor;
    private Map<String, Socket> Clientes = new HashMap<>();
    EventListenerList listenerList = new EventListenerList();

    public ServidorSocket(int puerto) {
        this.puerto = puerto;
    }

    public void iniciar() {
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("*******Servidor Socket iniciado*******");
            HiloConexion hc = new HiloConexion(servidor);
            hc.addMyEventListener(this);
            hc.start();
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Agregar cliente a la lista con HashMap
    public void agregarCliente(String clienteId, Socket socket) {
        Map<String, Object> atributosCliente = new HashMap<>(); // HashMap para almacenar los atributos del cliente
        Clientes.put(clienteId, socket); // Agregar el cliente al mapa usando el identificador único como clave
        sendMessageId(clienteId, clienteId);
    }

    public String generarClienteId() {
        return UUID.randomUUID().toString(); // Utilizar la clase UUID para generar un identificador único
    }

    @Override
    public void onSesionCliente(EventSesion evento) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        System.out.println("se conectó un nuevo cliente: " + evento.getSocket());
        String clienteId = generarClienteId();
        agregarCliente(clienteId, evento.getSocket());
        HiloMensaje hm = new HiloMensaje(evento.getSocket());
        hm.addMyEventListener(this);
        hm.start();
        EventUserConnected euc = new EventUserConnected(clienteId, this);
        // System.out.println("crea el evento");
        notificarOnSesionEvent(euc);
        // System.out.println("despues del evento");
    }

    @Override
    public void onMessageCliente(EventMensaje evento) {
        // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        //System.out.println("Cliente: " + evento.getMensaje());
        EventUserData eud = new EventUserData(evento.getMensaje());
        notificarOnDataEvent(eud);
        //Parsear(evento.getMensaje());
    }

    public Socket getClient(String id) {
        return Clientes.get(id);
    }

    public void sendMessageAll(String id, String mensaje) {

        for (Map.Entry<String, Socket> entry : Clientes.entrySet()) {
            String key = (String) entry.getKey();
            Socket val = entry.getValue();
            //System.out.println("key: " + key + "; id: " + id);
            if (!key.equals(id)) {
                sendMessageId(key, id + ":" + mensaje);
            }
        }
    }

    public void sendMessageId(String Id, String mensaje) {
        Socket socket = (Socket) Clientes.get(Id);
        try {
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            salida.println(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(Socket socket, String mensaje) {
        try {
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            salida.println(mensaje);
            //salida.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //********************************************************************
    public void addMyEventListener(InterfaceServidorGame listener) {
        listenerList.add(InterfaceServidorGame.class, listener);
    }

    public void removeMyEventListener(InterfaceServidorGame listener) {
        listenerList.remove(InterfaceServidorGame.class, listener);
    }

    void notificarOnSesionEvent(EventUserConnected evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceServidorGame.class) {
                ((InterfaceServidorGame) listeners[i + 1]).onUserConnected(evt);
            }
        }
    }

    void notificarOnDataEvent(EventUserData evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceServidorGame.class) {
                ((InterfaceServidorGame) listeners[i + 1]).onUserData(evt);
            }
        }
    }
}
