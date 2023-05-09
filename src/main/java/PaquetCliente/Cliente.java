/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaquetCliente;

import PaqueteInterfaceCliente.EventClientConnected;
import PaqueteInterfaceCliente.EventClientPing;
import PaqueteInterfaceCliente.EventMessageServer;
import PaqueteInterfaceCliente.InterfaceCliente;
import PaqueteInterfaceFrameCliente.EventEstadoConexion;
import PaqueteInterfaceFrameCliente.InterfaceFrameCliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Christian
 */
public class Cliente implements InterfaceCliente {

    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private BufferedReader teclado;
    private String id;
    private String host;
    private int puerto;
    HiloClienteConexion hiloConexion;
    HiloPingCliente hiloPing;
    EventListenerList listenerList = new EventListenerList();
    EventEstadoConexion eec;

    public Cliente(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public void iniciar() {
        this.hiloConexion = new HiloClienteConexion(host, puerto);
        this.hiloConexion.addMyEventListener(this);
        this.hiloConexion.start();
    }

    /* public static void main(String[] args) {
        String host = "localhost";
        int puerto = 5000;
        Cliente cliente = new Cliente(host, puerto);
        cliente.iniciar();
        // cliente.ejecutar();
    }
     */
    @Override
    public void OnConnecting(EventClientConnected evento) {
        System.out.println("Se logró conectar con el servidor");
        Socket socket = evento.getSocket();
        this.hiloPing = new HiloPingCliente(socket);
        this.hiloPing.addMyEventListener(this);
        this.hiloPing.start();
        eec = new EventEstadoConexion(true, this);
        this.notificarEstado(eec);
        this.socket = socket;
        try {
            this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.salida = new PrintWriter(socket.getOutputStream(), true);
            this.teclado = new BufferedReader(new InputStreamReader(System.in));
            HiloEscuchaServidor hes = new HiloEscuchaServidor(entrada);
            hes.start();
        } catch (SocketException e) {
            // Manejo de la excepción SocketException
            System.out.println("Se ha cerrado la conexión del cliente de manera abrupta.");
            // Puedes cerrar los recursos y finalizar el hilo de manera adecuada aquí
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnPing(EventClientPing evento) {
        System.out.println("Se desconectó");

        eec = new EventEstadoConexion(false, this);
        this.notificarEstado(eec);
        this.hiloPing.removeMyEventListener(this);
        this.hiloConexion.removeMyEventListener(this);
        this.hiloConexion = new HiloClienteConexion(host, puerto);
        System.out.println(" iniciando reconeccion");
        this.hiloConexion.addMyEventListener(this);
        this.hiloConexion.start();
    }

    //********************************************************************************
    public void addMyEventListener(InterfaceFrameCliente listener) {
        listenerList.add(InterfaceFrameCliente.class, listener);
    }

    public void removeMyEventListener(InterfaceFrameCliente listener) {
        listenerList.remove(InterfaceFrameCliente.class, listener);
    }

    void notificarEstado(EventEstadoConexion evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceFrameCliente.class) {
                ((InterfaceFrameCliente) listeners[i + 1]).onEstado(evt);
            }
        }
    }

    @Override
    public void OnMessageServer(EventMessageServer evento) {
        //   throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        System.out.println("mensaje: " + evento.getData());
    }
}
