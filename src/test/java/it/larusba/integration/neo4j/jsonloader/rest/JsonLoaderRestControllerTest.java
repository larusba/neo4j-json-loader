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

import java.util.LinkedHashMap;

import org.junit.Test;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.server.rest.domain.JsonHelper;
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

			String content = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": \"CEO @ LARUS Business Automation\"}";

			JsonDocument jsonDocument = new JsonDocument("1234567890QWERTY", "Person", content, JsonMappingStrategy.ATTRIBUTE_BASED, null);
			
			String json = JsonHelper.createJsonFrom(jsonDocument);
			
			System.out.println(json);
			
			HTTP.Response response = HTTP.PUT(server.httpURI().resolve("jsonloader").toString(), jsonDocument);

			assertEquals(200, response.status());
		}
	}
}
