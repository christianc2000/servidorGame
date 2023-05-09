/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBPostgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 *
 * @author Christian
 */
public class DUser extends CConexion {

    java.sql.Statement st;
    ResultSet rs;

    public void insertar(String id, String nickname, String password) {
        try {
            Connection connection = establecerConexion();
            st = connection.createStatement();
            String sql = "INSERT INTO users(user_id,nickname,password) values('" + id + "','" + nickname + "','" + password + "');";
            st.execute(sql);
            st.close();
            connection.close();
            System.out.println("Se registro un nuevo usuario a la BD");
        } catch (Exception e) {
            System.out.println("Error al conectar a la Base de datos: " + e.toString());
        }
    }

    public String[] mostrar(String id) {
        String[] usuario = new String[3];
        try {
            Connection connection = establecerConexion();
            st = connection.createStatement();
            String sql = "SELECT * FROM users where user_id='" + id + "';";
            rs = st.executeQuery(sql);
            int i = 1;
            if (rs.next()) {
                usuario[0] = rs.getString("user_id");
                usuario[1] = rs.getString("nickname");
                usuario[2] = rs.getString("password");
            }
            st.close();
            connection.close();
            // System.out.println("EL REGISTRO SE REALIZO EXITOSAMENTE");
        } catch (Exception e) {
            System.out.println("Error al conectar a la Base de datos: " + e.toString());
        }
        return usuario;
    }

    public HashMap<Integer, String[]> listar() {
        HashMap<Integer, String[]> usuarios = new HashMap<>();
        try {
            Connection connection = establecerConexion();
            st = connection.createStatement();
            String sql = "SELECT * FROM users;";
            rs = st.executeQuery(sql);
            int i = 1;
            while (rs.next()) {
                String[] usuario = new String[3];
                usuario[0] = rs.getString("user_id");
                usuario[1] = rs.getString("nickname");
                usuario[2] = rs.getString("password");
                usuarios.put(i, usuario);
                i++;
            }
            st.close();
            connection.close();

            // System.out.println("EL REGISTRO SE REALIZO EXITOSAMENTE");
        } catch (Exception e) {
            System.out.println("Error al conectar a la Base de datos: " + e.toString());
        }
        return usuarios;
    }
}
