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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import it.larusba.integration.neo4j.jsonloader.bean.JsonDocument;

/**
 * Domain agnostic {@link JsonTransformer} implementation.
 * <p/>
 * It converts a JSON document into a Cypher statement, following this strategy:
 * <p/>
 * <ul>
 * <li>When primitive, JSON attributes become node properties and their names
 * are used as property names.</li>
 * <li>When object, JSON attributes become new nodes (in a recursive fashion)
 * connected to their own father node.</li>
 * </ul>
 * As an example, the following JSON document
 * 
 * <pre>
 * Person: {
 *   "firstname" : "Lorenzo",
 *   "lastname"  : "Speranzoni",
 *   "age"       : 41,
 *   "job"       :
 *   {
 *     "role"    : "CEO",
 *     "company" : "LARUS Business Automation"
 *   }
 * }
 * </pre>
 * 
 * will be translated into this sub-graph:
 * 
 * <pre>
 * CREATE (person:Person { firstname: 'Lorenzo', lastname: 'Speranzoni', age: 41 } )
 * CREATE (job:JOB { role: 'CEO', company: 'LARUS Business Automation' })
 * CREATE (person)-[:PERSON_JOB]->(job)
 * </pre>
 * 
 * @author Lorenzo Speranzoni
 * 
 * @see <a href="http://neo4j.com/blog/cypher-load-json-from-url/">http://neo4j.
 *      com/blog/cypher-load-json-from-url/</a>
 */
public class AttributeBasedJsonTransformer implements JsonTransformer<String> {

  /**
   * @see it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformer#transform(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  @Override
  public String transform(JsonDocument jsonDocument) throws JsonParseException, JsonMappingException, IOException {

    Map<String, Object> documentMap = new ObjectMapper().readValue(jsonDocument.getContent(),
        new TypeReference<Map<String, Object>>() {
        });

    return transform(jsonDocument.getId(), jsonDocument.getType(), documentMap);
  }

  /**
   * It recursively parses a <code>Map</code> representation of the JSON
   * document.
   * 
   * @param documentId
   * @param documentType
   * @param documentMap
   * @return
   */
  @SuppressWarnings("unchecked")
  public String transform(String documentId, String documentType, Map<String, Object> documentMap) {

    StringBuffer rootNode = new StringBuffer();
    List<String> childNodes = new ArrayList<String>();
    List<String> childRelationships = new ArrayList<String>();

    String nodeReference = (documentType != null) ? StringUtils.lowerCase(documentType) : "document";
    String nodeLabel = StringUtils.capitalize(nodeReference);

    rootNode.append("CREATE (").append(nodeReference).append(":").append(nodeLabel);

    boolean firstAttr = true;

    for (String attributeName : documentMap.keySet()) {

      Object attributeValue = documentMap.get(attributeName);

      if (attributeValue instanceof Map) {

        childNodes.add(transform(documentId, attributeName, (Map<String, Object>) attributeValue));

        childRelationships.add(new StringBuffer().append("CREATE (").append(nodeReference).append(")-[").append(":")
            .append(nodeReference.toUpperCase()).append("_").append(attributeName.toUpperCase()).append("]->(")
            .append(attributeName).append(")").toString());
      } else {

        if (firstAttr) {
          rootNode.append(" { ");

          if (documentId != null) {
            rootNode.append("_documentId: '").append(documentId).append("', ");
          }

          firstAttr = false;
        } else {
          rootNode.append(", ");
        }

        if (attributeValue != null) {
          rootNode.append(attributeName).append(": ");

          if (attributeValue instanceof String) {
            rootNode.append("'").append(attributeValue).append("'");
          } else {
            rootNode.append(attributeValue);
          }
        }
      }
    }

    rootNode.append(" })");

    StringBuffer cypher = new StringBuffer();

    cypher.append(rootNode);

    for (String childNode : childNodes) {

      cypher.append("\n").append(childNode);
    }

    for (String childRelationship : childRelationships) {

      cypher.append("\n").append(childRelationship);
    }

    return cypher.toString();
  }
}
