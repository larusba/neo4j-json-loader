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

import org.junit.Assert;
import org.junit.Test;

import it.larusba.integration.neo4j.jsonloader.bean.JsonDocument;
import it.larusba.integration.neo4j.jsonloader.bean.JsonObjectDescriptor;
import it.larusba.integration.neo4j.jsonloader.mapping.JsonMappingStrategy;

/**
 *
 * @author Lorenzo Speranzoni
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

      JsonTransformer<String> documentTransformer = new DomainBasedJsonTransformer();

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

      JsonObjectDescriptor companyObjectDescriptor = new JsonObjectDescriptor("Company", Arrays.asList("vat"), "type");

      JsonObjectDescriptor jobObjectDescriptor = new JsonObjectDescriptor("Job", Arrays.asList("role"), "type");

      JsonObjectDescriptor personObjectDescriptor = new JsonObjectDescriptor("Person",
          Arrays.asList("firstname", "lastname"), "type");

      List<JsonObjectDescriptor> jsonObjectDescriptors = new ArrayList<JsonObjectDescriptor>();
      jsonObjectDescriptors.add(addressObjectDescriptor);
      jsonObjectDescriptors.add(companyObjectDescriptor);
      jsonObjectDescriptors.add(jobObjectDescriptor);
      jsonObjectDescriptors.add(personObjectDescriptor);

      String jsonAddressDocument = "{\"street\": \"Via B. Maderna, 7\", \"zipCode\": 30174, \"city\": \"Mestre\", \"province\": \"Venice\", \"country\": \"Italy\"}";

      String jsonCompanyDocument = "{\"name\": \"LARUS Business Automation\", \"vat\": \"03540680273\", \"address\": "
          + jsonAddressDocument + ", \"type\": \"Company\"}";

      String jsonJobDocument = "{\"role\": \"CEO\", \"company\": " + jsonCompanyDocument + ", \"type\": \"Job\"}";

      String jsonPersonDocument = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": "
          + jsonJobDocument + ", \"type\": \"Person\"}";

      JsonDocument jsonDocument = new JsonDocument("1234567890QWERTY", "Person", jsonPersonDocument,
          JsonMappingStrategy.DOMAIN_DRIVEN, jsonObjectDescriptors);

      JsonTransformer<String> documentTransformer = new DomainBasedJsonTransformer();

      System.out.println(documentTransformer.transform(jsonDocument));

    } catch (Exception e) {

      e.printStackTrace();

      Assert.fail(e.getMessage());
    }
  }

}
