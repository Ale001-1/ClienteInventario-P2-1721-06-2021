package sv.edu.utec.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProductoApiService {

    private static final String URL_API = "https://dummyjson.com/products?limit=5";

    private final HttpClient cliente;
    private final ObjectMapper mapper;

    public ProductoApiService() {
        cliente = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
    }

    public List<ProductoApi> obtenerProductos()
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_API))
                .GET()
                .build();

        HttpResponse<String> response = cliente.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Error al consumir la API. Código HTTP: "
                            + response.statusCode()
            );
        }

        RespuestaProductos respuesta =
                mapper.readValue(response.body(), RespuestaProductos.class);

        return respuesta.getProducts();
    }
}