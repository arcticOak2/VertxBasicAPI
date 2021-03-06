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

package com.annihilator.vertx.resources;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

public class ApiResources {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiResources.class);
	
	public void getAllProducts(RoutingContext routingContext) {
		JsonObject responseJson = new JsonObject();

		JsonArray items = new JsonArray();

		JsonObject firstItem = new JsonObject();

		firstItem.put("Item", "keyboard").put("ItemID", 234);

		JsonObject secondItem = new JsonObject();

		secondItem.put("Item", "mouse").put("ItemID", 2334);

		items.add(firstItem).add(secondItem);

		responseJson.put("items", items);

		routingContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json")
			.end(Json.encodePrettily(responseJson));
	}

	public void getProductByID(RoutingContext routingContext) {
		
		final String productID = routingContext.request().getParam("id");
		
		JsonObject firstItem = new JsonObject();

		firstItem.put("Item", "keyboard").put("ItemID", productID);
		
		
		routingContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json")
			.end(Json.encodePrettily(firstItem));
	}

	public void addProduct(RoutingContext routingContext) {
		JsonObject jsonBody = routingContext.getBodyAsJson();
		
		JsonObject firstItem = new JsonObject();

		firstItem.put("resp", "ItemID: " + jsonBody.getString("id") + " created");
		
		routingContext.response()
			.setStatusCode(201)
			.putHeader("content-type", "application/json")
			.end(Json.encodePrettily(firstItem));
	}
	
	public void updateProductByID(RoutingContext routingContext) {
		final String productID = routingContext.request().getParam("id");
		
		JsonObject firstItem = new JsonObject();

		firstItem.put("resp", "ItemID: " + productID + " updated");
		
		routingContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json")
			.end(Json.encodePrettily(firstItem));
	}
	
	public void deleteProductByID(RoutingContext routingContext) {
		final String productID = routingContext.request().getParam("id");
		
		JsonObject firstItem = new JsonObject();

		firstItem.put("resp", "ItemID: " + productID + " deleted");
		
		routingContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json")
			.end(Json.encodePrettily(firstItem));
	}
	
	public void defaultProcessorForAllAPI(RoutingContext routingContext) {
		String authToken = routingContext.request().headers().get("AuthToken");
		
		if(!authToken.equals("123")) {
			routingContext.response()
				.setStatusCode(401)
				.putHeader("content-type", "application/json")
				.end();
			LOGGER.error("Failed basic authentication check");
		} else {
			// Allowing CORS
			routingContext.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			routingContext.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE");
			
			routingContext.next();
		}
	}
}
