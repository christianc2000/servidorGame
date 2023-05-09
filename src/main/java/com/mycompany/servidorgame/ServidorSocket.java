/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servidorgame;

import PaqueteHilosServidorSocket.HiloConexion;
import PaqueteHilosServidorSocket.HiloMensaje;
import PaqueteHilosServidorSocket.HiloPing;
import PaqueteInterfaceServidorGame.EventUserConnected;
import PaqueteInterfaceServidorGame.EventUserData;
import PaqueteInterfaceServidorGame.InterfaceServidorGame;
import PaqueteInterfaceServidorSocket.Escuchadores;
import PaqueteInterfaceServidorSocket.EventMensaje;
import PaqueteInterfaceServidorSocket.EventPing;
import PaqueteInterfaceServidorSocket.EventSesion;
import PaqueteInterfaceServidorSocket.InterfaceServidorSocket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private ConcurrentHashMap<String, Socket> Clientes = new ConcurrentHashMap<>();
    EventListenerList listenerList = new EventListenerList();

    public ServidorSocket(int puerto) {
        this.puerto = puerto;
    }

    public synchronized ConcurrentHashMap<String, Socket> getClientes() {
        return Clientes;
    }

    public void iniciar() {
        try {

            HiloPing ping = new HiloPing(this);
            ping.addMyEventListener(this);
            ping.start();
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
        ConcurrentHashMap<String, Object> atributosCliente = new ConcurrentHashMap<>(); // HashMap para almacenar los atributos del cliente
        Clientes.put(clienteId, socket); // Agregar el cliente al mapa usando el identificador único como clave
        sendMessageId(clienteId, "<ID:"+clienteId+",TIPO:I>");
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

    @Override
    public void onDesconexionCliente(EventPing evento) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        System.out.println("****DESDE EL EVENTO DESCONEXION*******");
        System.out.println(evento.getId() + "Cliente se desconecto");
        System.out.println("cantidad: "+Clientes.size());
        Clientes.remove(evento.getId());
        System.out.println("cantidad: "+Clientes.size());
        System.out.println("**************************************");
    }

    public Socket getClient(String id) {
        return Clientes.get(id);
    }

    public void sendMessageAll(String id, String mensaje) {

        for (ConcurrentHashMap.Entry<String, Socket> entry : Clientes.entrySet()) {
            String key = (String) entry.getKey();
            Socket val = entry.getValue();
            //System.out.println("key: " + key + "; id: " + id);
            if (!key.equals(id)) {
                sendMessageId(key, id + ":" + mensaje);
            }
        }

    }

    public void sendMessageId(String Id, String mensaje) {
        //ExecutorService executor = Executors.newFixedThreadPool(1);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        HiloTask task = new HiloTask();
        Socket socket = (Socket) Clientes.get(Id);
        executor.execute(() -> task.sendMessageSocket(socket, mensaje));

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
