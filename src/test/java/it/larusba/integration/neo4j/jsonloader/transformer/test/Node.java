
package it.larusba.integration.neo4j.jsonloader.transformer.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Node {

	private String name;
	private String label;
	private List<String> documentIds;
	private Map<String, String> keys;
	private Map<String, String> attributes;
	private Map<String, List<String>> listAttributes;
	private List<Node> outgoingRelations;

	public Node() {
		this.keys = new HashMap<>();
		this.attributes = new HashMap<>();
		this.listAttributes = new HashMap<>();
		this.outgoingRelations = new ArrayList<>();
		this.documentIds = new ArrayList<>();
	}

	public Node(String documentId, String name, String label) {
		this();
		addDocumentId(documentId);
		this.name = name;
		this.label = label;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("MERGE (").append(this.name).append(":").append(this.label);
		if (this.keys.isEmpty()) {
			if (this.attributes.isEmpty()) {
				throw new IllegalStateException("The node cannot be completely empty");
			} else {
				this.keys.putAll(this.attributes);
				this.attributes.clear();
			}
		}
		buffer.append(buildAttributesString(this.keys));
		buffer.append(")");

		String ids = buildArrayString(this.documentIds);

		buffer.append(System.lineSeparator());
		buffer.append(String.format("SET %s.documentIds=%s.documentIds + %s", this.name, this.name, ids));
//		buffer.append(String.format("SET %s.documentIds=filter(x in %s.documentIds | x <> %s) + %s", this.name, this.name, ids, ids));
		if (!this.attributes.isEmpty()) {
			buffer.append(", ");
			for (String key : this.attributes.keySet()) {
				String value = this.attributes.get(key);
				buffer.append(this.name).append(".").append(key).append("='").append(value).append("', ");
			}
			buffer.delete(buffer.length() - 2, buffer.length());
		}
		if (!this.listAttributes.isEmpty()) {
			if (!this.attributes.isEmpty()) {
				buffer.append(", ");
			}
			for (String key : this.listAttributes.keySet()) {
				List<String> values = this.listAttributes.get(key);
				buffer.append(this.name).append(".").append(key).append("=").append(buildArrayString(values));
			}
		}
		return buffer.toString();
	}

	private String buildArrayString(List<String> list) {
		StringBuffer docBuffer = new StringBuffer();
		docBuffer.append("[");
		for (String documentId : list) {
			docBuffer.append("'").append(documentId).append("', ");
		}
		docBuffer.delete(docBuffer.length() - 2, docBuffer.length());
		docBuffer.append("]");
		return docBuffer.toString();
	}

	private String buildAttributesString(Map<String, String> map) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		for (String key : map.keySet()) {
			String value = map.get(key);
			buffer.append(key).append(":'").append(value).append("', ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}");
		return buffer.toString();
	}

	/*
	 * MERGE (tracks)-[:TRACKS_ITEMS]->(items0)
	 */
	public Set<String> toStringOutcomingRelations() {
		Set<String> response = null;
		if (!this.outgoingRelations.isEmpty()) {
			response = new HashSet<>();
			for (Node node : this.outgoingRelations) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(this.label).append("_").append(node.getLabel());
				String relationName = buffer.toString().toUpperCase(Locale.ITALY);
				response.add(String.format("MERGE (%s)-[:%s]->(%s)", this.name, relationName, node.getName()));
			}
		}
		return response;
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

	public void addOutgoingRelation(Node node) {
		this.outgoingRelations.add(node);
	}

	public void addDocumentId(String documentId) {
		if (this.documentIds == null) {
			this.documentIds = new ArrayList<>();
		}
		this.documentIds.add(documentId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<String> documentIds) {
		this.documentIds = documentIds;
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

	public Map<String, List<String>> getListAttributes() {
		return listAttributes;
	}

	public void setListAttributes(Map<String, List<String>> listAttributes) {
		this.listAttributes = listAttributes;
	}

	public List<Node> getOutgoingRelations() {
		return outgoingRelations;
	}

	public void setOutgoingRelations(List<Node> outgoingRelations) {
		this.outgoingRelations = outgoingRelations;
	}
}
