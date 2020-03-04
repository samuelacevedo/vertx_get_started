package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> future) {
    vertx
      .createHttpServer()
      .requestHandler(r -> {
        r.response().end("<h1>Hello from my first " +
          "Vert.x 3 application</h1>");
      })
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

  /* @Override
  public void start() {
    vertx.createHttpServer()
        .requestHandler(req -> req.response().end("Hello Vert.x!"))
        .listen(8080);
  } */

 /* @Override
  public void start(Promise<Void> promise){
    promise.complete();
  } */

}
