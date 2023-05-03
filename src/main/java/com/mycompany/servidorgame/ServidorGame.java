/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.servidorgame;

import DBPostgresql.CConexion;
import DBPostgresql.DUser;
import PaqueteInterfaceServidorGame.EventUserConnected;
import PaqueteInterfaceServidorGame.EventUserData;
import PaqueteInterfaceServidorGame.InterfaceServidorGame;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Christian
 */
public class ServidorGame implements InterfaceServidorGame {

    ServidorSocket SS;
    private Map<String, User> Usuarios = new HashMap<>();
    //ExecutorService executor = Executors.newSingleThreadExecutor();

    public ServidorGame(int puerto) {
        this.SS = new ServidorSocket(puerto);
        // HiloTask task = new HiloTask();
        //   CConexion objectConexion=new CConexion();
        //   objectConexion.establecerConexion();
        cargarListUser();
        mostrarUserCargados();
        SS.iniciar();
        SS.addMyEventListener(this);
    }
public void mostrarUserCargados(){
    for (Map.Entry<String, User> entry : Usuarios.entrySet()) {
        String key = entry.getKey();
        User val = entry.getValue();
        System.out.println("usuario: "+val.getNombre()); 
    }
}
    public void cargarListUser() {
        HashMap<Integer, String[]> getlistuser = new HashMap<>();
        DUser user = new DUser();
        getlistuser = user.listar();
        
        for (Map.Entry<Integer, String[]> entry : getlistuser.entrySet()) {
            Integer key = entry.getKey();
            String val[] = entry.getValue();
            User u=new User(val[1],val[2],"");
            addUser(val[0], u);
        }
    }

    public void preguntaInicial(String id) {
        String mensaje = "Escriba que quiere hacer:\n1) Register \n2) Login";
        SS.sendMessageId(id, mensaje);
    }

    @Override
    public void onUserConnected(EventUserConnected evento) {
        //System.out.println("SERVIDOR GAME: " + evento.getId());
        preguntaInicial(evento.getId());
    }

    public String buscarUserSesion(String sesion) {
        for (Map.Entry<String, User> entry : Usuarios.entrySet()) {
            String key = entry.getKey();
            User u = entry.getValue();
            if (u.getSesion().equals(sesion)) {
                return key;
            }
        }
        return "";
    }

    @Override
    public void onUserData(EventUserData evento) {
        String data[] = Parsear(evento.getData());
        if (data != null) {
            if (data.length == 2) {
                if (data[1].equals("*1*")) {//Register
                    SS.sendMessageId(data[0], "***************REGISTER***************");
                    SS.sendMessageId(data[0], "NOMBRE:");
                    User user = new User("", "", data[0]);
                    addUser(generarUserId(),user);
                } else if (data[1].equals("*2*")) {//login
                    SS.sendMessageId(data[0], "***************LOGIN***************");
                    SS.sendMessageId(data[0], "NOMBRE:");
                } else {//mensaje normal
                    SS.sendMessageAll(data[0], data[1]);
//                    System.out.println("mensaje normal");
                }
            } else {//id,data,tipo,c
                if (data[2].equals("R")) {
                    if (data[3].equals("1")) {//recibe el nombre
                        User u = Usuarios.get(buscarUserSesion(data[0]));
                        u.setNombre(data[1]);
                        SS.sendMessageId(data[0], "PASSWORD:");
                    } else {//recibe el password
                        String key = buscarUserSesion(data[0]);
                        User u = Usuarios.get(key);
                        u.setPassword(data[1]);
                        DUser usercrud = new DUser();
                        usercrud.insertar(key, u.getNombre(), u.getPassword());
                        SS.sendMessageId(data[0], "USUARIO REGISTRADO EXITOSAMENTE");
                        SS.sendMessageId(data[0], "***************Sesion iniciada***************");
                        System.out.println("usuario<" + u.getNombre() + "," + u.getPassword() + "," + u.getSesion() + ">");
                    }
                } else if (data[2].equals("L")) {
                    if (data[3].equals("1")) {

                    } else {

                    }
                }
            }

        } else {
            System.out.println("ERROR: Formato de mensaje no valido");
        }
    }

    public void addUser(String id,User user) {
        Map<String, Object> atributosCliente = new HashMap<>(); // HashMap para almacenar los atributos del cliente
        Usuarios.put(id, user); // Agregar el cliente al mapa usando el identificador único como clave
    }

    public String generarUserId() {
        return UUID.randomUUID().toString(); // Utilizar la clase UUID para generar un identificador único
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

    public String[] Parsear(String data) {
        data = data.substring(1, data.length() - 1);
        // System.out.println("data: " + data);
        String v[] = data.split(",");
        if (v.length == 2) {
            if (ContieneSintaxis("ID:", v[0]) && ContieneSintaxis("DATA:", v[1])) {
                // System.out.println("mensaje correcto");
                v[0] = v[0].substring(3, v[0].length());
                v[1] = v[1].substring(5, v[1].length());
                //sendMessageAll(id, mensaje);
                return v;
            } else {
                return null;
            }
        } else if (v.length == 4) {
            if (ContieneSintaxis("ID:", v[0]) && ContieneSintaxis("TIPO:", v[2])) {//verifica si es L o R
                v[0] = v[0].substring(3, v[0].length());
                v[1] = v[1].substring(5, v[1].length());
                v[2] = v[2].substring(5, v[2].length());
                v[3] = v[3].substring(2, v[3].length());
                return v;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
