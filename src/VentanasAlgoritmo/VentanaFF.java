/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VentanasAlgoritmo;

import java.util.LinkedList;
import clases.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;


public class VentanaFF extends javax.swing.JFrame {

    LinkedList<BloqueMemoria> procesos_en_memoria = new LinkedList();
    LinkedList<BloqueMemoria> memoria = new LinkedList();
    LinkedList<BloqueMemoria> dispo = new LinkedList(); 
    BloqueMemoria dispo_cabecera = new BloqueMemoria();
    private int tam_memoria; 
    private int tam_memoria_disponible;
    
    
    public VentanaFF(int tamaño) {
        initComponents();
         
        this.tam_memoria = tamaño;
        this.tam_memoria_disponible = this.tam_memoria;
        //this.tam_memoria_label.setText("Tama�o de memoria: " + tam_mem + "k");
        //configurar_memoria();
        //configurar_dispo();
        //mostrar_estado_memoria();
        //mostrar_dispo();
        //agregar_evento_mouse();
        setLocationRelativeTo(null);
    }
    
    public void configurar_memoria() {
        
        BloqueMemoria bl = new BloqueMemoria();
        bl.setMar(0);
        bl.setPa(9999);
        bl.setPs(9999);
        bl.setDirInicio(0);
        bl.setProceso(null);
        bl.setTamaño(this.tam_memoria);

        this.memoria.addFirst(bl);
    }
    
    public void configurar_dispo() {
        
        BloqueMemoria bl = new BloqueMemoria();
        bl.setMar(0);
        bl.setPa(999);
        bl.setPs(999);
        bl.setDirInicio(0);
        bl.setProceso(null);
        bl.setTamaño(this.tam_memoria);

        this.dispo.addFirst(bl);
    }
    
    public void buscar_espacio_libre(String nombre_pro, int tam_pro) {
        
        for (int i = 0; i < this.dispo.size(); i++) {
          if (((BloqueMemoria)this.dispo.get(i)).getTamaño() == tam_pro) {

            asignar_bloque_entero_proceso(((BloqueMemoria)this.dispo.get(i)).getDirInicio(), nombre_pro, tam_pro);
            ordenar_memoria();
            mostrar_estado_memoria();

            actualizar_dispo(((BloqueMemoria)this.dispo.get(i)).getDirInicio(), tam_pro);
            mostrar_dispo();

            mostrar_lista_procesos();
          } else if (((BloqueMemoria)this.dispo.get(i)).getTamaño() > tam_pro) {
            asignar_bloque_parte(((BloqueMemoria)this.dispo.get(i)).getDirInicio(), nombre_pro, tam_pro);
            ordenar_memoria();
            mostrar_estado_memoria();

            actualizar_dispo(((BloqueMemoria)this.dispo.get(i)).getDirInicio(), tam_pro);
            mostrar_dispo();

            mostrar_lista_procesos();
          } else {
            JOptionPane.showMessageDialog(this, "No hay suficiente espacio en la memoria para asignar el proceso");
          } 
       } 
    }
    
    public void asignar_bloque_entero_proceso(int posicion, String nombre_pro, int tam_pro) {
        
        for (int i = 0; i < this.memoria.size(); i++) {
          if (((BloqueMemoria)this.memoria.get(i)).getDirInicio() == posicion) {
            Proceso pro = new Proceso(nombre_pro, tam_pro);
            ((BloqueMemoria)this.memoria.get(i)).setProceso(pro);
            ((BloqueMemoria)this.memoria.get(i)).setMar(1);
            this.procesos_en_memoria.addFirst(this.memoria.get(i));
          } 
        } 
    }

    
    public void asignar_bloque_parte(int posicion, String id_pro, int tam_pro) {
        for (int i = 0; i < this.memoria.size(); i++) {
          if (((BloqueMemoria)this.memoria.get(i)).getDirInicio() == posicion) {

            ((BloqueMemoria)this.memoria.get(i)).setTamaño(((BloqueMemoria)this.memoria.get(i)).getTamaño() - tam_pro);

            Proceso pro = new Proceso(id_pro, tam_pro);

            BloqueMemoria bl = new BloqueMemoria();
            bl.setMar(1);
            bl.setDirInicio(((BloqueMemoria)this.memoria.get(i)).getDirInicio() + ((BloqueMemoria)this.memoria.get(i)).getTamaño());
            bl.setTamaño(tam_pro);
            bl.setProceso(pro);

            this.memoria.add(bl);
            this.procesos_en_memoria.addFirst(bl);
          } 
        } 
      }

