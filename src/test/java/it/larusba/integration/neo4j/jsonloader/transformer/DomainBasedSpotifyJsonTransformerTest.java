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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import it.larusba.integration.common.document.bean.JsonDocument;
import it.larusba.integration.common.document.bean.JsonObjectDescriptor;
import it.larusba.integration.common.document.mapping.JsonMappingStrategy;

/**
 *
 * @author Lorenzo Speranzoni
 */
public class DomainBasedSpotifyJsonTransformerTest {

  @Test
  public void shouldTransformU2SongOfInnocenceAlbumJsonIntoACypherStatement() {

    try {

      List<JsonObjectDescriptor> jsonObjectDescriptors = new ArrayList<JsonObjectDescriptor>();
      jsonObjectDescriptors.add(new JsonObjectDescriptor("Album", Arrays.asList("id"), "type"));
      jsonObjectDescriptors.add(new JsonObjectDescriptor("Artists", Arrays.asList("id"), "type"));
      jsonObjectDescriptors.add(new JsonObjectDescriptor("Images", Arrays.asList("url"), null));
      jsonObjectDescriptors.add(new JsonObjectDescriptor("Tracks", Arrays.asList("href"), null));
      jsonObjectDescriptors.add(new JsonObjectDescriptor("Items", Arrays.asList("id"), "type"));

      String spotifyAlbumDocument = FileUtils.readFileToString(new File("src/test/resources/spotify", "album-u2-songs-of-innocence.json"));

      //System.out.println(spotifyAlbumDocument);
      
      JsonDocument jsonDocument = new JsonDocument("U2SongOfInnocenceAlbum", "Album", spotifyAlbumDocument,
          JsonMappingStrategy.DOMAIN_DRIVEN, jsonObjectDescriptors);

      JsonTransformer<String> documentTransformer = new DomainDrivenJsonTransformer();

      System.out.println(documentTransformer.transform(jsonDocument));

    } catch (Exception e) {

      e.printStackTrace();

      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void shouldTransformU2NoLineOnTheHorizonAlbumJsonIntoACypherStatement() {

    try {

      List<JsonObjectDescriptor> jsonObjectDescriptors = new ArrayList<JsonObjectDescriptor>();
      jsonObjectDescriptors.add(new JsonObjectDescriptor("Album", Arrays.asList("id"), "type"));
      jsonObjectDescriptors.add(new JsonObjectDescriptor("Artists", Arrays.asList("id"), "type"));
      jsonObjectDescriptors.add(new JsonObjectDescriptor("Images", Arrays.asList("url"), null));
      jsonObjectDescriptors.add(new JsonObjectDescriptor("Items", Arrays.asList("id"), "type"));

      String spotifyAlbumDocument = FileUtils.readFileToString(new File("src/test/resources/spotify", "album-u2-no-line-on-the-horizon.json"));

      //System.out.println(spotifyAlbumDocument);
      
      JsonDocument jsonDocument = new JsonDocument("U2NoLineOnTheHorizonAlbum", "Album", spotifyAlbumDocument,
          JsonMappingStrategy.DOMAIN_DRIVEN, jsonObjectDescriptors);

      JsonTransformer<String> documentTransformer = new DomainDrivenJsonTransformer();

      System.out.println(documentTransformer.transform(jsonDocument));

    } catch (Exception e) {

      e.printStackTrace();

      Assert.fail(e.getMessage());
    }
  }
}
