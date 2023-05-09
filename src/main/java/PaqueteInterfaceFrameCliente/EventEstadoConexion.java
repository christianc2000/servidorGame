/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaqueteInterfaceFrameCliente;

import java.util.EventObject;

/**
 *
 * @author Christian
 */
public class EventEstadoConexion extends EventObject{
    boolean estado;

    public EventEstadoConexion(boolean estado, Object source) {
        super(source);
        this.estado = estado;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
    
}
