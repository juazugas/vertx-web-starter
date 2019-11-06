package com.example.webstarter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * $JAVA_HOME/bin/java -jar target/web-starter-1.0.0-SNAPSHOT-fat.jar run com.example.webstarter.MainVerticle  --launcher-class=io.vertx.core.Launcher
 */
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    // http :8080/hi?name=world
    router.get("/hi")
          .handler(rc -> rc.response().end("Hi from router"));
    // http :8080/hi?name=world key:val
    router.post("/hi")
          .handler(BodyHandler.create())
          .handler(hi("Hi, here is your body."));
    // http :8080/nohi?name=world key=val
    router.post("/nohi")
          .consumes("application/json")
          .handler(BodyHandler.create())
          .handler(hi("Nohi, here is your body."));
    server.requestHandler(router).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private Handler<RoutingContext> hi(String message) {
    return  rc -> rc.response().end(message+"\n"+new String(rc.getBody().getBytes()));
  }

}
