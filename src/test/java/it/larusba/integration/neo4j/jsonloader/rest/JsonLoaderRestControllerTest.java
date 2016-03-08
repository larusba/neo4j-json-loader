/**
 * Copyright (c) 2016 LARUS Business Automation [http://www.larus-ba.it]
 * <p>
 * This file is part of the "LARUS Integration Framework for Neo4j".
 * <p>
 * The "LARUS Integration Framework for Neo4j" is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.larusba.integration.neo4j.jsonloader.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.test.server.HTTP;

import it.larusba.integration.neo4j.jsonloader.bean.JsonDocument;
import it.larusba.integration.neo4j.jsonloader.mapping.JsonMappingStrategy;

/**
 *
 * @author Lorenzo Speranzoni
 */
public class JsonLoaderRestControllerTest {

	@Test
	public void shouldLoadJSONIntoGraph() throws Exception {

		try (ServerControls server = TestServerBuilders.newInProcessBuilder()
		    .withExtension("/jsonloader", JsonLoaderRestController.class).newServer()) {

			String jsonPersonDocument = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": \"CEO @ LARUS Business Automation\"}";

			JsonDocument jsonDocument = new JsonDocument("1234567890QWERTY", "Person", jsonPersonDocument,
			    JsonMappingStrategy.ATTRIBUTE_BASED, null);

			HTTP.Response response = HTTP.PUT(server.httpURI().resolve("jsonloader").toString(), jsonDocument);

			assertEquals(200, response.status());

			assertEquals(0, response.get("constraintsAdded").asInt());
			assertEquals(0, response.get("constraintsRemoved").asInt());
			assertEquals(0, response.get("containsUpdates").asInt());
			assertEquals(0, response.get("indexesAdded").asInt());
			assertEquals(0, response.get("indexesRemoved").asInt());
			assertEquals(1, response.get("labelsAdded").asInt());
			assertEquals(0, response.get("labelsRemoved").asInt());
			assertEquals(1, response.get("nodesCreated").asInt());
			assertEquals(0, response.get("nodesDeleted").asInt());
			assertEquals(5, response.get("propertiesSet").asInt());
			assertEquals(0, response.get("relationshipsCreated").asInt());
			assertEquals(0, response.get("relationshipsDeleted").asInt());
		}
	}

	@Test
	public void shouldLoadJSONWithNestedObjectsIntoGraph() throws Exception {

		try (ServerControls server = TestServerBuilders.newInProcessBuilder()
		    .withExtension("/jsonloader", JsonLoaderRestController.class).newServer()) {

			String jsonAddressDocument = "{\"street\": \"Via B. Maderna, 7\", \"zipCode\": 30174, \"city\": \"Mestre\", \"province\": \"Venice\", \"country\": \"Italy\"}";

			String jsonCompanyDocument = "{\"name\": \"LARUS Business Automation\", \"vat\": \"03540680273\", \"address\": "
			    + jsonAddressDocument + "}";

			String jsonJobDocument = "{\"role\": \"CEO\", \"company\": " + jsonCompanyDocument + "}";

			String jsonPersonDocument = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": "
			    + jsonJobDocument + "}";

			JsonDocument jsonDocument = new JsonDocument("1234567890QWERTY", "Person", jsonPersonDocument,
			    JsonMappingStrategy.ATTRIBUTE_BASED, null);

			HTTP.Response response = HTTP.PUT(server.httpURI().resolve("jsonloader").toString(), jsonDocument);

			assertEquals(200, response.status());

			assertEquals(0, response.get("constraintsAdded").asInt());
			assertEquals(0, response.get("constraintsRemoved").asInt());
			assertEquals(0, response.get("containsUpdates").asInt());
			assertEquals(0, response.get("indexesAdded").asInt());
			assertEquals(0, response.get("indexesRemoved").asInt());
			assertEquals(4, response.get("labelsAdded").asInt());
			assertEquals(0, response.get("labelsRemoved").asInt());
			assertEquals(4, response.get("nodesCreated").asInt());
			assertEquals(0, response.get("nodesDeleted").asInt());
			assertEquals(15, response.get("propertiesSet").asInt());
			assertEquals(3, response.get("relationshipsCreated").asInt());
			assertEquals(0, response.get("relationshipsDeleted").asInt());
		}
	}
}
