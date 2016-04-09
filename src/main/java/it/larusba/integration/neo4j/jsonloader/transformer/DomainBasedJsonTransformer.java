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

import it.larusba.integration.common.document.bean.JsonDocument;
import it.larusba.integration.neo4j.jsonloader.bean.DocumentNode;
import it.larusba.integration.neo4j.jsonloader.util.JsonObjectDescriptorHelper;

public class DomainBasedJsonTransformer implements JsonTransformer<Set<String>> {

	@Override
	public Set<String> transform(JsonDocument jsonDocument)
			throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> documentMap = new ObjectMapper().readValue(jsonDocument.getContent(),
				new TypeReference<Map<String, Object>>() {
				});

		JsonObjectDescriptorHelper jsonObjectDescriptorHelper = new JsonObjectDescriptorHelper(
				jsonDocument.getObjectDescriptors());

		DocumentNode documentNode = transform(jsonDocument.getId(), jsonDocument.getType(), documentMap, jsonObjectDescriptorHelper,
				null);

		return getAllStatements(Arrays.asList(documentNode));
	}

	public Set<String> getAllStatements(List<DocumentNode> documentNodes) {
		Set<String> response = new HashSet<>();
		for (DocumentNode documentNode : documentNodes) {
			response.add(documentNode.toString());
			Set<String> stringOutcomingRelations = documentNode.toStringOutcomingRelations();
			if (stringOutcomingRelations != null) {
				response.addAll(stringOutcomingRelations);
			}
			response.addAll(getAllStatements(documentNode.getOutcomingRelations()));
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	private DocumentNode transform(String documentId, String documentType, Map<String, Object> documentMap,
			JsonObjectDescriptorHelper objectDescriptorHelper, Integer position) {

		String nodeLabel = buildNodeLabel(documentType, documentMap, objectDescriptorHelper);
		String nodeReference = buildNodeReference(position, nodeLabel);

		DocumentNode documentNode = new DocumentNode(documentId, nodeReference, nodeLabel);

		for (String attributeName : documentMap.keySet()) {
			Object attributeValue = documentMap.get(attributeName);
			if (attributeValue instanceof Map) {
				String type = buildNodeLabel(attributeName, (Map<String, Object>) attributeValue,
						objectDescriptorHelper);
				documentNode.addOutcomingRelation(transform(documentId, type, (Map<String, Object>) attributeValue,
						objectDescriptorHelper, null));
			} else if (attributeValue instanceof List) {
				List<Object> list = (List<Object>) attributeValue;
				if (!list.isEmpty()) {
					for (Object object : list) {
						if (object instanceof Map) {
							String type = buildNodeLabel(attributeName, (Map<String, Object>) object,
									objectDescriptorHelper);
							documentNode.addOutcomingRelation(transform(documentId, type, (Map<String, Object>) object,
									objectDescriptorHelper, null));
						} else {
							documentNode.addListAttribute(attributeName, (String) object);
						}
					}
				}
			} else {
				if (objectDescriptorHelper.isAttributeInUniqueKey(nodeLabel, attributeName)) {
					documentNode.addKey(attributeName, (String) attributeValue);
				} else {
					documentNode.addAttribute(attributeName, (String) attributeValue);
				}
			}
		}
		System.out.println(documentNode.toString());
		System.out.println(documentNode.toStringOutcomingRelations());
		return documentNode;
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
