
package it.larusba.integration.neo4j.jsonloader.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class represents a DOCUMENT node.
 * 
 * @author Riccardo Birello
 */
public class DocumentNode {

    /** The name attribute. */
    private String name;
    /** The type attribute. */
    private String type;
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
    private List<DocumentNode> outgoingRelations;
    /** The depth attribute. */
    private int depth = -1;
    /** The parentPropertyName attribute. */
    private String parentPropertyName;

    /** The constructor. */
    public DocumentNode() {
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
    public DocumentNode(String documentId, String type, String name, String label) {
        this();
        this.documentId = documentId;
        this.type = type;
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
    public void addOutgoingRelation(DocumentNode node) {
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
     * The getter of type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * The setter of type.
     * 
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
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
    public List<DocumentNode> getOutgoingRelations() {
        return outgoingRelations;
    }

    /**
     * The setter of outgoingRelations.
     * 
     * @param outgoingRelations
     *            the outgoingRelations to set
     */
    public void setOutgoingRelations(List<DocumentNode> outgoingRelations) {
        this.outgoingRelations = outgoingRelations;
    }

    /**
     * The getter of depth.
     * 
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * The setter of depth.
     * 
     * @param depth
     *            the depth to set
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * The getter of parentPropertyName.
     * 
     * @return the parentPropertyName
     */
    public String getParentPropertyName() {
        return parentPropertyName;
    }

    /**
     * The setter of parentPropertyName.
     * 
     * @param parentPropertyName
     *            the parentPropertyName to set
     */
    public void setParentPropertyName(String parentPropertyName) {
        this.parentPropertyName = parentPropertyName;
    }

    /**
     * The method gets the qualified name of the node.
     * 
     * @return the qualified name of the node
     */
    public String getQualifiedName() {
        // FIXME is it correct to take the absolute hash?
        int hashCode = Math.abs(hashCode());
        return getName() + hashCode;
    }
}
