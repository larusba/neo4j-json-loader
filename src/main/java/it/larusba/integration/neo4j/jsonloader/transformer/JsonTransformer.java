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
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * Tranformer for JSON Document.
 * 
 * @author Lorenzo Speranzoni
 * 
 * @see JsonToCypherTransformer
 */
public interface JsonTransformer<T> {

	/**
	 * 
	 * @param documentType
	 *            the associated JSON document type
	 * @param jsonDocument
	 *            the JSON document to transform
	 * @return the generated cypher statement
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	T transform(String documentType, String jsonDocument) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * @param documentType
	 *            the associated JSON document type
	 * @param documentMap
	 *            the JSON document map representation
	 * @return the generated cypher statement
	 */
	String transform(String documentType, Map<String, Object> documentMa);
}
