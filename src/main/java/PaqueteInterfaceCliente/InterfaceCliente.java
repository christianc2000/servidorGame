/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PaqueteInterfaceCliente;

import java.util.EventListener;

/**
 *
 * @author Christian
 */
public interface InterfaceCliente extends EventListener{
    void OnConnecting(EventClientConnected evento);
    void OnPing(EventClientPing evento);
    void OnMessageServer(EventMessageServer evento);
}
