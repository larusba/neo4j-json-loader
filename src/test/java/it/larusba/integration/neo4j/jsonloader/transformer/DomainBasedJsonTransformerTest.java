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
package it.larusba.integration.neo4j.jsonloader.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import it.larusba.integration.common.document.bean.JsonDocument;
import it.larusba.integration.common.document.bean.JsonObjectDescriptor;
import it.larusba.integration.common.document.mapping.JsonMappingStrategy;

/**
 * @author Lorenzo Speranzoni
 * @author Riccardo Birello
 */
public class DomainBasedJsonTransformerTest {

	@Test
	public void shouldTransformJsonDocumentIntoACypherStatement() {

		try {

			JsonObjectDescriptor personObjectDescriptor = new JsonObjectDescriptor("Person",
					Arrays.asList("firstname", "lastname"), "type");

			List<JsonObjectDescriptor> jsonObjectDescriptors = new ArrayList<JsonObjectDescriptor>();
			jsonObjectDescriptors.add(personObjectDescriptor);

			String jsonPersonDocument = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": \"CEO @ LARUS Business Automation\", \"type\": \"Person\"}";

			JsonDocument jsonDocument = new JsonDocument("1234567890QWERTY", "Person", jsonPersonDocument,
					JsonMappingStrategy.DOMAIN_DRIVEN, jsonObjectDescriptors);

			JsonTransformer<String> documentTransformer = new UnrefactoredDomainBasedJsonTransformer();

			System.out.println(documentTransformer.transform(jsonDocument));

		} catch (Exception e) {

			e.printStackTrace();

			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void shouldTransformNestedJsonDocumentIntoACypherStatement() {

		try {

			JsonObjectDescriptor addressObjectDescriptor = new JsonObjectDescriptor("Address",
					Arrays.asList("street", "zipCode"), "type");

			JsonObjectDescriptor companyObjectDescriptor = new JsonObjectDescriptor("Company", Arrays.asList("vat"),
					"type");

			JsonObjectDescriptor jobObjectDescriptor = new JsonObjectDescriptor("Job", Arrays.asList("role"), "type");

			JsonObjectDescriptor personObjectDescriptor = new JsonObjectDescriptor("Person",
					Arrays.asList("firstname", "lastname"), "type");

			List<JsonObjectDescriptor> jsonObjectDescriptors = new ArrayList<JsonObjectDescriptor>();
			jsonObjectDescriptors.add(addressObjectDescriptor);
			jsonObjectDescriptors.add(companyObjectDescriptor);
			jsonObjectDescriptors.add(jobObjectDescriptor);
			jsonObjectDescriptors.add(personObjectDescriptor);

			String jsonAddressDocument = "{\"street\": \"Via B. Maderna, 7\", \"zipCode\": 30174, \"city\": \"Mestre\", \"province\": \"Venice\", \"country\": \"Italy\", \"type\": \"Address\"}";

			String jsonCompanyDocument = "{\"name\": \"LARUS Business Automation\", \"vat\": \"03540680273\", \"address\": "
					+ jsonAddressDocument + ", \"type\": \"Company\"}";

			String jsonJobDocument = "{\"role\": \"CEO\", \"company\": " + jsonCompanyDocument + ", \"type\": \"Job\"}";

			String jsonPersonDocument = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": "
					+ jsonJobDocument + ", \"type\": \"Person\"}";

			JsonDocument jsonDocument = new JsonDocument("1234567890QWERTY", "Person", jsonPersonDocument,
					JsonMappingStrategy.DOMAIN_DRIVEN, jsonObjectDescriptors);

			JsonTransformer<String> documentTransformer = new UnrefactoredDomainBasedJsonTransformer();

			System.out.println(documentTransformer.transform(jsonDocument));

		} catch (Exception e) {

			e.printStackTrace();

			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test() throws Exception {
		String content = IOUtils.toString(getClass().getResourceAsStream("/json/album.json"));
//		String content = IOUtils.toString(getClass().getResourceAsStream("/json/album-mini.json"));
		String id = UUID.randomUUID().toString();
		String type = "Album";
		JsonMappingStrategy mappingStrategy = JsonMappingStrategy.DOMAIN_DRIVEN;
		JsonObjectDescriptor descriptor1 = new JsonObjectDescriptor("Album", Arrays.asList("id", "type"), "type");
		List<JsonObjectDescriptor> descriptors = new ArrayList<>();
		descriptors.add(descriptor1);
		JsonDocument jsonDocument = new JsonDocument(id, type, content, mappingStrategy, descriptors);
		UnrefactoredDomainBasedJsonTransformer transformer = new UnrefactoredDomainBasedJsonTransformer();
		String transform = transformer.transform(jsonDocument);
		System.out.println(transform);
	}
}
