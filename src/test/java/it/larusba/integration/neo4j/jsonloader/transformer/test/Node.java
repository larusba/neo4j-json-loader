
package it.larusba.integration.neo4j.jsonloader.transformer.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Node {

	private String label;
	private String type;
	private String documentId;
	private Map<String, String> keys;
	private Map<String, String> attributes;
	private Map<String, List<String>> listAttributes;
	private List<Node> outcomingRelations;

	public Node() {
		this.keys = new HashMap<>();
		this.attributes = new HashMap<>();
		this.listAttributes = new HashMap<>();
		this.outcomingRelations = new ArrayList<>();
	}

	public Node(String documentId, String label, String type) {
		this();
		this.documentId = documentId;
		this.label = label;
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("MERGE (").append(this.label).append(":").append(this.type);
		if (!this.keys.isEmpty()) {
			buffer.append("{");
			for (String key : this.keys.keySet()) {
				String value = this.keys.get(key);
				buffer.append(key).append("=").append("'").append(value).append("'").append(", ");
			}
			buffer.delete(buffer.length() - 2, buffer.length());
			buffer.append("}");
		}
		buffer.append(")");
		buffer.append(System.lineSeparator()).append("ON CREATE SET ").append(this.label).append(".")
				.append("documentId").append("=").append("'").append(this.documentId).append("'").append(", ");
		if (!this.attributes.isEmpty()) {
			for (String key : this.attributes.keySet()) {
				String value = this.attributes.get(key);
				buffer.append(this.label).append(".").append(key).append("=").append("'").append(value).append("'")
						.append(", ");
			}
			buffer.delete(buffer.length() - 2, buffer.length());
		}
		if (!this.listAttributes.isEmpty()) {
			if (!this.attributes.isEmpty()) {
				buffer.append(", ");
			}
			for (String key : this.listAttributes.keySet()) {
				buffer.append(this.label).append(".").append(key).append("=").append("[");
				List<String> values = this.listAttributes.get(key);
				for (String value : values) {
					buffer.append("'").append(value).append("'").append(", ");
				}
				buffer.delete(buffer.length() - 2, buffer.length());
				buffer.append("]");
			}
		}
		return buffer.toString();
	}

	/*
	 * MERGE (tracks)-[:TRACKS_ITEMS]->(items0)
	 */
	public Set<String> toStringOutcomingRelations() {
		Set<String> response = null;
		if (!this.outcomingRelations.isEmpty()) {
			response = new HashSet<>();
			for (Node node : outcomingRelations) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(this.type).append("_").append(node.getType());
				String relationName = buffer.toString().toUpperCase(Locale.ITALY);
				response.add(String.format("MERGE (%s)-[:%s]->(%s)", this.label, relationName, node.getLabel()));
			}
		}
		return response;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getKeys() {
		return keys;
	}

	public void setKeys(Map<String, String> keys) {
		this.keys = keys;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public List<Node> getOutcomingRelations() {
		return outcomingRelations;
	}

	public void setOutcomingRelations(List<Node> outcomingRelations) {
		this.outcomingRelations = outcomingRelations;
	}

	public void addKey(String key, String value) {
		this.keys.put(key, value);
	}

	public void addAttribute(String key, String value) {
		this.attributes.put(key, value);
	}

	public void addListAttribute(String key, String value) {
		List<String> list = this.listAttributes.get(key);
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(value);
		this.listAttributes.put(key, list);
	}

	public void addOutcomingRelation(Node node) {
		this.outcomingRelations.add(node);
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
}
