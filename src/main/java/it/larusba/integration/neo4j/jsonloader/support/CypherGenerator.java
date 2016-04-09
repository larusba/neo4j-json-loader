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
package it.larusba.integration.neo4j.jsonloader.support;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import it.larusba.integration.neo4j.jsonloader.bean.DocumentNode;

/**
 * The Cypher generator class starting from nodes.
 * 
 * @author Riccardo Birello
 */
public final class CypherGenerator {

    /**
     * The method generates a node statement.
     * 
     * @param node
     *            the current node
     * @return the statement
     */
    public static String generateNodeStatement(DocumentNode node) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("MERGE (").append(node.getName()).append(":").append(node.getLabel());
        if (node.getKeys().isEmpty()) {
            if (node.getAttributes().isEmpty()) {
                throw new IllegalStateException("The node cannot be completely empty");
            } else {
                node.getKeys().putAll(node.getAttributes());
                node.getAttributes().clear();
            }
        }
        buffer.append(buildAttributesMap(node.getKeys()));
        buffer.append(")");

        buffer.append(System.lineSeparator());
        buffer.append(String.format("SET %s.documentIds=coalesce(filter(x in %s.documentIds where x <> '%s'), []) + ['%s']", node.getName(), node.getName(), node.getDocumentId(), node.getDocumentId()));

        if (!node.getAttributes().isEmpty()) {
            buffer.append(", ");
            for (String key : node.getAttributes().keySet()) {
                Object value = node.getAttributes().get(key);
                buffer.append(node.getName()).append(".").append(key).append("='").append(value).append("', ");
            }
            buffer.delete(buffer.length() - 2, buffer.length());
        }
        if (!node.getListAttributes().isEmpty()) {
            buffer.append(", ");
            for (String key : node.getListAttributes().keySet()) {
                List<Object> values = node.getListAttributes().get(key);
                buffer.append(node.getName()).append(".").append(key).append("=").append(buildArrayString(values));
            }
        }
        return buffer.toString();
    }

    /**
     * The method builds a string from an array of strings.
     * 
     * @param list
     *            the array of strings
     * @return the formatted string
     */
    private static String buildArrayString(List<Object> list) {
        StringBuffer docBuffer = new StringBuffer();
        docBuffer.append("[");
        for (Object documentId : list) {
            docBuffer.append("'").append(documentId).append("', ");
        }
        docBuffer.delete(docBuffer.length() - 2, docBuffer.length());
        docBuffer.append("]");
        return docBuffer.toString();
    }

    /**
     * The method builds a string from the attributes map.
     * 
     * @param map
     *            the attributes map
     * @return the formatted string
     */
    private static String buildAttributesMap(Map<String, Object> map) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        for (String key : map.keySet()) {
            Object value = map.get(key);
            buffer.append(key).append(":'").append(value).append("', ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * The method generates the outgoing relations statements.
     * 
     * @param node
     *            the current node
     * @return the outgoing relations statements
     */
    public static Set<String> generateOutgoingRelationsStatements(DocumentNode node) {
        Set<String> response = null;
        if (!node.getOutgoingRelations().isEmpty()) {
            response = new HashSet<>();
            for (DocumentNode currentNode : node.getOutgoingRelations()) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(node.getLabel()).append("_").append(currentNode.getLabel());
                String relationName = buffer.toString().toUpperCase(Locale.ITALY);
                response.add(String.format("MERGE (%s)-[:%s]->(%s)", node.getName(), relationName, currentNode.getName()));
            }
        }
        return response;
    }

}
