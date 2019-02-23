package com.anihillator.vertx.api;

import com.anihillator.vertx.resources.ApiResources;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class BasicWebVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicWebVerticle.class);

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();

		ConfigRetriever config = ConfigRetriever.create(vertx);
		config.getConfig(conf -> {
			if (conf.succeeded()) {
				JsonObject configJson = conf.result();
				System.out.println(configJson.encodePrettily());
				DeploymentOptions options = new DeploymentOptions().setConfig(configJson);
//				options.getConfig().put("http.port", 8080);
				vertx.deployVerticle(new BasicWebVerticle(), options);
			}
		});
	}

	@Override
	public void start() {

		LOGGER.info("Verticle App started");

		Router router = Router.router(vertx);
		Router subRouter = Router.router(vertx);
		
		ApiResources resource = new ApiResources();

		// API Routing
		
		subRouter.route("/*").handler(resource::defaultProcessorForAllAPI);
		subRouter.route("/v1/products*").handler(BodyHandler.create());
		subRouter.get("/v1/products").handler(resource::getAllProducts);
		subRouter.get("/v1/products/:id").handler(resource::getProductByID);
		subRouter.post("/v1/products").handler(resource::addProduct);
		subRouter.put("/v1/products/:id").handler(resource::updateProductByID);
		subRouter.delete("/v1/products/:id").handler(resource::deleteProductByID);
		
		router.mountSubRouter("/api/", subRouter);

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port"));
	}

	@Override
	public void stop() {
		LOGGER.info("Verticle App stopped");
	}

}
