/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;


public class Proceso {
    
    
    
    private String nombre;
    private Integer tamaño;

    
    public Proceso(String nombre, Integer tamaño){
        this.nombre=nombre;
        this.tamaño=tamaño;
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTamaño() {
        return tamaño;
    }

    public void setTamaño(Integer tamaño) {
        this.tamaño = tamaño;
    }
 


    
}
