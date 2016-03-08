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
import it.larusba.integration.neo4j.jsonloader.util.JsonObjectDescriptorHelper;

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
 * <pre>
 * Person:
 * {
 *   "firstname" : "Lorenzo",
 *   "lastname"  : "Speranzoni",
 *   "age"       : 41,
 *   "job"       :
 *   {
 *     "role"    : "CEO",
 *     "company" :
 *     {
 *       "name"    : "LARUS Business Automation",
 *       "vat"     : "03540680273",
 *       "address" :
 *       {
 *         "street"   : "Via B. Maderna, 7",
 *         "zipCode"  : 30174,
 *         "city"     : "Mestre",
 *         "province" : "Venice",
 *         "country"  : "Italy"
 *       }
 *     }  
 *   }
 * }
 * </pre>
 * trained with the following domain description:
 * <pre>
 * JsonObjectDescriptor addressObjectDescriptor = new JsonObjectDescriptor("Address", Arrays.asList("street", "zipCode")    , "type");
 * JsonObjectDescriptor companyObjectDescriptor = new JsonObjectDescriptor("Company", Arrays.asList("vat")                  , "type");
 * JsonObjectDescriptor     jobObjectDescriptor = new JsonObjectDescriptor("Job"    , Arrays.asList("role")                 , "type");
 * JsonObjectDescriptor personObjectDescriptor = new JsonObjectDescriptor("Person"  , Arrays.asList("firstname", "lastname"), "type");
 * </pre>
 * will be translated into this sub-graph:
 * <pre>
 * MERGE (person:Person { firstname: 'Lorenzo', lastname: 'Speranzoni' })
 * ON CREATE SET person._documentId = '1234567890QWERTY', person.age = 41, person.type = 'Person'
 * MERGE (job:Job { role: 'CEO' })
 * ON CREATE SET job._documentId = '1234567890QWERTY', job.type = 'Job'
 * MERGE (company:Company { vat: '03540680273' })
 * ON CREATE SET company._documentId = '1234567890QWERTY', company.name = 'LARUS Business Automation', company.type = 'Company'
 * MERGE (address:Address { street: 'Via B. Maderna, 7', zipCode: 30174 })
 * ON CREATE SET address._documentId = '1234567890QWERTY', address.city = 'Mestre', address.province = 'Venice', address.country = 'Italy'
 * CREATE (company)-[:COMPANY_ADDRESS]->(address)
 * CREATE (job)-[:JOB_COMPANY]->(company)
 * CREATE (person)-[:PERSON_JOB]->(job)
 * </pre>
 *
 * 
 * @author Lorenzo Speranzoni
 * 
 * @see <a href="http://neo4j.com/blog/cypher-load-json-from-url/">http://neo4j.
 *      com/blog/cypher-load-json-from-url/</a>
 *      
 * @since Mar 5, 2016
 */
public class DomainBasedJsonTransformer implements JsonTransformer<String> {

	/**
	 * @see it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformer#transform(it.larusba.integration.neo4j.jsonloader.bean.JsonDocument)
	 */
	@Override
	public String transform(JsonDocument jsonDocument) throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> documentMap = new ObjectMapper().readValue(jsonDocument.getContent(),
				new TypeReference<Map<String, Object>>() {
				});

		JsonObjectDescriptorHelper jsonObjectDescriptorHelper = new JsonObjectDescriptorHelper(
				jsonDocument.getObjectDescriptors());

		return transform(jsonDocument.getId(), jsonDocument.getType(), documentMap, jsonObjectDescriptorHelper);
	}

	/**
	 * TODO we still don't use <code>objectDescriptorHelper.getTypeAttribute</code> to properly set node <code>Labels</code>.
	 * <p/>
	 * @param documentId
	 * @param documentType
	 * @param documentMap
	 * @param objectDescriptors
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String transform(String documentId, String documentType, Map<String, Object> documentMap,
			JsonObjectDescriptorHelper objectDescriptorHelper) {

		StringBuffer rootNode = new StringBuffer();
		List<String> childNodes = new ArrayList<String>();
		List<String> childRelationships = new ArrayList<String>();

		String nodeReference = (documentType != null) ? StringUtils.lowerCase(documentType) : "document";
		String nodeLabel = StringUtils.capitalize(nodeReference);

		rootNode.append("MERGE (").append(nodeReference).append(":").append(nodeLabel);

		StringBuffer nodeAttributes = new StringBuffer();

		boolean firstAttr = true;
		boolean firstUniqueAttr = true;

		for (String attributeName : documentMap.keySet()) {

			Object attributeValue = documentMap.get(attributeName);

			if (attributeValue instanceof Map) {

				childNodes.add(transform(documentId, attributeName, (Map<String, Object>) attributeValue,
						objectDescriptorHelper));

				childRelationships.add(new StringBuffer().append("CREATE (").append(nodeReference).append(")-[")
						.append(":").append(nodeReference.toUpperCase()).append("_").append(attributeName.toUpperCase())
						.append("]->(").append(attributeName).append(")").toString());
			} else {

				if (objectDescriptorHelper.getUniqueKeyAttributes(nodeLabel).contains(attributeName)) {

					if (firstUniqueAttr) {
						rootNode.append(" { ");

						firstUniqueAttr = false;
					} else {
						rootNode.append(", ");
					}

					rootNode.append(attributeName).append(": ");

					if (attributeValue instanceof String) {
						rootNode.append("'").append(attributeValue).append("'");
					} else {
						rootNode.append(attributeValue);
					}

				} else {

					if (attributeValue != null) {

						if (firstAttr) {
							nodeAttributes.append("ON CREATE SET ");
							
							if (documentId != null) {
								nodeAttributes.append(nodeReference).append(".").append("_documentId = '").append(documentId).append("', ");
							}

							firstAttr = false;
						} else {
							nodeAttributes.append(", ");
						}
						
						nodeAttributes.append(nodeReference).append(".").append(attributeName).append(" = ");

						if (attributeValue instanceof String) {
							nodeAttributes.append("'").append(attributeValue).append("'");
						} else {
							nodeAttributes.append(attributeValue);
						}
					}
				}
			}
		}

		rootNode.append(" })").append("\n").append(nodeAttributes);

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
