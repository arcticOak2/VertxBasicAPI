
// Copyright 2019 Author Abhijeet Kumar
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.annihilator.vertx.api;

import com.annihilator.vertx.resources.ApiResources;

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
