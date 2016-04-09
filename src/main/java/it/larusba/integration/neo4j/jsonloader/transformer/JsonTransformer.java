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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import it.larusba.integration.common.document.bean.JsonDocument;

/**
 * Tranformer for JSON Document.
 * 
 * @author Lorenzo Speranzoni
 * 
 * @see AttributeBasedJsonTransformer
 */
public interface JsonTransformer<T> {

  /**
   * @param jsonDocument
   *          the object wrapper containing all the directives to trasform the
   *          embedded JSON document
   * @return the generated cypher statement
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  T transform(JsonDocument jsonDocument) throws JsonParseException, JsonMappingException, IOException;
}
