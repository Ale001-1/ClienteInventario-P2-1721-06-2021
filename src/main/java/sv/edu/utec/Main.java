package sv.edu.utec;

import sv.edu.utec.api.ProductoApi;
import sv.edu.utec.api.ProductoApiService;
import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.modelo.Producto;
import sv.edu.utec.servicio.InventarioJsonService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static final ProductoDAO dao = new ProductoDAO();
    private static final InventarioJsonService jsonService =
            new InventarioJsonService();

    private static final String ARCHIVO = "inventario.json";

    public static void main(String[] args) {

        try {
            // 1. Preparar la base de datos
            dao.crearTabla();
            System.out.println("Tabla producto lista.");

            sembrarDatos();

            System.out.println("\n--- Inventario inicial ---");
            imprimir(dao.listar());

            // 2. Respaldar en JSON antes de modificar
            jsonService.exportar(ARCHIVO);
            System.out.println("\nRespaldo generado en " + ARCHIVO);

            // 3. Modificar la base de datos
            if (dao.actualizar(
                    new Producto(2, "Monitor 24 pulgadas", 12))) {

                System.out.println("Producto 2 actualizado.");
            }

            if (dao.eliminar(1)) {
                System.out.println("Producto 1 eliminado.");
            }

            System.out.println("\n--- Despues de los cambios ---");
            imprimir(dao.listar());

            // 4. Restaurar desde el respaldo JSON
            int restaurados = jsonService.importar(ARCHIVO);

            System.out.println(
                    "\nRegistros restaurados desde JSON: "
                            + restaurados
            );

            System.out.println("\n--- Inventario final ---");
            imprimir(dao.listar());

            // 5. Consumir API REST externa
            System.out.println(
                    "\n--- Productos obtenidos desde API REST ---"
            );

            ProductoApiService apiService =
                    new ProductoApiService();

            List<ProductoApi> productosApi =
                    apiService.obtenerProductos();

            for (ProductoApi producto : productosApi) {

                System.out.printf(
                        "ID: %d | Producto: %s | Precio: $%.2f | Stock: %d%n",
                        producto.getId(),
                        producto.getTitle(),
                        producto.getPrice(),
                        producto.getStock()
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error de base de datos: "
                            + e.getMessage()
            );

        } catch (IOException e) {

            System.out.println(
                    "Error al leer/escribir JSON o consumir la API: "
                            + e.getMessage()
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    "La solicitud a la API fue interrumpida: "
                            + e.getMessage()
            );
        }
    }

    // Inserta solamente los productos que todavía no existen.
    private static void sembrarDatos() throws SQLException {

        if (!dao.existe(1)) {
            dao.insertar(
                    new Producto(
                            1,
                            "Teclado mecanico",
                            15
                    )
            );
        }

        if (!dao.existe(2)) {
            dao.insertar(
                    new Producto(
                            2,
                            "Monitor 24 pulgadas",
                            8
                    )
            );
        }
    }

    // Imprime los productos almacenados en la base de datos.
    private static void imprimir(List<Producto> productos) {

        System.out.printf(
                "%-5s %-25s %10s%n",
                "ID",
                "PRODUCTO",
                "CANTIDAD"
        );

        for (Producto p : productos) {

            System.out.printf(
                    "%-5d %-25s %10d%n",
                    p.getId(),
                    p.getNombre(),
                    p.getCantidad()
            );
        }
    }
}