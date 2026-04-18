import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://localhost:8080/dsaApp/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig()
                .packages("services")
                .register(OpenApiResource.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        StaticHttpHandler staticHttpHandler = new StaticHttpHandler("./public/");
        server.getServerConfiguration().addHttpHandler(staticHttpHandler, "/");

        System.out.println("Servidor arrencat a: " + BASE_URI);
        System.out.println("OpenAPI JSON: " + BASE_URI + "openapi.json");
        System.out.println("Prem ENTER per aturar...");

        System.in.read();
        server.stop();
    }
}