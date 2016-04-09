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
package it.larusba.integration.neo4j.jsonloader.service;

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.core.Context;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.larusba.integration.common.document.bean.JsonDocument;
import it.larusba.integration.neo4j.jsonloader.bean.JsonLoaderStatistics;
import it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformer;
import it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformerFactory;

/**
 *
 * @author Lorenzo Speranzoni
 */
public class DefaultJsonLoaderService implements JsonLoaderService {

  private static Logger LOGGER = LoggerFactory.getLogger(DefaultJsonLoaderService.class);

  @Context
  private GraphDatabaseService graphDatabaseService;

  public DefaultJsonLoaderService(GraphDatabaseService graphDatabaseService) {
    this.graphDatabaseService = graphDatabaseService;
  }

  /**
   * @see it.larusba.integration.neo4j.jsonloader.service.JsonLoaderService#setGraphDatabaseService(org.neo4j.graphdb.GraphDatabaseService)
   */
  @Override
  public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
    this.graphDatabaseService = graphDatabaseService;
  }

  /**
   * @see it.larusba.integration.neo4j.jsonloader.service.JsonLoaderService#save(it.larusba.integration.neo4j.jsonloader.bean.JsonDocumentTest)
   */
  @Override
  public JsonLoaderStatistics save(JsonDocument jsonDocument)
      throws JsonParseException, JsonMappingException, IOException {

    JsonLoaderStatistics jsonLoaderStatistics = null;

    if (this.graphDatabaseService == null || !this.graphDatabaseService.isAvailable(10))
      throw new IllegalStateException("Database connection not available.");

    JsonTransformer<Set<String>> jsonTransformer = JsonTransformerFactory.getInstance(jsonDocument.getMappingStrategy());

    String cypher = jsonTransformer.transform(jsonDocument).toArray(new String[] {})[0];

    LOGGER.info("Cypher statement:");
    LOGGER.info("\n" + cypher);

    try (Transaction tx = this.graphDatabaseService.beginTx()) {

      Result result = this.graphDatabaseService.execute(cypher);

      jsonLoaderStatistics = new JsonLoaderStatistics(result.getQueryStatistics());

      tx.success();
    }

    return jsonLoaderStatistics;
  }
}
