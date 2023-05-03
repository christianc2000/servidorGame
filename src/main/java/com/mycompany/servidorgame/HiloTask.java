/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servidorgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class HiloTask extends Thread {

    ServidorGame SG;

    public HiloTask(ServidorGame SG) {
        this.SG = SG;
    }

    public void preguntar(String id) {

    }

    public void login() {

    }

    public void register(String id, String c, String data, String answer) {
       if(c.equals("1")){
           SG.SS.sendMessageId(id, answer);
       }else{
           
       }
    }
}
