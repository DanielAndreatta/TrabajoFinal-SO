/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabajofinal.so;

import java.util.ArrayList;

/**
 *
 * @author Dany
 */
public class GestionadorMemoria {
    
    ArrayList <Memoria> estado;
    
    ArrayList <Memoria> dispo;
    
    GestionadorMemoria(Memoria memoria){
        Memoria disp = new Memoria(0,"DISPO");
        dispo.add(disp);
        dispo.add(memoria);
        
        estado.add(memoria);
    }
    
    public void agregarBF(Memoria memoria){
        
        
    
    }
    
    public void agregarFF(Memoria memoria){
        
        //Primero entramos en array dispo a posicion 1,y empezamos a comparar valores de tamaño, 
        //cuando encontremos el primero que sea mayor o igual a lo 
        //requerido lo tomamos, hacemos calculo y hacemos una segunda pasada para actualizar los ps y pa 
        //si no hay espacio se avisa y no se hace nada
        // si el espacio es igual se elimina el bloque de la dispo y se hace cambios
        
        //Luego de hacer cambios en dispo pasamos a estado y agreagamos nuevo proceso, la 
        // dir de inicio se calcula con la resta de tamaños
    
    }
    
}
