
package it.larusba.integration.neo4j.jsonloader.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

<<<<<<< HEAD:src/main/java/it/larusba/integration/neo4j/jsonloader/bean/DocumentNode.java
public class DocumentNode {

	private String documentId;

	private String label;
	private String type;
	
	private Map<String, String> keys;
	private Map<String, String> attributes;
	private Map<String, List<String>> listAttributes;
	
	private List<DocumentNode> outcomingRelations;

	public DocumentNode() {
		this.keys = new HashMap<>();
		this.attributes = new HashMap<>();
		this.listAttributes = new HashMap<>();
		this.outcomingRelations = new ArrayList<>();
	}

	public DocumentNode(String documentId, String label, String type) {
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
			for (DocumentNode documentNode : outcomingRelations) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(this.type).append("_").append(documentNode.getType());
				String relationName = buffer.toString().toUpperCase(Locale.ITALY);
				response.add(String.format("MERGE (%s)-[:%s]->(%s)", this.label, relationName, documentNode.getLabel()));
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

	public List<DocumentNode> getOutcomingRelations() {
		return outcomingRelations;
	}

	public void setOutcomingRelations(List<DocumentNode> outcomingRelations) {
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

	public void addOutcomingRelation(DocumentNode documentNode) {
		this.outcomingRelations.add(documentNode);
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
=======
/**
 * The class represents a JSON node with all informations.
 * 
 * @author Riccardo Birello
 */
public class Node {

    /** The name attribute. */
    private String name;
    /** The label attribute. */
    private String label;
    /** The documentId attribute. */
    private String documentId;
    /** The keys attribute. */
    private Map<String, Object> keys;
    /** The attributes attribute. */
    private Map<String, Object> attributes;
    /** The listAttributes attribute. */
    private Map<String, List<Object>> listAttributes;
    /** The outgoingRelations attribute. */
    private List<Node> outgoingRelations;

    /** The constructor. */
    public Node() {
        this.keys = new HashMap<>();
        this.attributes = new HashMap<>();
        this.listAttributes = new HashMap<>();
        this.outgoingRelations = new ArrayList<>();
    }

    /**
     * The constructor.
     * 
     * @param documentId
     *            the document ID
     * @param name
     *            the name of the node
     * @param label
     *            the label of the node
     */
    public Node(String documentId, String name, String label) {
        this();
        this.documentId = documentId;
        this.name = name;
        this.label = label;
    }

    /**
     * The method adds a key attribute to node.
     * 
     * @param key
     *            the key of the attribute
     * @param value
     *            the value of the attribute
     */
    public void addKey(String key, Object value) {
        this.keys.put(key, value);
    }

    /**
     * The method adds an attribute.
     * 
     * @param key
     *            the key of the attribute
     * @param value
     *            the value of the attribute
     */
    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * The method adds a list attribute to the node.
     * 
     * @param key
     *            the key of the attribute
     * @param value
     *            the value to add
     */
    public void addListAttribute(String key, Object value) {
        List<Object> list = this.listAttributes.get(key);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(value);
        this.listAttributes.put(key, list);
    }

    /**
     * The method adds an outgoing relation.
     * 
     * @param node
     *            the node to add
     */
    public void addOutgoingRelation(Node node) {
        this.outgoingRelations.add(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + ((keys == null) ? 0 : keys.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((listAttributes == null) ? 0 : listAttributes.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * The getter of name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * The setter of name.
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The getter of label.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * The setter of label.
     * 
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * The getter of documentId.
     * 
     * @return the documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * The setter of documentId.
     * 
     * @param documentId
     *            the documentId to set
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * The getter of keys.
     * 
     * @return the keys
     */
    public Map<String, Object> getKeys() {
        return keys;
    }

    /**
     * The setter of keys.
     * 
     * @param keys
     *            the keys to set
     */
    public void setKeys(Map<String, Object> keys) {
        this.keys = keys;
    }

    /**
     * The getter of attributes.
     * 
     * @return the attributes
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * The setter of attributes.
     * 
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * The getter of listAttributes.
     * 
     * @return the listAttributes
     */
    public Map<String, List<Object>> getListAttributes() {
        return listAttributes;
    }

    /**
     * The setter of listAttributes.
     * 
     * @param listAttributes
     *            the listAttributes to set
     */
    public void setListAttributes(Map<String, List<Object>> listAttributes) {
        this.listAttributes = listAttributes;
    }

    /**
     * The getter of outgoingRelations.
     * 
     * @return the outgoingRelations
     */
    public List<Node> getOutgoingRelations() {
        return outgoingRelations;
    }

    /**
     * The setter of outgoingRelations.
     * 
     * @param outgoingRelations
     *            the outgoingRelations to set
     */
    public void setOutgoingRelations(List<Node> outgoingRelations) {
        this.outgoingRelations = outgoingRelations;
    }
>>>>>>> d2b2d86fe940c26ea9a53cce6d6975aed127ab08:src/test/java/it/larusba/integration/neo4j/jsonloader/transformer/test/Node.java
}
