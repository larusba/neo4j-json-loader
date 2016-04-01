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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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

/**
 * @author Riccardo Birello
 */
public class NodeJsonTransformer implements JsonTransformer<List<String>> {

    /*
     * (non-Javadoc)
     * 
     * @see it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformer#
     * transform(it.larusba.integration.neo4j.jsonloader.bean.JsonDocument)
     */
    @Override
    public List<String> transform(JsonDocument jsonDocument) throws JsonParseException, JsonMappingException, IOException {
        Map<String, Object> documentMap = new ObjectMapper().readValue(jsonDocument.getContent(), new TypeReference<Map<String, Object>>() {
        });
        JsonObjectDescriptorHelper jsonObjectDescriptorHelper = new JsonObjectDescriptorHelper(jsonDocument.getObjectDescriptors());
        Node node = transform(jsonDocument.getId(), jsonDocument.getType(), documentMap, jsonObjectDescriptorHelper);
        return getAllStatements(Arrays.asList(node));
    }

    /**
     * The method gets all statements, ordering first the nodes statements e
     * after the relations.
     * 
     * @param nodes
     *            the node's list
     * @return the ordered statements list
     */
    private List<String> getAllStatements(List<Node> nodes) {
        Set<String> nodesSet = getAllNodeStatements(nodes);
        Set<String> nodesRelationsSet = getAllRelationsStatements(nodes);
        ArrayList<String> result = new ArrayList<>(nodesSet);
        result.addAll(nodesRelationsSet);
        return result;
    }

    /**
     * The method get all node's statements.
     * 
     * @param nodes
     *            the nodes list
     * @return the statements
     */
    private Set<String> getAllNodeStatements(List<Node> nodes) {
        Set<String> nodesSet = new HashSet<>();
        for (Node node : nodes) {
            nodesSet.add(CypherGenerator.generateNodeStatement(node));
            nodesSet.addAll(getAllNodeStatements(node.getOutgoingRelations()));
        }
        return nodesSet;
    }

    /**
     * The method get all relations statements.
     * 
     * @param nodes
     *            the nodes list
     * @return the statements
     */
    private Set<String> getAllRelationsStatements(List<Node> nodes) {
        Set<String> nodesRelationsSet = new HashSet<>();
        for (Node node : nodes) {
            Set<String> stringOutgoingRelations = CypherGenerator.generateOutgoingRelationsStatements(node);
            if (stringOutgoingRelations != null) {
                nodesRelationsSet.addAll(stringOutgoingRelations);
            }
            nodesRelationsSet.addAll(getAllRelationsStatements(node.getOutgoingRelations()));
        }
        return nodesRelationsSet;
    }

    /**
     * The method transforms the JSON map.
     * 
     * @param documentId
     *            the document ID
     * @param documentType
     *            the type of the document
     * @param documentMap
     *            the document's map
     * @param objectDescriptorHelper
     *            the descriptor helper
     * @return the node
     */
    private Node transform(String documentId, String documentType, Map<String, Object> documentMap, JsonObjectDescriptorHelper objectDescriptorHelper) {
        String nodeLabel = buildNodeLabel(documentType, documentMap, objectDescriptorHelper);
        String nodeName = nodeLabel.toLowerCase(Locale.ITALY);
        Node node = new Node(documentId, nodeName, nodeLabel);
        for (String attributeName : documentMap.keySet()) {
            Object attributeValue = documentMap.get(attributeName);
            if (attributeValue instanceof Map) {
                handleMap(documentId, objectDescriptorHelper, node, attributeName, attributeValue);
            } else if (attributeValue instanceof List) {
                handleList(documentId, objectDescriptorHelper, node, attributeName, attributeValue);
            } else {
                handleSimpleAttribute(objectDescriptorHelper, node, attributeName, attributeValue);
            }
        }
        // FIXME is it correct to take the absolute hash?
        int hashCode = Math.abs(node.hashCode());
        node.setName(node.getName() + hashCode);
        return node;
    }

    /**
     * The method handles a simple attribute.
     * 
     * @param objectDescriptorHelper
     *            the descriptor helper
     * @param node
     *            the current node
     * @param attributeName
     *            the attribute name
     * @param attributeValue
     *            the attribute value
     */
    private void handleSimpleAttribute(JsonObjectDescriptorHelper objectDescriptorHelper, Node node, String attributeName, Object attributeValue) {
        if (objectDescriptorHelper.isAttributeInUniqueKey(node.getLabel(), attributeName)) {
            node.addKey(attributeName, (String) attributeValue);
        } else {
            node.addAttribute(attributeName, attributeValue);
        }
    }

    /**
     * The method handleList.
     * 
     * @param documentId
     *            the document ID
     * @param objectDescriptorHelper
     *            the descriptor helper
     * @param node
     *            the current node
     * @param attributeName
     *            the attribute name
     * @param attributeValue
     *            the attribute value
     */
    @SuppressWarnings("unchecked")
    private void handleList(String documentId, JsonObjectDescriptorHelper objectDescriptorHelper, Node node, String attributeName, Object attributeValue) {
        List<Object> list = (List<Object>) attributeValue;
        if (!list.isEmpty()) {
            for (Object object : list) {
                if (object instanceof Map) {
                    handleMap(documentId, objectDescriptorHelper, node, attributeName, object);
                } else {
                    node.addListAttribute(attributeName, (String) object);
                }
            }
        }
    }

    /**
     * The method handleMap.
     * 
     * @param documentId
     *            the document ID
     * @param objectDescriptorHelper
     *            the descriptor helper
     * @param node
     *            the current node
     * @param attributeName
     *            the attribute name
     * @param attributeValue
     *            the attribute value
     */
    @SuppressWarnings("unchecked")
    private void handleMap(String documentId, JsonObjectDescriptorHelper objectDescriptorHelper, Node node, String attributeName, Object attributeValue) {
        String type = buildNodeLabel(attributeName, (Map<String, Object>) attributeValue, objectDescriptorHelper);
        node.addOutgoingRelation(transform(documentId, type, (Map<String, Object>) attributeValue, objectDescriptorHelper));
    }

    /**
     * The method builds the label of the node.
     * 
     * @param documentType
     *            the type of the document
     * @param documentMap
     *            the document's map
     * @param objectDescriptorHelper
     *            the descriptor helper
     * @return the label of the node
     */
    private String buildNodeLabel(String documentType, Map<String, Object> documentMap, JsonObjectDescriptorHelper objectDescriptorHelper) {
        String typeAttribute = (String) documentMap.get(objectDescriptorHelper.getTypeAttribute(documentType));
        if (StringUtils.isBlank(typeAttribute)) {
            typeAttribute = StringUtils.lowerCase(documentType);
        }
        return StringUtils.capitalize(typeAttribute);
    }
}
