package io.vertx.starter;

import com.hikari.connection.HikariConnection;
import io.vertx.businesslogic.Zone;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
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

  private String post_zone = "INSERT INTO ZONE ("
                              + " name,"
                              + " description) VALUES ("
                              + "?, ?)";

  private String get_zones = "SELECT * FROM ZONE";

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

    /* Zones */
    router.get("/api/zones").handler(this::getZones);
    router.route("/api/zones*").handler(BodyHandler.create());
    router.post("/api/zones").handler(this::addZone);
    router.delete("/api/zones/:id").handler(this::deleteZone);

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
    try {
      Connection connection = HikariConnection.getConnection();
      PreparedStatement statement = connection.prepareStatement(get_zones);
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

  private void addZone(RoutingContext routingContext){
    final Zone zone = Json.decodeValue(routingContext.getBodyAsString(),
      Zone.class);

    /* Add to the database */
      try {
        Connection connection = HikariConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(post_zone);
        statement.setString(1, zone.getName());
        statement.setString(2, zone.getDescription());
        statement.execute();

        zones.put(zone.getId(), zone);
        routingContext.response()
          .setStatusCode(201)
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(Json.encodePrettily(zone));
      } catch (SQLException e) {
        e.printStackTrace();
      }
  }

  private void deleteZone(RoutingContext routingContext){
    String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      Integer idAsInteger = Integer.valueOf(id);
      zones.remove(idAsInteger);
    }
    routingContext.response().setStatusCode(204).end();
  }
}
