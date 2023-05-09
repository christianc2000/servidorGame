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
import PaqueteInterfaceFrameCliente.EventMessageSG;
import PaqueteInterfaceFrameCliente.EventUserAceppt;
import PaqueteInterfaceFrameCliente.InterfaceFrameCliente;
import PaqueteInterfaceServidorSocket.EventSesion;
import PaqueteInterfaceServidorSocket.InterfaceServidorSocket;
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
    EventUserAceppt eua;
    EventEstadoConexion eec;
    EventMessageSG emsg;

    public Cliente(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public void iniciar() {
        System.out.println("iniciando cliente....");
        this.hiloConexion = new HiloClienteConexion(host, puerto);
        this.hiloConexion.addMyEventListener(this);
        this.hiloConexion.start();
    }

    public void crearTablero(){
        salida.println("<ID:"+this.id+",DATA:CTABLERO"+">");
    }
    
    public void login(String nickname, String password) {
        String msje = "<ID:" + this.id + ",TIPO:L,NICKNAME:" + nickname + ",PASSWORD:" + password + ">";
        salida.println(msje);
    }

    public void registrar(String nickname, String password) {
        String msje = "<ID:" + this.id + ",TIPO:R,NICKNAME:" + nickname + ",PASSWORD:" + password + ">";
        salida.println(msje);
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
        System.out.println("antes del EvenoEstado Conexión");
        eec = new EventEstadoConexion(true, this);
        System.out.println("Después del EvenoEstado Conexión");
        this.notificarEstado(eec);
        this.socket = socket;
        try {
            this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.salida = new PrintWriter(socket.getOutputStream(), true);
            this.teclado = new BufferedReader(new InputStreamReader(System.in));
            HiloEscuchaServidor hes = new HiloEscuchaServidor(entrada);
            hes.addMyEventListener(this);
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

    @Override
    public void OnMessageServer(EventMessageServer evento) {
        //   throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        System.out.println("mensaje: " + evento.getData());
        String data[] = parsear(evento.getData());
        //System.out.println("data.length: " + data.length);
        if (data != null) {
            //System.out.println("data distinto de null");
            if (data.length == 2) {
                String v[]=data[1].split(":");
               if(v[0].equals("USER")){
                   emsg=new EventMessageSG(v[1], this);
                   this.notificarMessageSG(emsg);
               }
                
                if (data[1].equals("I")) {
                    this.id = data[0];
                    System.out.println("Id cliente: " + data[0]);
                } else if (data[1].equals("SG")) {
                    System.out.println("S: " + data[0]);
                } else {

                    if (data[1].equals("acceptL")) {
                        System.out.println("S: Login correcto");
                        eua = new EventUserAceppt("L", true, this);
                        this.notificarOnUserAccept(eua);
                    } else if(data[1].equals("NoacceptL")){
                        eua = new EventUserAceppt("L", false, this);
                        this.notificarOnUserAccept(eua);
                    }
                    if (data[1].equals("acceptR")) {
                        System.out.println("S: Registro correcto");
                        eua = new EventUserAceppt("R", true, this);
                        this.notificarOnUserAccept(eua);
                    } else if(data[1].equals("NoacceptR")){
                        System.out.println("No registra");
                        eua = new EventUserAceppt("R", false, this);
                        this.notificarOnUserAccept(eua);
                    }
                }

            }
        }

    }

    public boolean ContieneSintaxis(String codigo, String data) {
        String cadena = "";
        for (int i = 0; i < codigo.length(); i++) {
            if (codigo.charAt(i) != data.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public String[] parsear(String data) {
        data = data.substring(1, data.length() - 1);
        // System.out.println("data: " + data);
        String v[] = data.split(",");
        switch (v.length) {
            case 2 -> {
                 if (ContieneSintaxis("ID:", v[0]) && (ContieneSintaxis("DATA:", v[1]))) {
                    System.out.println("entra inicial");
                    v[0] = v[0].substring(3, v[0].length());
                    v[1] = v[1].substring(5, v[1].length());
                    return v;
                }
                if (ContieneSintaxis("ID:", v[0]) && (ContieneSintaxis("TIPO:", v[1]))) {
                    System.out.println("entra inicial");
                    v[0] = v[0].substring(3, v[0].length());
                    v[1] = v[1].substring(5, v[1].length());
                    return v;
                }
                if (ContieneSintaxis("MSJ:", v[0]) && (ContieneSintaxis("TIPO:", v[1]))) {
                    System.out.println("entra inicial");
                    v[0] = v[0].substring(4, v[0].length());
                    v[1] = v[1].substring(5, v[1].length());
                    return v;
                }
                if (ContieneSintaxis("ID:", v[0]) && (ContieneSintaxis("LOGIN:", v[1]))) {
                    v[0] = v[0].substring(3, v[0].length());
                    v[1] = v[1].substring(6, v[1].length());
                    return v;
                }
                if (ContieneSintaxis("ID:", v[0]) && (ContieneSintaxis("REGISTRAR:", v[1]))) {
                    v[0] = v[0].substring(3, v[0].length());
                    v[1] = v[1].substring(10, v[1].length());
                    return v;
                }
                return null;
            }
            case 3 -> {
                if (ContieneSintaxis("ID:", v[0]) && ContieneSintaxis("TIPO:", v[1]) && ContieneSintaxis("DATA:", v[2])) {
                    v[0] = v[0].substring(3, v[0].length());
                    v[1] = v[1].substring(5, v[1].length());
                    v[2] = v[2].substring(5, v[2].length());
                    return v;
                } else {
                    return null;
                }
            }
            case 4 -> {
                if (ContieneSintaxis("ID:", v[0]) && ContieneSintaxis("TIPO:", v[1]) && ContieneSintaxis("NICKNAME:", v[2]) && ContieneSintaxis("PASSWORD:", v[3])) {
                    v[0] = v[0].substring(3, v[0].length());
                    v[1] = v[1].substring(5, v[1].length());
                    v[2] = v[2].substring(9, v[2].length());
                    v[3] = v[3].substring(9, v[3].length());
                    return v;
                } else {
                    return null;
                }
            }
            default -> {
                return null;
            }
        }
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

    void notificarOnUserAccept(EventUserAceppt evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceFrameCliente.class) {
                ((InterfaceFrameCliente) listeners[i + 1]).onAcceptUser(evt);
            }
        }
    }
    void notificarMessageSG(EventMessageSG evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == InterfaceFrameCliente.class) {
                ((InterfaceFrameCliente) listeners[i + 1]).onMessageSG(evt);
            }
        }
    }

}
