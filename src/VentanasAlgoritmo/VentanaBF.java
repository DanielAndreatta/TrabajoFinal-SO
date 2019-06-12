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

/**
 *
 * @author familia
 */
public class VentanaBF extends javax.swing.JFrame {

    
    LinkedList<BloqueMemoria> procesos_en_memoria = new LinkedList();
    LinkedList<BloqueMemoria> memoria = new LinkedList();
    LinkedList<BloqueMemoria> dispo = new LinkedList(); 
    BloqueMemoria dispo_cabecera = new BloqueMemoria();
    private int tam_memoria; 
    private int tam_memoria_disponible;
    /**
     * Creates new form VentanaBF
     */
    public VentanaBF(int tamaño) {
        initComponents();
        this.tam_memoria = tamaño;
        this.tam_memoria_disponible = this.tam_memoria;
        this.jLabel9.setText("Tamaño de memoria: " + tamaño + "k");
        configurar_memoria();
        configurar_dispo();
        mostrar_estado_memoria();
        mostrar_dispo();
        agregar_evento_mouse();
        setLocationRelativeTo(null);
    }

    public void configurar_memoria() {  // LISTO
        
        BloqueMemoria bl = new BloqueMemoria();
        bl.setMar(0);
        bl.setPa(9999);
        bl.setPs(9999);
        bl.setDirInicio(0);
        bl.setProceso(null);
        bl.setTamaño(this.tam_memoria);

        this.memoria.addFirst(bl);
    }
    
    public void configurar_dispo() {  // LISTO
        
        BloqueMemoria bl = new BloqueMemoria();
        bl.setMar(0);
        bl.setPa(9999);
        bl.setPs(9999);
        bl.setDirInicio(0);
        bl.setProceso(null);
        bl.setTamaño(this.tam_memoria);

        this.dispo.addFirst(bl);
    }
    
    public void buscar_espacio_libre(String nombre_pro, int tam_pro) {
        
        int aux=9999,resta,pos=0;
        for (int i = 0; i < this.dispo.size(); i++) {
          if (((BloqueMemoria)this.dispo.get(i)).getTamaño() == tam_pro) {

            asignar_bloque_entero_proceso(((BloqueMemoria)this.dispo.get(i)).getDirInicio(), nombre_pro, tam_pro);
            ordenar_memoria();
            mostrar_estado_memoria();

            actualizar_dispo(((BloqueMemoria)this.dispo.get(i)).getDirInicio(), tam_pro);
            mostrar_dispo();

            mostrar_lista_procesos();
            return;
          }  
       } 
       for(int i=0;i < this.dispo.size();i++)
       {
           if (((BloqueMemoria)this.dispo.get(i)).getTamaño() > tam_pro) 
           {
               resta=((BloqueMemoria)this.dispo.get(i)).getTamaño()-tam_pro;
               if(resta<aux)
               {
                   aux=resta;
                   pos=i;
               }
          }
       }
       if(aux!=9999)
       {
            asignar_bloque_parte(((BloqueMemoria)this.dispo.get(pos)).getDirInicio(), nombre_pro, tam_pro);
            ordenar_memoria();
            mostrar_estado_memoria();

            actualizar_dispo(((BloqueMemoria)this.dispo.get(pos)).getDirInicio(), tam_pro);
            mostrar_dispo();

            mostrar_lista_procesos();
            return;
       }
       JOptionPane.showMessageDialog(this, "No hay suficiente espacio en la memoria para asignar el proceso");
    }
    
    public void asignar_bloque_entero_proceso(int posicion, String nombre_pro, int tam_pro) {  //LISTO
        
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
            data[0] = " LIBRE | MAR " + bl.getMar() + " | TAM " + bl.getTamaño() + "k";
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
          blo.setPs(9999);
          blo.setDirInicio(posicion);
          blo.setProceso(null);
          blo.setTamaño(tam);
          try {
            blo.setPa(((BloqueMemoria)this.dispo.getLast()).getDirInicio());
            ((BloqueMemoria)this.dispo.getLast()).setPs(posicion);
          }
          catch (Exception ex) {
            blo.setPa(9999);
          } 
          this.dispo.add(blo);
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
    
    public void agregar_evento_mouse() {
        this.jTable2.addMouseListener(new MouseAdapter() {
              public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {

                  String proceso = VentanaBF.this.jTable2.getValueAt(VentanaBF.this.jTable2.getSelectedRow(), 0).toString();
                  VentanaBF.this.finalizar_proceso(VentanaBF.this.buscar_proceso(proceso));
                } 
              }
            });
     }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("ASIGNAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("LIBERAR PROCESO");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setText("ESTADO MEMORIA");

        jLabel9.setText("tamaño");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "0"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setRowHeight(55);
        jScrollPane1.setViewportView(jTable1);

        jLabel8.setText("DISPO");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Title 1"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setRowHeight(45);
        jScrollPane2.setViewportView(jTable4);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Title 1"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable3.setColumnSelectionAllowed(true);
        jTable3.setRowHeight(45);
        jScrollPane3.setViewportView(jTable3);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("NUEVO PROCESO");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("  ALGORITMO: BEST-FIT");

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

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Nombre", "Tamaño"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable2MousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(jTable2);

        jLabel10.setText("No hay procesos en memoria");
        jLabel10.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addGap(10, 10, 10)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(97, 97, 97)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(282, 282, 282)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 687, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addComponent(jButton1)
                        .addGap(46, 46, 46))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(44, 44, 44)))
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        buscar_espacio_libre(this.jTextField1.getText(), Integer.parseInt(this.jTextField2.getText()));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String proceso = this.jLabel10.getText();
        finalizar_proceso(buscar_proceso(proceso));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTable2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MousePressed
        String nombre_proc=this.jTable2.getValueAt(this.jTable2.getSelectedRow(),0).toString();
        this.jLabel10.setText(nombre_proc);
    }//GEN-LAST:event_jTable2MousePressed

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
            java.util.logging.Logger.getLogger(VentanaBF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaBF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaBF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaBF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new VentanaBF().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
