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
 * This class generates a cypher equivalent representation from the provided input node
 * 
 * @author Riccardo Birello
 */
public final class CypherGenerator {

	/**
	 * The method generates a node statement.
	 * 
	 * @param node
	 *          current node to be translated into its cypher representation
	 * @return the statement
	 */
	public static String generateNodeStatement(DocumentNode node) {

		StringBuffer buffer = new StringBuffer();

		buffer.append("MERGE (");
		buffer.append(node.getName());
		buffer.append(":");
		buffer.append(node.getLabel());
		buffer.append(buildUniqueKey(node));
		buffer.append(")");
		buffer.append(System.lineSeparator());
		buffer.append("SET ");
		buffer.append(buildDocumentIdProperty(node));
		buffer.append(buildPrimitiveProperties(node));
		buffer.append(buildArrayProperties(node));

		return buffer.toString();
	}

	/**
	 * The method generates the outgoing relations statements.
	 * 
	 * @param node
	 *          current node to be translated into its cypher representation
	 * @return the outgoing relations statements
	 */
	public static Set<String> generateOutgoingRelationsStatements(DocumentNode node) {
		
		Set<String> response = null;
		
		if (!node.getOutgoingRelations().isEmpty()) {
			response = new HashSet<>();
			for (DocumentNode currentNode : node.getOutgoingRelations()) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(node.getType().toUpperCase(Locale.ITALY)).append("_").append(currentNode.getLabel());
				String relationName = buffer.toString().toUpperCase(Locale.ITALY);
				response.add(String.format("MERGE (%s)-[:%s]->(%s)", node.getName(), relationName, currentNode.getName()));
			}
		}
		
		return response;
	}

	/**
	 * The method builds a string from the attributes map.
	 * 
	 * @param node
	 *          current node to be translated into its cypher representation
	 * @return the formatted string
	 */
	private static String buildUniqueKey(DocumentNode node) {
	
		StringBuffer buffer = new StringBuffer();
		Map<String, Object> keys = node.getKeys();
		if (keys.isEmpty()) {
			if (node.getAttributes().isEmpty()) {
				throw new IllegalStateException("The node cannot be completely empty");
			} else {
				node.getKeys().putAll(node.getAttributes());
				node.getAttributes().clear();
			}
		}
		
		buffer.append(" {");

		for (String key : node.getKeys().keySet()) {
			Object value = node.getKeys().get(key);
			if (value != null) {
				if (value instanceof String)
					buffer.append(key).append(": '").append(((String) value).replace("'", "\\'")).append("', ");
				else
					buffer.append(key).append(": ").append(value).append(", ");
			}
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		
		buffer.append("}");
	
		return buffer.toString();
	}

	/**
	 * @param node
	 *          current node to be translated into its cypher representation
	 * @return
	 */
	private static String buildDocumentIdProperty(DocumentNode node) {
		return String.format("%s.documentIds = coalesce(filter(x in %s.documentIds where x <> '%s'), []) + ['%s']",
		    node.getName(), node.getName(), node.getDocumentId(), node.getDocumentId());
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	private static StringBuffer buildPrimitiveProperties(DocumentNode node) {
	
		StringBuffer buffer = new StringBuffer();
	
		if (!node.getAttributes().isEmpty()) {
			buffer.append(", ");
			for (String key : node.getAttributes().keySet()) {
				Object value = node.getAttributes().get(key);
				if (value != null) {
					if (value instanceof String) {
						buffer.append(node.getName()).append(".").append(key).append(" = '")
						    .append(((String) value).replace("'", "\\'")).append("', ");
					} else
						buffer.append(node.getName()).append(".").append(key).append(" = ").append(value).append(", ");
				}
			}
			buffer.delete(buffer.length() - 2, buffer.length());
		}
	
		return buffer;
	}

	/**
	 * @param node
	 *          current node to be translated into its cypher representation
	 * @return
	 */
	private static StringBuffer buildArrayProperties(DocumentNode node) {
	
		StringBuffer buffer = new StringBuffer();
	
		if (!node.getListAttributes().isEmpty()) {
			buffer.append(", ");
			for (String key : node.getListAttributes().keySet()) {
				List<Object> values = node.getListAttributes().get(key);
				buffer.append(node.getName()).append(".").append(key).append(" = ").append(buildArrayString(values));
			}
		}
	
		return buffer;
	}

	/**
	 * The method builds a string from an array of strings.
	 * 
	 * @param elements
	 *          the array of strings
	 * @return the formatted string
	 */
	private static StringBuffer buildArrayString(List<Object> elements) {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("[");
		
		for (Object element : elements) {
			if (element instanceof String)
				buffer.append("'").append(((String) element).replace("'", "\\'")).append("', ");
			else
				buffer.append(element).append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		
		buffer.append("]");
		
		return buffer;
	}

}
