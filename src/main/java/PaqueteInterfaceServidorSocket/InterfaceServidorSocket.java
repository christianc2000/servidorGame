/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PaqueteInterfaceServidorSocket;

import java.util.EventListener;

/**
 *
 * @author Christian
 */
public interface InterfaceServidorSocket  extends EventListener{
      void onSesionCliente(EventSesion evento);
       void onMessageCliente(EventMensaje evento);
}
