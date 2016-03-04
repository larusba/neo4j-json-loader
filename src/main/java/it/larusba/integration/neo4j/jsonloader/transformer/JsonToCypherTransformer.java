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

/**
 * {@link JsonTransformer} implementation converting a JSON document into a
 * Cypher statement
 * 
 * @author Lorenzo Speranzoni
 * 
 * @see <a href="http://neo4j.com/blog/cypher-load-json-from-url/">http://neo4j.
 *      com/blog/cypher-load-json-from-url/</a>
 */
public class JsonToCypherTransformer implements JsonTransformer<String> {

	/**
	 * @see it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformer#transform(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String transform(String documentType, String jsonDocument)
			throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> documentMap = new ObjectMapper().readValue(jsonDocument,
				new TypeReference<Map<String, Object>>() {
				});

		return transform(documentType, documentMap);
	}

	/**
	 * @see it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformer#transform(java.lang.String,
	 *      java.util.Map)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String transform(String documentType, Map<String, Object> documentMap) {

		StringBuffer rootNode = new StringBuffer();
		List<String> childNodes = new ArrayList<String>();
		List<String> childRelationships = new ArrayList<String>();

		String documentKey = "documentKey";

		rootNode.append("MERGE (").append(documentType).append(":").append(StringUtils.capitalize(documentType))
				.append(" { couchbaseId: '").append(documentKey).append("' })\n");

		boolean firstAttr = true;

		for (String attributeName : documentMap.keySet()) {

			Object attributeValue = documentMap.get(attributeName);

			if (attributeValue instanceof Map) {

				childNodes.add(transform(attributeName, (Map<String, Object>) attributeValue));

				childRelationships.add(new StringBuffer().append("MERGE (").append(documentType).append(")-[")
						.append(":").append(documentType.toUpperCase()).append("_").append(attributeName.toUpperCase())
						.append("]->(").append(attributeName).append(")").toString());
			} else {

				if (firstAttr) {
					rootNode.append("ON CREATE SET ");
					firstAttr = false;
				} else {
					rootNode.append(", ");
				}

				rootNode.append(documentType).append(".").append(attributeName).append(" = ");

				if (attributeValue instanceof String) {
					rootNode.append("'").append(attributeValue).append("'");
				} else {
					rootNode.append(attributeValue);
				}
			}
		}

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
