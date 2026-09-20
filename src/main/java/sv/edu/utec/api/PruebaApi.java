package sv.edu.utec.api;

import java.util.List;

public class PruebaApi {

    public static void main(String[] args) {

        ProductoApiService servicio = new ProductoApiService();

        try {
            List<ProductoApi> productos = servicio.obtenerProductos();

            System.out.println("=== PRODUCTOS OBTENIDOS DE LA API ===");

            for (ProductoApi producto : productos) {
                System.out.println(
                        "ID: " + producto.getId()
                                + " | Producto: " + producto.getTitle()
                                + " | Stock: " + producto.getStock()
                );
            }

        } catch (Exception e) {
            System.out.println("Error al consumir la API: " + e.getMessage());
        }
    }
}