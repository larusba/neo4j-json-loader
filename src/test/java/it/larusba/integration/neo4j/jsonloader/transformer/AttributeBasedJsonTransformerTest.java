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

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.larusba.integration.common.document.bean.JsonDocument;
import it.larusba.integration.common.document.mapping.JsonMappingStrategy;

/**
 *
 * @author Lorenzo Speranzoni
 */
public class AttributeBasedJsonTransformerTest {

  @Test
  public void shouldTransformJsonDocumentIntoACypherStatement() {

    try {

      JsonTransformer<Set<String>> documentTransformer = new AttributeBasedJsonTransformer();

      String jsonPersonDocument = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": \"CEO @ LARUS Business Automation\"}";

      JsonDocument jsonDocument = new JsonDocument("1234567890QWERTY", "Person", jsonPersonDocument,
          JsonMappingStrategy.ATTRIBUTE_BASED, null);

      System.out.println(documentTransformer.transform(jsonDocument));

    } catch (Exception e) {

      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void shouldTransformNestedJsonDocumentIntoACypherStatement() {

    try {

      JsonTransformer<Set<String>> documentTransformer = new AttributeBasedJsonTransformer();

      String jsonAddressDocument = "{\"street\": \"Via B. Maderna, 7\", \"zipCode\": 30174, \"city\": \"Mestre\", \"province\": \"Venice\", \"country\": \"Italy\"}";

      String jsonCompanyDocument = "{\"name\": \"LARUS Business Automation\", \"vat\": \"03540680273\", \"address\": "
          + jsonAddressDocument + "}";

      String jsonJobDocument = "{\"role\": \"CEO\", \"company\": " + jsonCompanyDocument + "}";

      String jsonPersonDocument = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": "
          + jsonJobDocument + "}";

      JsonDocument jsonDocument = new JsonDocument("1234567890QWERTY", "Person", jsonPersonDocument,
          JsonMappingStrategy.ATTRIBUTE_BASED, null);

      System.out.println(documentTransformer.transform(jsonDocument));

    } catch (Exception e) {

      Assert.fail(e.getMessage());
    }
  }

}
