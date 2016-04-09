package it.larusba.integration.neo4j.jsonloader.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import it.larusba.integration.neo4j.jsonloader.bean.DocumentNode;
import it.larusba.integration.neo4j.jsonloader.support.CypherGenerator;

public class DocumentNodeTest {

  /**
   * MERGE (album1:Album{name='the album name', id='1458856248873'}) ON CREATE
   * SET album1.year='2016',
   * album1.documentId='635e2291-31aa-4809-8f92-3a75ddd326a3'
   */
  @Test
  public void testToString() {
      DocumentNode node = buildNode();

      System.out.println(CypherGenerator.generateNodeStatement(node));
  }

  @Test
  public void testToStringOutComingRelations() {
      DocumentNode related = new DocumentNode(String.valueOf(System.currentTimeMillis()), "items", "song1", "Track");

      List<DocumentNode> outcomingRelations = new ArrayList<>();
      outcomingRelations.add(related);

      DocumentNode node = buildNode();
      node.setOutgoingRelations(outcomingRelations);

      System.out.println(CypherGenerator.generateNodeStatement(node));
      System.out.println(CypherGenerator.generateOutgoingRelationsStatements(node));
  }

	private DocumentNode buildNode() {
	    Map<String, Object> keys = new HashMap<>();
	    keys.put("id", String.valueOf(System.currentTimeMillis()));
	    keys.put("name", "the album name");
	
	    Map<String, Object> attributes = new HashMap<>();
	    attributes.put("year", "2016");
	
	    DocumentNode node = new DocumentNode(String.valueOf(System.currentTimeMillis()), "albums", "album1", "Album");
	    node.setKeys(keys);
	    node.setAttributes(attributes);
	    node.addListAttribute("markets", "IT");
	    node.addListAttribute("markets", "DE");
	    return node;
	}
}
