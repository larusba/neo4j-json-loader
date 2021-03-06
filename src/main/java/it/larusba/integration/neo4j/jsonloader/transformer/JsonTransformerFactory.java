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

import it.larusba.integration.common.document.mapping.JsonMappingStrategy;

/**
 *
 * @author Lorenzo Speranzoni
 */
public class JsonTransformerFactory {

  public static final JsonTransformer<String> getInstance(JsonMappingStrategy graphMappingStrategy) {

    if (graphMappingStrategy == null) {
      throw new IllegalArgumentException("Mapping stategy cannot be null");
    }

    switch (graphMappingStrategy) {

    case DOMAIN_DRIVEN:
      return new DomainDrivenJsonTransformer();

    case FULLY_FLEXIBLE_BUT_NOT_YET_INVENTED:
      throw new UnsupportedOperationException();

    default:
      return new AttributeBasedJsonTransformer();

    }
  }
}
