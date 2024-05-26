package com.example.sanitech;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.example.sanitech.CrearusuariosController.mostrarError;

public class ModificararticulosController {

    private ArticulosController articulosController;
    private Articulos articuloSeleccionado;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private TextField tfCodigoarticulo;

    @FXML
    private TextField tfCodigoproveedor;

    @FXML
    private TextField tfArticulo;

    @FXML
    private TextField tfPreciocompra;

    @FXML
    private TextField tfPrecioventa;

    @FXML
    private DatePicker dpFechaalta;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnMinimizar;

    @FXML
    private Button btnCerrar;

    @FXML
    private void initialize() {
        btnGuardar.setOnAction(event -> modificarArticulo());
        btnCancelar.setOnAction(event -> cerrarVentana());
    }

    @FXML
    private void onMousePressed(MouseEvent event) { // Metodo para mantener pulsado usando MouseEvent debido a la escena
        Stage stage = (Stage) ((AnchorPane) event.getSource()).getScene().getWindow();
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void onMouseDragged(MouseEvent event) { // Metodo para arrastrar usando MouseEvent debido a la escena
        Stage stage = (Stage) ((AnchorPane) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    protected void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void minimizarVentana(ActionEvent event) {
        Stage stage = (Stage) btnMinimizar.getScene().getWindow();
        stage.setIconified(true);
    }

    public void setArticulosController(ArticulosController articulosController) { // Setter para establecer el controlador de ArticulosController
        this.articulosController = articulosController;
    }

    public void mostrarDatosArticulo(Articulos articulos) {
        articuloSeleccionado = articulos;
        tfCodigoarticulo.setText(articulos.getCodigoarticulo());
        tfArticulo.setText(articulos.getArticulo());
        tfCodigoproveedor.setText(String.valueOf(articulos.getCodigoproveedor()));
        tfPreciocompra.setText(String.valueOf(articulos.getPreciocompra()));
        tfPrecioventa.setText(String.valueOf(articulos.getPrecioventa()));
        dpFechaalta.setValue(articulos.getFechaalta());
    }

    public void initData(Articulos articulos) {
        this.articuloSeleccionado = articulos;
        // Mostrar los datos del articulo en los textfields
        mostrarDatosArticulo(articulos);
    }

    private void cerrarVentana() {
        // Obtener el Stage (escenario) actual
        Stage stage = (Stage) btnCancelar.getScene().getWindow();

        // Cerrar la ventana actual
        stage.close();
    }

    private void modificarArticulo() {
        // Obtener los datos ingresados por el usuario
        String codigoArticulo = tfCodigoarticulo.getText();
        String articulo = tfArticulo.getText();
        String codigoProveedor = tfCodigoproveedor.getText();
        String precioCompra = tfPreciocompra.getText();
        String precioVenta = tfPrecioventa.getText();
        String fechaAlta = null;
        if (dpFechaalta.getValue() != null) {
            fechaAlta = dpFechaalta.getValue().toString();
        }

        // Validar que el código de artículo no esté vacío y no contenga espacios en blanco
        if (codigoArticulo.isEmpty() || codigoArticulo.contains(" ")) {
            mostrarError("El campo código_articulo es obligatorio y no puede contener espacios en blanco");
            return;
        }

        // Validar que el articulo no esté vacío
        if (articulo.isEmpty()) {
            mostrarError("El campo articulo es obligatorio");
            return;
        }

        // Validar que el codigo de proveedor contenga solo números
        if (!codigoProveedor.isEmpty() && !codigoProveedor.matches("\\d+")) {
            mostrarError("Solo se admiten números en el campo codigo_proveedor");
            return;
        }

        // Validar que el precio de compra y el precio de venta sea un numero decimal
        try {
            // Convertir los precios a números decimales
            BigDecimal precioCompraDecimal = new BigDecimal(precioCompra);
            BigDecimal precioVentaDecimal = new BigDecimal(precioVenta);

            // Validar que los precios sean positivos
            if (precioCompraDecimal.compareTo(BigDecimal.ZERO) < 0 || precioVentaDecimal.compareTo(BigDecimal.ZERO) < 0) {
                mostrarError("Los precios no pueden ser negativos");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarError("Los precios deben ser números decimales");
            return;
        }

        // Validar que la fecha de alta tenga el formato "año-mes-día"
        if (fechaAlta == null || !fechaAlta.matches("\\d{4}-\\d{2}-\\d{2}")) {
            mostrarError("El formato de la fecha de alta debe ser año-mes-día (por ejemplo, 2024-01-21)");
            return;
        }

        // Establecer la conexión con la base de datos
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/saneamientos", "root", "rootpass")) {
            // Preparar la consulta SQL de actualizacion
            String sql = "UPDATE articulos SET codigo_articulo = ?, articulo = ?, codigo_proveedor = ?, precio_compra = ?, precio_venta = ?, fecha_alta = ? WHERE codigo_articulo = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Establecer los parámetros en la consulta preparada
                statement.setString(1, codigoArticulo);
                statement.setString(2, articulo);
                statement.setString(3, codigoProveedor);
                statement.setString(4, precioCompra);
                statement.setString(5, precioVenta);
                statement.setString(6, fechaAlta);
                statement.setString(7, articuloSeleccionado.getCodigoarticulo());

                // Ejecutar la consulta
                int filasAfectadas = statement.executeUpdate();

                // Comprobar si se actualizo correctamente
                if (filasAfectadas > 0) {
                    // Cargar los datos actualizados en la tabla de ArticulosController
                    cargarDatosArticulos();
                } else {
                    mostrarError("No se pudo modificar el artículo");
                }
            }
        } catch (SQLException e) {
            mostrarError("Error al conectar a la base de datos: " + e.getMessage());
        }

        // Actualizar la tabla de articulos en ArticulosController despues de modificar el articulo
        if (articulosController != null) {
            articulosController.cargarDatos();
        }

        // Cerrar la ventana
        cerrarVentana();
    }

    private void cargarDatosArticulos() {
        if (articulosController != null) {
            articulosController.cargarDatos();
        }
    }
}
