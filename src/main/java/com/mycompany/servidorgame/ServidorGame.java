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
    private Map<String, Tablero> Tableros = new HashMap<>();
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

    public void mostrarUserCargados() {
        for (Map.Entry<String, User> entry : Usuarios.entrySet()) {
            String key = entry.getKey();
            User val = entry.getValue();
            System.out.println("usuario: " + val.getNickname());
        }
    }

    public void cargarListUser() {
        HashMap<Integer, String[]> getlistuser = new HashMap<>();
        DUser user = new DUser();
        getlistuser = user.listar();

        for (Map.Entry<Integer, String[]> entry : getlistuser.entrySet()) {
            Integer key = entry.getKey();
            String val[] = entry.getValue();
            User u = new User(val[1], val[2], "");
            addUser(val[0], u);
        }
    }

    public void preguntaInicial(String id) {
        String mensaje = "Opciones:,1)Register,2)Login";
        System.out.println(mensaje);
        //System.out.println("antes de enviar el msje");
        SS.sendMessageId(id, mensaje);
        SS.sendMessageId(id, "Ingrese el número de opción:");
        //System.out.println("después de enviar el msje");
    }

    @Override
    public void onUserConnected(EventUserConnected evento) {
        //System.out.println("SERVIDOR GAME: " + evento.getId());
        //preguntaInicial(evento.getId());
        //System.out.println("Nuevo usuario conectado");
        System.out.println("Cliente nuevo con sesión: " + evento.getId());
        SS.sendMessageId(evento.getId(), "<MSJ: Conexión exitosa,TIPO:SG>");

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

    public String findNickName(String nickname) {
        for (Map.Entry<String, User> entry : Usuarios.entrySet()) {
            String key = entry.getKey();
            User val = entry.getValue();
            if (val.getNickname().equals(nickname)) {
                return key;
            }
        }
        return "";
    }

    public boolean existeNickname(String nickname) {
        for (Map.Entry<String, User> entry : Usuarios.entrySet()) {
            Object key = entry.getKey();
            User val = entry.getValue();
            if (val.getNickname().toUpperCase().equals(nickname.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public int cantUserConected(String mysesion) {
        int c = 0;
        for (Map.Entry<String, User> entry : Usuarios.entrySet()) {
            String key = entry.getKey();
            User val = entry.getValue();
            if (val.getSesion().length() > 0) {
                c++;
            }
        }
        return c;
    }

    public void sendCantUserConnected(String sesion) {
        SS.sendMessageId(sesion, "<ID:" + sesion + ",DATA:USER:" + (cantUserConected(sesion) - 1) + ">");
    }

    @Override
    public void onUserData(EventUserData evento) {
        // String data[] = Parsear(evento.getData());
        System.out.println("data: " + evento.getData());
        String data[] = Parsear(evento.getData());
        System.out.println("data SG: " + data.length);
        if (data != null) {
            if (data[1].equals("R")) {
                if (!existeNickname(data[2])) {
                    User u = new User(data[2], data[3], data[0]);
                    System.out.println("nickname: " + data[2] + ", password: " + data[3] + ", sesion: " + data[0]);
                    String idUser = generarUserId();
                    addUser(idUser, u);
                    DUser usercrud = new DUser();
                    usercrud.insertar(idUser, u.getNickname(), u.getPassword());
                    System.out.println("S: Usuario Registrado exitosamente");
                    SS.sendMessageId(u.getSesion(), "<ID:" + u.getSesion() + ",REGISTRAR:acceptR>");
                    sendCantUserConnected(u.getSesion());
                    Tablero t = new Tablero(idUser);
                    Tableros.put(idUser, t);

                } else {
                    System.out.println("nickname ya existe");
                    SS.sendMessageId(data[0], "<ID:" + data[0] + ",REGISTRAR:Noaccept>");
                }
                //System.out.println("se envio un msje al cliente");
            } else if (data[1].equals("L")) {
                System.out.println("entra a login");
                String idUser = findNickName(data[2]);
                System.out.println("idUser: " + idUser.length());
                if (idUser.length() > 0) {
                    User u = Usuarios.get(idUser);
                    //   System.out.println("user: "+u.getNickname());
                    System.out.println("pass u: " + u.getPassword() + "; pass login: " + data[3]);
                    if (u.getPassword().equals(data[3])) {
                        System.out.println("S: login correcto");
                        u.setSesion(data[0]);
                        SS.sendMessageId(u.getSesion(), "<ID:" + u.getSesion() + ",LOGIN:acceptL>");
                        sendCantUserConnected(u.getSesion());
                        //Se crea su tablero
                        Tablero t = new Tablero(idUser);
                        Tableros.put(idUser, t);
                    } else {
                        System.out.println("login incorrecto, contraseña incorrecta");
                        SS.sendMessageId(data[0], "<ID:" + data[0] + ",LOGIN:NoacceptL>");
                    }

                } else {
                    System.out.println("login incorrecto" + data[0]);
                    SS.sendMessageId(data[0], "<ID:" + data[0] + ",LOGIN:NoacceptL>");
                }
            } else if (data[1].equals("M")) {
                SS.sendMessageAll(data[0], data[2]);
            } else {
                System.out.println("ERROR: Formato no válido");
            }

        }
    }

    public void addUser(String id, User user) {
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
        switch (v.length) {

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
}
