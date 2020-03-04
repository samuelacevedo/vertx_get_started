package io.vertx.starter;

import com.hikari.connection.HikariConnection;
import io.vertx.businesslogic.Zone;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

  // Store our product
  private Map<Integer, Zone> zones = new LinkedHashMap<>();

  @Override
  public void start(Future<Void> future) {

    createSomeData();

    // Create a router object.
    Router router = Router.router(vertx);

    // Bind "/" to our hello message - so we are still compatible.
    router.route("/").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response
        .putHeader("content-type", "text/html")
        .end("<h1>Hello from my first Vert.x 3 application</h1>");
    });

    // Serve static resources from the /assets directory
    router.route("/assets/*").handler(StaticHandler.create("assets"));

    router.get("/api/zones").handler(this::getZones);

    // Create the HTTP server and pass the "accept" method to the request handler.
    vertx
      .createHttpServer()
      .requestHandler(router::accept)
      .listen(
        config().getInteger("http.port",8080),
        result -> {
          if (result.succeeded()) {
            future.complete();
          } else {
            future.fail(result.cause());
          }
      });
  }

  private void createSomeData(){
    //Zone b = new Zone("Zona B", "Pico el Avila");
    //zones.put(b.getId(), b);

    try {
      Connection connection = HikariConnection.getConnection();
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM ZONE");
      ResultSet resultSet = statement.executeQuery();
      while(resultSet.next()){
        Zone zone = new Zone(
          resultSet.getString("name"),
          resultSet.getString("description")
        );
        zones.put(zone.getId(), zone);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  private void getZones(RoutingContext routingContext) {
    routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(zones.values()));
  }
}
