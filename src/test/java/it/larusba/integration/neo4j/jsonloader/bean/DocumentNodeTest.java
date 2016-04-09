package it.larusba.integration.neo4j.jsonloader.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import it.larusba.integration.neo4j.jsonloader.bean.DocumentNode;

public class DocumentNodeTest {

	/**
	 * MERGE (album1:Album{name='the album name', id='1458856248873'}) ON CREATE
	 * SET album1.year='2016',
	 * album1.documentId='635e2291-31aa-4809-8f92-3a75ddd326a3'
	 */
	@Test
	public void testToString() {
		DocumentNode documentNode = buildNode();

		System.out.println(documentNode.toString());
	}

	private DocumentNode buildNode() {
		Map<String, String> keys = new HashMap<>();
		keys.put("id", String.valueOf(System.currentTimeMillis()));
		keys.put("name", "the album name");

		Map<String, String> attributes = new HashMap<>();
		attributes.put("year", "2016");

		DocumentNode documentNode = new DocumentNode(String.valueOf(System.currentTimeMillis()), "album1", "Album");

		documentNode.setKeys(keys);
		documentNode.setAttributes(attributes);
		documentNode.addListAttribute("markets", "IT");
		documentNode.addListAttribute("markets", "DE");

		return documentNode;
	}

	@Test
	public void testToStringOutComingRelations() {
		DocumentNode related = new DocumentNode(String.valueOf(System.currentTimeMillis()), "song1", "Track");

		List<DocumentNode> outcomingRelations = new ArrayList<>();
		outcomingRelations.add(related);

		DocumentNode documentNode = buildNode();
		documentNode.setOutcomingRelations(outcomingRelations);

		System.out.println(documentNode.toString());
		System.out.println(documentNode.toStringOutcomingRelations());
	}
}
