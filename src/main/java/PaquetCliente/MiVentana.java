/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PaquetCliente;

/**
 *
 * @author Christian
 */
import javax.swing.*;
import java.awt.*;

public class MiVentana extends JFrame {

    public MiVentana() {
        // Configuración básica del JFrame
        setTitle("Mi Ventana con JTable");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear la tabla y el JScrollPane
        JTable tabla = new JTable(8, 8);
        tabla.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        // Agregar el JScrollPane al panel principal del JFrame
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Establecer el tamaño y la ubicación del JFrame
        setSize(600, 400);
        setLocationRelativeTo(null);
    }
}
