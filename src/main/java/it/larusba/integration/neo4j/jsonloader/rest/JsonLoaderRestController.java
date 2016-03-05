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
package it.larusba.integration.neo4j.jsonloader.rest;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.larusba.integration.neo4j.jsonloader.bean.JsonDocument;
import it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformer;
import it.larusba.integration.neo4j.jsonloader.transformer.JsonTransformerFactory;

/**
 * @author Lorenzo Speranzoni
 * @since Mar 3, 2016
 */

@Path("/")
public class JsonLoaderRestController {

	private static Logger LOGGER = LoggerFactory.getLogger(JsonLoaderRestController.class);

	@PUT
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response loadJSON(JAXBElement<JsonDocument> jsonDocumentWrapper) {

		try {

			JsonDocument jsonDocument = jsonDocumentWrapper.getValue();
			
			LOGGER.info("PUT /");
			LOGGER.info("");
			LOGGER.info("document id: " + jsonDocument.getId());
			LOGGER.info("document type: " + jsonDocument.getType());
			LOGGER.info("document: " + jsonDocument.getContent());
			LOGGER.info("mapping strategy: " + jsonDocument.getMappingStrategy());
			
			JsonTransformer<String> jsonTransformer = JsonTransformerFactory.getInstance(jsonDocument.getMappingStrategy());
			
			String cypher =  jsonTransformer.transform(jsonDocument.getId(), jsonDocument.getType(), jsonDocument.getContent());

			LOGGER.info("Cypher statement:");
			LOGGER.info("\n" + cypher);

			return Response.ok().build();

		} catch (Exception e) {

			LOGGER.error("Error parsing JSON document", e);

			return Response.status(Status.BAD_REQUEST).entity("Error parsing JSON document. Reason: " + e.getMessage())
					.build();
		}
	}
}
