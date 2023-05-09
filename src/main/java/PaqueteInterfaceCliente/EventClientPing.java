/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaqueteInterfaceCliente;

import java.util.EventObject;

/**
 *
 * @author Christian
 */
public class EventClientPing extends EventObject{
    boolean conectado;

    public EventClientPing(boolean conectado, Object source) {
        super(source);
        this.conectado = conectado;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
    
    
    
    
}
