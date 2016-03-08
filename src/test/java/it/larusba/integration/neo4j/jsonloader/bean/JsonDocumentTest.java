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
package it.larusba.integration.neo4j.jsonloader.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.neo4j.server.rest.domain.JsonHelper;

import it.larusba.integration.neo4j.jsonloader.mapping.JsonMappingStrategy;

/**
 *
 * @author Lorenzo Speranzoni
 */
public class JsonDocumentTest {

	@Test
	public void shouldSerializeJsonDocument() {

		JsonDocument jsonDocument = new JsonDocument();

		String jsonContent = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": \"CEO @ LARUS Business Automation\"}";

		List<JsonObjectDescriptor> objectDescriptors = new ArrayList<JsonObjectDescriptor>();
		JsonObjectDescriptor personObjectDescriptor = new JsonObjectDescriptor("Person",
		    Arrays.asList("firstName", "lastname"), null);
		objectDescriptors.add(personObjectDescriptor);

		jsonDocument.setId("1234567890QWERTY");
		jsonDocument.setType("Person");
		jsonDocument.setContent(jsonContent);
		jsonDocument.setMappingStrategy(JsonMappingStrategy.DOMAIN_DRIVEN);
		jsonDocument.setObjectDescriptors(objectDescriptors);

		System.out.println(JsonHelper.createJsonFrom(jsonDocument));
	}
}
