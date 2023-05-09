/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PaqueteInterfaceFrameCliente;

import java.util.EventListener;

/**
 *
 * @author Christian
 */
public interface InterfaceFrameCliente extends EventListener{
    void onEstado(EventEstadoConexion evento);
}
