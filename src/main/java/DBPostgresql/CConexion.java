/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBPostgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian
 */
public class CConexion {
  Connection conectar=null;
    String usuario="postgres";
    String contrasenia="9821736";
    String bd="javadb";
    String ip="localhost";
    String puerto="5432";
    
    String cadena="jdbc:postgresql://"+ip+":"+puerto+"/"+bd;
    
    public Connection establecerConexion(){
        try {
            Class.forName("org.postgresql.Driver");
            conectar=DriverManager.getConnection(cadena,usuario,contrasenia);
            System.out.println("Se estableció la conexión a la base de datos correctamente");
        } catch (Exception e) {
            System.out.println("Error al conectar a la Base de datos: "+e.toString());
        }
        return conectar;
    }
}
