/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servidorgame;

/**
 *
 * @author Christian
 */
public class User {

    private String nombre;
    private String password;
    public String sesion;

    public User(String nombre, String password, String sesion) {
        this.nombre = nombre;
        this.password = password;
        this.sesion = sesion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSesion() {
        return sesion;
    }

    public void seSesion(String sesion) {
        this.sesion = sesion;
    }

}
