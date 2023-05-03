/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PaqueteInterfaceServidorGame;

import java.util.EventListener;

/**
 *
 * @author Christian
 */
public interface InterfaceServidorGame  extends EventListener{
    void onUserConnected(EventUserConnected evento);
    void onUserData(EventUserData evento);
}
