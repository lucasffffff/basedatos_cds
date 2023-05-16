package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.Vector;
import basedatos_cds.Basedatos_cds;

public class CDManagerFrame extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldNombre;
    private JTextField textFieldGenero;
    private JTextField textFieldArtista;
    private JTextField textFieldEstante;
    private JTextField textFieldPosicion;
    private JTable table;
    private DefaultTableModel tableModel;
    private Basedatos_cds basedatos;
    private JButton btnConectar;

    public CDManagerFrame(Basedatos_cds basedatos) {
        this.basedatos = basedatos;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(35, 30, 500, 200);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Género");
        tableModel.addColumn("Artista");
        tableModel.addColumn("Estante");
        tableModel.addColumn("Posición");

        table.setModel(tableModel);

        JButton btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = textFieldNombre.getText();
                String genero = textFieldGenero.getText();
                String artista = textFieldArtista.getText();
                int estante = Integer.parseInt(textFieldEstante.getText());
                int posicion = Integer.parseInt(textFieldPosicion.getText());

                basedatos.insertarCD(nombre, genero, artista, estante, posicion);
                cargarCDs();
            }
        });
        btnInsertar.setBounds(35, 250, 117, 29);
        contentPane.add(btnInsertar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = textFieldNombre.getText();

                basedatos.eliminarCD(nombre);
                cargarCDs();
            }
        });
        btnEliminar.setBounds(180, 250, 117, 29);
        contentPane.add(btnEliminar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreDisco = JOptionPane.showInputDialog("Ingrese el nombre del disco a editar:");

                if (nombreDisco != null && !nombreDisco.isEmpty()) {
                    String genero = textFieldGenero.getText();
                    String artista = textFieldArtista.getText();
                    int estante, posicion;

                    if (textFieldEstante.getText().isEmpty() || textFieldPosicion.getText().isEmpty()) {
                        System.out.println("Los campos de estante y posición no pueden estar vacíos.");
                        return;
                    }

                    try {
                        estante = Integer.parseInt(textFieldEstante.getText());
                        posicion = Integer.parseInt(textFieldPosicion.getText());
                    } catch (NumberFormatException ex) {
                        System.out.println("Los campos de estante y posición deben ser números enteros.");
                        return;
                    }

                    basedatos.editarCD(nombreDisco, genero, artista, estante, posicion);
                    cargarCDs();
                }
            }
        });
        btnEditar.setBounds(325, 250, 117, 29);
        contentPane.add(btnEditar);

        btnConectar = new JButton("Conectar");
        btnConectar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (basedatos.estaConectado()) {
                    basedatos.cerrarConexion();
                    btnConectar.setText("Conectar");
                } else {
                    basedatos.establecerConexion();
                    btnConectar.setText("Desconectar");
                }
            }
        });
        btnConectar.setBounds(470, 250, 117, 29);
        contentPane.add(btnConectar);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(35, 300, 70, 16);
        contentPane.add(lblNombre);

        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setBounds(35, 330, 70, 16);
        contentPane.add(lblGenero);

        JLabel lblArtista = new JLabel("Artista:");
        lblArtista.setBounds(250, 300, 70, 16);
        contentPane.add(lblArtista);

        JLabel lblEstante = new JLabel("Estante:");
        lblEstante.setBounds(250, 330, 70, 16);
        contentPane.add(lblEstante);

        JLabel lblPosicion = new JLabel("Posición:");
        lblPosicion.setBounds(420, 330, 70, 16);
        contentPane.add(lblPosicion);

        textFieldNombre = new JTextField();
        textFieldNombre.setBounds(100, 295, 130, 26);
        contentPane.add(textFieldNombre);
        textFieldNombre.setColumns(10);

        textFieldGenero = new JTextField();
        textFieldGenero.setBounds(100, 325, 130, 26);
        contentPane.add(textFieldGenero);
        textFieldGenero.setColumns(10);

        textFieldArtista = new JTextField();
        textFieldArtista.setBounds(315, 295, 130, 26);
        contentPane.add(textFieldArtista);
        textFieldArtista.setColumns(10);

        textFieldEstante = new JTextField();
        textFieldEstante.setBounds(315, 325, 80, 26);
        contentPane.add(textFieldEstante);
        textFieldEstante.setColumns(10);

        textFieldPosicion = new JTextField();
        textFieldPosicion.setBounds(480, 325, 55, 26);
        contentPane.add(textFieldPosicion);
        textFieldPosicion.setColumns(10);

        cargarCDs();
    }

    private void cargarCDs() {
        tableModel.setRowCount(0);

        if (basedatos.estaConectado()) {
            Vector<Vector<String>> data = basedatos.consultarCDs();

            for (Vector<String> row : data) {
                tableModel.addRow(row);
            }
        }
    }
}
