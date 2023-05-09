/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servidorgame;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Christian
 */
public class Tablero {

    String userId;

    int dimension;
    int tablero[][];
    //0: celda sin nada
    //1: celda con ocupada
    //2: celda con barco destruida

    public Tablero(String userId) {
        this.dimension = 8;
        this.tablero = new int[this.dimension][this.dimension];
        this.userId = this.userId;
        llenarCeros();
    }

    public int getDimension() {
        return this.dimension;
    }

    public void llenarCeros() {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = 0;
            }
        }
    }

    public void colocarBarco(String pos) {
        String columnas = "ABCDEFGH";
        int c = columnas.indexOf(pos.charAt(0));
        int f = (int) (pos.charAt(1));
        if (tablero[f][c] != 1) {
            System.out.println("Barco colocado en la celda[" + f + "][" + c + "]");
            tablero[f][c] = 1;
        }
    }

    public void pintarTableroFrame(JTable tabla) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int val = (int) value;
                if (val == 1) {
                    c.setBackground(Color.RED);
                } else if (val == 2) {
                    c.setBackground(Color.BLUE);
                } else {
                    c.setBackground(table.getBackground());
                }
                return c;
            }
        });

        DefaultTableModel model = new DefaultTableModel(tablero.length, tablero[0].length);
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                model.setValueAt(tablero[i][j], i, j);
            }
        }
        tabla.setModel(model);

        //return tabla;
    }

    public void verificarDisparoRival(String pos) {
        String columnas = "ABCDEFGH";
        int c = columnas.indexOf(pos.charAt(0));
        int f = (int) (pos.charAt(1));
        if (tablero[f][c] == 1) {
            System.out.println("Barco destruido");
            tablero[f][c] = 2;
        }
    }
}
