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
package it.larusba.integration.neo4j.jsonloader.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.larusba.integration.neo4j.jsonloader.bean.JsonObjectDescriptor;

/**
 *
 * @author Lorenzo Speranzoni
 */
public class JsonObjectDescriptorHelper {
	
	private List<String> entityNames;
	private Map<String, String> entityTypeAttributeMap;
	private Map<String, List<String>> entityUniqueKeyAttributesMap;
	
	public JsonObjectDescriptorHelper(List<JsonObjectDescriptor> objectDescriptors) {
		
		init(objectDescriptors);
	}

	private void init(List<JsonObjectDescriptor> objectDescriptors) {
		
		if (objectDescriptors != null) {
			
			this.entityNames = new ArrayList<String>();
			this.entityTypeAttributeMap = new HashMap<String, String>();
			this.entityUniqueKeyAttributesMap = new HashMap<String, List<String>>();
			
			for (JsonObjectDescriptor jsonObjectDescriptor : objectDescriptors) {
				
				String entityName = jsonObjectDescriptor.getEntityName();
				
				this.entityNames.add(entityName);
				this.entityTypeAttributeMap.put(entityName, jsonObjectDescriptor.getTypeAttribute());
				this.entityUniqueKeyAttributesMap.put(entityName, jsonObjectDescriptor.getUniqueKeyAttributes());
			}
		}
	}
	
	public List<String> getEntityNames() {
		return entityNames;
	}
	
	public String getTypeAttribute(String entityName) {
		return this.entityTypeAttributeMap.get(entityName);
	}
	
	public List<String> getUniqueKeyAttributes(String entityName) {
		return this.entityUniqueKeyAttributesMap.get(entityName);
	}
	
	public Map<String, String> getEntityTypeAttributeMap() {
		return entityTypeAttributeMap;
	}
	
	public Map<String, List<String>> getEntityUniqueKeyAttributesMap() {
		return entityUniqueKeyAttributesMap;
	}
}
