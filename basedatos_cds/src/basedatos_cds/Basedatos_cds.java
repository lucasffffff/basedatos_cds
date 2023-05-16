package basedatos_cds;

import gui.CDManagerFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;


public class Basedatos_cds {

    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/esquema";
    private final String usuario = "root";
    private final String contraseña = "programacion";

    public static void main(String[] args) {
        Basedatos_cds basedatos = new Basedatos_cds();
        basedatos.establecerConexion();

        CDManagerFrame frame = new CDManagerFrame(basedatos);
        frame.setVisible(true);
    }

    public void establecerConexion() {
        try {
            connection = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexión establecida correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al establecer la conexión.");
        }
    }

    public void cerrarConexion() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cerrar la conexión.");
        }
    }

    public boolean estaConectado() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertarCD(String nombre, String genero, String artista, int estante, int posicion) {
        try {
            String query = "INSERT INTO cds (nombre_cd, genero_cd, artista_cd, estante_cd, posicion_cd) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombre);
            statement.setString(2, genero);
            statement.setString(3, artista);
            statement.setInt(4, estante);
            statement.setInt(5, posicion);

            statement.executeUpdate();
            statement.close();

            System.out.println("CD insertado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al insertar el CD.");
        }
    }
    
    public void editarCD(String nombre, String genero, String artista, int estante, int posicion) {
    try {
        String query = "UPDATE cds SET genero_cd = ?, artista_cd = ?, estante_cd = ?, posicion_cd = ? WHERE nombre_cd = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, genero);
        statement.setString(2, artista);
        statement.setInt(3, estante);
        statement.setInt(4, posicion);
        statement.setString(5, nombre);

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("CD actualizado correctamente.");
        } else {
            System.out.println("No se encontró un CD con ese nombre.");
        }
        statement.close();
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error al actualizar el CD.");
    }
}


    public Vector<Vector<String>> consultarCDs() {
        Vector<Vector<String>> data = new Vector<>();

        try {
            String query = "SELECT * FROM cds";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Vector<String> row = new Vector<>();
                row.add(resultSet.getString("nombre_cd"));
                row.add(resultSet.getString("genero_cd"));
                row.add(resultSet.getString("artista_cd"));
                row.add(String.valueOf(resultSet.getInt("estante_cd")));
                row.add(String.valueOf(resultSet.getInt("posicion_cd")));

                data.add(row);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al consultar los CDs.");
        }

        return data;
    }

    public void eliminarCD(String nombre) {
        try {
            String query = "DELETE FROM cds WHERE nombre_cd = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombre);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("CD eliminado correctamente.");
            } else {
                System.out.println("No se encontró un CD con ese nombre.");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al eliminar el CD.");
        }
    }
}
