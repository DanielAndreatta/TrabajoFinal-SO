/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabajofinal.so;

/**
 *
 * @author Dany
 */
public class Memoria {
    
    
    
    private String nombre;
    private Integer tamaño;
    private Integer mar;
    private Integer ps;
    private Integer pa;
    private Integer dirInicio;
    
    Memoria(Integer mar, String nombre){ //prime bloque dispo
        this.nombre=nombre;
        this.mar=mar;
        ps=0;
        pa=0;
        dirInicio=9999;
    }

    Memoria(Integer tamaño){ //ventana pide tamaño
        nombre="LIBRE";
        this.tamaño=tamaño;
        mar=0;
        ps=0;
        pa=0;
        dirInicio=0;
    }
    
    Memoria(String nombre, Integer tamaño){ //procesos ventana chica
        this.nombre=nombre;
        this.tamaño=tamaño;
        mar=1;
        ps=0;
        pa=0;
        dirInicio=0;
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

    public Integer getMar() {
        return mar;
    }

    public void setMar(Integer mar) {
        this.mar = mar;
    }

    public Integer getPs() {
        return ps;
    }

    public void setPs(Integer ps) {
        this.ps = ps;
    }

    public Integer getPa() {
        return pa;
    }

    public void setPa(Integer pa) {
        this.pa = pa;
    }

    public Integer getDirInicio() {
        return dirInicio;
    }

    public void setDirInicio(Integer dirInicio) {
        this.dirInicio = dirInicio;
    }
    
}
