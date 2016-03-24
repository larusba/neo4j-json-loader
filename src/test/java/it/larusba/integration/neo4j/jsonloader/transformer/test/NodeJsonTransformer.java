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
package it.larusba.integration.neo4j.jsonloader.transformer.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import it.larusba.integration.neo4j.jsonloader.bean.JsonDocument;
import it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformer;
import it.larusba.integration.neo4j.jsonloader.util.JsonObjectDescriptorHelper;

public class NodeJsonTransformer implements JsonTransformer<Set<String>> {

	@Override
	public Set<String> transform(JsonDocument jsonDocument)
			throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> documentMap = new ObjectMapper().readValue(jsonDocument.getContent(),
				new TypeReference<Map<String, Object>>() {
				});

		JsonObjectDescriptorHelper jsonObjectDescriptorHelper = new JsonObjectDescriptorHelper(
				jsonDocument.getObjectDescriptors());

		Node node = transform(jsonDocument.getId(), jsonDocument.getType(), documentMap, jsonObjectDescriptorHelper,
				null);

		return getAllStatements(Arrays.asList(node));
	}

	public Set<String> getAllStatements(List<Node> nodes) {
		Set<String> response = new HashSet<>();
		for (Node node : nodes) {
			response.add(node.toString());
			Set<String> stringOutcomingRelations = node.toStringOutcomingRelations();
			if (stringOutcomingRelations != null) {
				response.addAll(stringOutcomingRelations);
			}
			response.addAll(getAllStatements(node.getOutcomingRelations()));
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	private Node transform(String documentId, String documentType, Map<String, Object> documentMap,
			JsonObjectDescriptorHelper objectDescriptorHelper, Integer position) {

		String nodeLabel = buildNodeLabel(documentType, documentMap, objectDescriptorHelper);
		String nodeReference = buildNodeReference(position, nodeLabel);

		Node node = new Node(documentId, nodeReference, nodeLabel);

		for (String attributeName : documentMap.keySet()) {
			Object attributeValue = documentMap.get(attributeName);
			if (attributeValue instanceof Map) {
				String type = buildNodeLabel(attributeName, (Map<String, Object>) attributeValue,
						objectDescriptorHelper);
				node.addOutcomingRelation(transform(documentId, type, (Map<String, Object>) attributeValue,
						objectDescriptorHelper, null));
			} else if (attributeValue instanceof List) {
				List<Object> list = (List<Object>) attributeValue;
				if (!list.isEmpty()) {
					for (Object object : list) {
						if (object instanceof Map) {
							String type = buildNodeLabel(attributeName, (Map<String, Object>) object,
									objectDescriptorHelper);
							node.addOutcomingRelation(transform(documentId, type, (Map<String, Object>) object,
									objectDescriptorHelper, null));
						} else {
							node.addListAttribute(attributeName, (String) object);
						}
					}
				}
			} else {
				if (objectDescriptorHelper.isAttributeInUniqueKey(nodeLabel, attributeName)) {
					node.addKey(attributeName, (String) attributeValue);
				} else {
					node.addAttribute(attributeName, (String) attributeValue);
				}
			}
		}
		System.out.println(node.toString());
		System.out.println(node.toStringOutcomingRelations());
		return node;
	}

	private String buildNodeReference(Integer position, String nodeLabel) {
		return nodeLabel.toLowerCase() + ((position != null) ? position : "");
	}

	private String buildNodeLabel(String documentType, Map<String, Object> documentMap,
			JsonObjectDescriptorHelper objectDescriptorHelper) {
		String typeAttribute = (String) documentMap.get(objectDescriptorHelper.getTypeAttribute(documentType));
		if (StringUtils.isBlank(typeAttribute)) {
			typeAttribute = StringUtils.lowerCase(documentType);
		}
		return StringUtils.capitalize(typeAttribute);
	}
}
