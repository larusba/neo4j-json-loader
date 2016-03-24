package it.larusba.integration.neo4j.jsonloader.transformer.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class NodeTest {

	/**
	 * MERGE (album1:Album{name='the album name', id='1458856248873'}) ON CREATE
	 * SET album1.year='2016',
	 * album1.documentId='635e2291-31aa-4809-8f92-3a75ddd326a3'
	 */
	@Test
	public void testToString() {
		Node node = buildNode();

		System.out.println(node.toString());
	}

	private Node buildNode() {
		Map<String, String> keys = new HashMap<>();
		keys.put("id", String.valueOf(System.currentTimeMillis()));
		keys.put("name", "the album name");

		Map<String, String> attributes = new HashMap<>();
		attributes.put("year", "2016");

		Node node = new Node(String.valueOf(System.currentTimeMillis()), "album1", "Album");
		node.setKeys(keys);
		node.setAttributes(attributes);
		node.addListAttribute("markets", "IT");
		node.addListAttribute("markets", "DE");
		return node;
	}

	@Test
	public void testToStringOutComingRelations() {
		Node related = new Node(String.valueOf(System.currentTimeMillis()), "song1", "Track");

		List<Node> outcomingRelations = new ArrayList<>();
		outcomingRelations.add(related);

		Node node = buildNode();
		node.setOutcomingRelations(outcomingRelations);

		System.out.println(node.toString());
		System.out.println(node.toStringOutcomingRelations());
	}
}