    public void mostrar_estado_memoria() {
        DefaultTableModel dtm = (DefaultTableModel)this.jTable1.getModel();
        dtm.setColumnCount(0);

        Object[] data = new Object[this.memoria.size()];

        for (BloqueMemoria bl : this.memoria) {


          if (bl.getProceso() == null) {
            data[0] = " LIBRE | MAR " + bl.getMar() + " | TAM " + bl.getTam() + "k";
          } else {
            data[0] = bl.getProceso().getNombre() + " | MAR " + bl.getMar() + " | TAM " + bl.getTamaño() + "k";
          } 
          dtm.addColumn(Integer.valueOf(bl.getDirInicio()), data);
        } 
        dtm.setRowCount(1);
        TableColumnModel columnModel = this.jTable1.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
          columnModel.getColumn(i).setPreferredWidth(250);
        }

        pintar_bloque_proceso();
      }

    public void ordenar_memoria() {
        Collections.sort(this.memoria, new Comparator()
            {
              public int compare(Object o1, Object o2) {
                BloqueMemoria bl1 = (BloqueMemoria)o1;
                BloqueMemoria bl2 = (BloqueMemoria)o2;
                return (new Integer(bl1.getDirInicio())).compareTo(new Integer(bl2.getDirInicio()));
              }
            });
      }

    public void actualizar_dispo(int posicion, int tam_pro) {
        for (int i = 0; i < this.dispo.size(); i++) {
          if (((BloqueMemoria)this.dispo.get(i)).getDirInicio() == posicion)
          {

            if (((BloqueMemoria)this.dispo.get(i)).getTamaño() == tam_pro) {




              if (this.dispo.size() == 1) {
                this.dispo_cabecera.setMar(0);
                this.dispo_cabecera.setPa(9999);
                this.dispo_cabecera.setPs(9999);
                this.dispo.remove(i);
              } else if (i == 0) {
                this.dispo_cabecera.setPs(((BloqueMemoria)this.dispo.get(i + 1)).getDirInicio());
                ((BloqueMemoria)this.dispo.get(i + 1)).setPa(9999);
                this.dispo.remove(i);
              }
              else if (i == this.dispo.size() - 1) {
                this.dispo_cabecera.setPa(((BloqueMemoria)this.dispo.get(i)).getPa());
                ((BloqueMemoria)this.dispo.get(i - 1)).setPs(9999);
              } else {
                ((BloqueMemoria)this.dispo.get(i - 1)).setPs(((BloqueMemoria)this.dispo.get(i)).getPs());
                ((BloqueMemoria)this.dispo.get(i + 1)).setPa(((BloqueMemoria)this.dispo.get(i)).getPa());
              } 
            } else {

              ((BloqueMemoria)this.dispo.get(i)).setTamaño(((BloqueMemoria)this.dispo.get(i)).getTamaño() - tam_pro);
            } 
          }
        } 
      }




    public void mostrar_dispo() {
        DefaultTableModel dtm_cabecera = (DefaultTableModel)this.jTable4.getModel();
        dtm_cabecera.setColumnCount(0);
        dtm_cabecera.setRowCount(0);
        Object[] data_cab = new Object[3];

        data_cab[0] = "PA " + this.dispo_cabecera.getPa() + "  | MAR " + this.dispo_cabecera.getMar() + " | PS " + this.dispo_cabecera.getPs();

        dtm_cabecera.addColumn("9999", data_cab);



        DefaultTableModel dtm = (DefaultTableModel)this.jTable3.getModel();
        dtm.setColumnCount(0);
        dtm.setRowCount(0);
        Object[] data = new Object[this.dispo.size()];

        for (BloqueMemoria bl : this.dispo) {


          data[0] = "PA " + bl.getPa() + "  | MAR " + bl.getMar() + " | TAM " + bl.getTamaño() + "k | PS " + bl.getPs();

          dtm.addColumn(Integer.valueOf(bl.getDirInicio()), data);
        } 

        TableColumnModel columnModel = this.jTable3.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
          columnModel.getColumn(i).setPreferredWidth(300);
        }
    }
  
    public void mostrar_lista_procesos() {
      
      DefaultTableModel dtm = (DefaultTableModel)this.jTable2.getModel();
      dtm.setRowCount(0);

      Object[] data = new Object[2];

      for (BloqueMemoria bl : this.procesos_en_memoria) {
        data[0] = bl.getProceso().getNombre();
        data[1] = bl.getProceso().getTamaño() + "k";
        dtm.addRow(data);
      } 
    }
    
    public void agregar_evento_mouse() {
        this.jTable2.addMouseListener(new MouseAdapter() {
              public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {

                  String proceso = First_Fit.this.jTable2.getValueAt(First_Fit.this.jTable2.getSelectedRow(), 0).toString();
                  First_Fit.this.finalizar_proceso(First_Fit.this.buscar_proceso(proceso));
                } 
              }
            });
     }





    public BloqueMemoria buscar_proceso(String proceso) {
        for (int i = 0; i < this.procesos_en_memoria.size(); i++) {
          if (((BloqueMemoria)this.procesos_en_memoria.get(i)).getProceso().getNombre().equals(proceso))
            return (BloqueMemoria)this.procesos_en_memoria.get(i); 
        } 
        return null;
     }


    public void finalizar_proceso(BloqueMemoria bloque) {
        for (int i = 0; i < this.memoria.size(); i++) {
          if (((BloqueMemoria)this.memoria.get(i)).getProceso() != null)
          {
            if (((BloqueMemoria)this.memoria.get(i)).getProceso().getNombre().equals(bloque.getProceso().getNombre())) {

              actualizar_lista_procesos(bloque);
              ((BloqueMemoria)this.memoria.get(i)).setProceso(null);
              ((BloqueMemoria)this.memoria.get(i)).setMar(0);



              compactar_dispo(((BloqueMemoria)this.memoria.get(i)).getDirInicio(), ((BloqueMemoria)this.memoria.get(i)).getTamaño());
              compactar_memoria();

              mostrar_estado_memoria();
              mostrar_dispo();
              mostrar_lista_procesos();
              JOptionPane.showMessageDialog(this, "Proceso a finalizado");
              if (this.procesos_en_memoria.size() != 0) {
                this.jLabel10.setText("Seleccione 1 proceso"); break;
              } 
              this.jLabel10.setText("no hay procesos en memoria");
              break;
            } 
          }
        } 
      }


    public void compactar_memoria() {
        for (int i = 0; i < this.memoria.size() - 1; i++) {
          if (((BloqueMemoria)this.memoria.get(i)).getMar() == 0 && ((BloqueMemoria)this.memoria.get(i + 1)).getMar() == 0) {
            ((BloqueMemoria)this.memoria.get(i)).setTamaño(((BloqueMemoria)this.memoria.get(i)).getTamaño() + ((BloqueMemoria)this.memoria.get(i + 1)).getTamaño());
            this.memoria.remove(i + 1);
            i--;
          } 
        } 
    }






    public void compactar_dispo(int posicion, int tam) {
        boolean compactado = false;


        for (int i = 0; i < this.dispo.size(); i++) {

          int pos_hno_izq = posicion - ((BloqueMemoria)this.dispo.get(i)).getTamaño();

          if (pos_hno_izq == ((BloqueMemoria)this.dispo.get(i)).getDirInicio()) {

            if (!compactado) {
              ((BloqueMemoria)this.dispo.get(i)).setTamaño(((BloqueMemoria)this.dispo.get(i)).getTamaño() + tam);


              tam = ((BloqueMemoria)this.dispo.get(i)).getTamaño();
              posicion = ((BloqueMemoria)this.dispo.get(i)).getDirInicio();
              compactado = true;
              i--;
            }
            else {

              ((BloqueMemoria)this.dispo.get(i)).setTamaño(((BloqueMemoria)this.dispo.get(i)).getTamaño() + tam);

              tam = ((BloqueMemoria)this.dispo.get(i)).getTamaño();
              posicion = ((BloqueMemoria)this.dispo.get(i)).getDirInicio();
              compactado = true;
              this.dispo.remove(i - 1);
              i--;
              try {
                ((BloqueMemoria)this.dispo.get(i)).setPa(((BloqueMemoria)this.dispo.get(i - 1)).getDirInicio());
              } catch (Exception ex) {
                ((BloqueMemoria)this.dispo.get(i)).setPa(9999);
              } 
              try {
                ((BloqueMemoria)this.dispo.get(i)).setPs(((BloqueMemoria)this.dispo.get(i + 1)).getDirInicio());
              } catch (Exception ex) {
                ((BloqueMemoria)this.dispo.get(i)).setPs(9999);

              }


            }

          }
          else {

            int pos_hno_der = posicion + tam;

            if (pos_hno_der == ((BloqueMemoria)this.dispo.get(i)).getDirInicio()) {
              if (!compactado) {

                ((BloqueMemoria)this.dispo.get(i)).setTamaño(((BloqueMemoria)this.dispo.get(i)).getTamaño() + tam);
                ((BloqueMemoria)this.dispo.get(i)).setDirInicio(posicion);
                try {
                  ((BloqueMemoria)this.dispo.get(i - 1)).setPs(posicion);
                } catch (Exception ex) {
                  this.dispo_cabecera.setPs(posicion);
                  ((BloqueMemoria)this.dispo.get(i)).setPa(9999);
                } 
                tam = ((BloqueMemoria)this.dispo.get(i)).getTamaño();
                posicion = ((BloqueMemoria)this.dispo.get(i)).getDirInicio();


                compactado = true;
                i--;
              } else {

                ((BloqueMemoria)this.dispo.get(i)).setTamaño(((BloqueMemoria)this.dispo.get(i)).getTamaño() + tam);
                ((BloqueMemoria)this.dispo.get(i)).setDirInicio(posicion);
                try {
                  ((BloqueMemoria)this.dispo.get(i - 1)).setPs(posicion);
                } catch (Exception ex) {
                  this.dispo_cabecera.setPs(posicion);
                  ((BloqueMemoria)this.dispo.get(i)).setPa(9999);
                } 
                tam = ((BloqueMemoria)this.dispo.get(i)).getTamaño();
                posicion = ((BloqueMemoria)this.dispo.get(i)).getDirInicio();


                compactado = true;
                this.dispo.remove(i + 1);
              } 
            }
          } 
        } 
        if (!compactado) {
          BloqueMemoria blo = new BloqueMemoria();
          blo.setMar(0);
          blo.setPa(9999);
          blo.setDirInicio(posicion);
          blo.setProceso(null);
          blo.setTamaño(tam);
          try {
            blo.setPs(((BloqueMemoria)this.dispo.getFirst()).getDirInicio());
            ((BloqueMemoria)this.dispo.getFirst()).setPa(posicion);
          }
          catch (Exception ex) {
            blo.setPs(9999);
          } 
          this.dispo.addFirst(blo);
        } 

        if (this.dispo.size() != 0) {
          this.dispo_cabecera.setPs(((BloqueMemoria)this.dispo.getFirst()).getDirInicio());
          this.dispo_cabecera.setPa(((BloqueMemoria)this.dispo.getLast()).getDirInicio());
          this.dispo_cabecera.setMar(1);
        } else if (this.dispo.size() == 0) {
          this.dispo_cabecera.setPs(9999);
          this.dispo_cabecera.setPa(9999);
          this.dispo_cabecera.setMar(0);
        } 
    }



    public void actualizar_lista_procesos(BloqueMemoria bloque) {
        for (int i = 0; i < this.procesos_en_memoria.size(); i++) {
          if (((BloqueMemoria)this.procesos_en_memoria.get(i)).getProceso().getNombre().equals(bloque.getProceso().getNombre())) {
            this.procesos_en_memoria.remove(i);
            break;
          } 
        } 
    }



    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        scrollPane1 = new java.awt.ScrollPane();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("NUEVO PROCESO");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("  ALGORITMO: FIRST-FIT");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("METODO ETIQUETAS LIMITE");

        jLabel4.setText("Proceso:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Tamaño?:");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel6.setText("LISTA PROCESOS MEMORIA");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(292, 292, 292)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(207, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(283, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaFF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaFF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaFF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaFF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new VentanaFF().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private java.awt.ScrollPane scrollPane1;
    // End of variables declaration//GEN-END:variables
}
