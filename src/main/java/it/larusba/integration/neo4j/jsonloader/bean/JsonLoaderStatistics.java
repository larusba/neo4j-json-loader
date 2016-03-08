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
package it.larusba.integration.neo4j.jsonloader.bean;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.neo4j.graphdb.QueryStatistics;

/**
 *
 * @author Lorenzo Speranzoni
 */
@XmlRootElement
public class JsonLoaderStatistics implements QueryStatistics {

  private int nodesCreated;
  private int nodesDeleted;
  private int relationshipsCreated;
  private int relationshipsDeleted;
  private int propertiesSet;
  private int labelsAdded;
  private int labelsRemoved;
  private int indexesAdded;
  private int indexesRemoved;
  private int constraintsAdded;
  private int constraintsRemoved;
  private boolean containsUpdates;

  public JsonLoaderStatistics() {
  }

  public JsonLoaderStatistics(QueryStatistics queryStatistics) {
    this.nodesCreated = queryStatistics.getNodesCreated();
    this.nodesDeleted = queryStatistics.getNodesDeleted();
    this.relationshipsCreated = queryStatistics.getRelationshipsCreated();
    this.relationshipsDeleted = queryStatistics.getRelationshipsDeleted();
    this.propertiesSet = queryStatistics.getPropertiesSet();
    this.labelsAdded = queryStatistics.getLabelsAdded();
    this.labelsRemoved = queryStatistics.getLabelsRemoved();
    this.indexesAdded = queryStatistics.getIndexesAdded();
    this.indexesRemoved = queryStatistics.getIndexesRemoved();
    this.constraintsAdded = queryStatistics.getConstraintsAdded();
    this.constraintsRemoved = queryStatistics.getConstraintsRemoved();
    this.containsUpdates = queryStatistics.containsUpdates();
  }

  public int getNodesCreated() {
    return nodesCreated;
  }

  public void setNodesCreated(int nodesCreated) {
    this.nodesCreated = nodesCreated;
  }

  public int getNodesDeleted() {
    return nodesDeleted;
  }

  public void setNodesDeleted(int nodesDeleted) {
    this.nodesDeleted = nodesDeleted;
  }

  public int getRelationshipsCreated() {
    return relationshipsCreated;
  }

  public void setRelationshipsCreated(int relationshipsCreated) {
    this.relationshipsCreated = relationshipsCreated;
  }

  public int getRelationshipsDeleted() {
    return relationshipsDeleted;
  }

  public void setRelationshipsDeleted(int relationshipsDeleted) {
    this.relationshipsDeleted = relationshipsDeleted;
  }

  public int getPropertiesSet() {
    return propertiesSet;
  }

  public void setPropertiesSet(int propertiesSet) {
    this.propertiesSet = propertiesSet;
  }

  public int getLabelsAdded() {
    return labelsAdded;
  }

  public void setLabelsAdded(int labelsAdded) {
    this.labelsAdded = labelsAdded;
  }

  public int getLabelsRemoved() {
    return labelsRemoved;
  }

  public void setLabelsRemoved(int labelsRemoved) {
    this.labelsRemoved = labelsRemoved;
  }

  public int getIndexesAdded() {
    return indexesAdded;
  }

  public void setIndexesAdded(int indexesAdded) {
    this.indexesAdded = indexesAdded;
  }

  public int getIndexesRemoved() {
    return indexesRemoved;
  }

  public void setIndexesRemoved(int indexesRemoved) {
    this.indexesRemoved = indexesRemoved;
  }

  public int getConstraintsAdded() {
    return constraintsAdded;
  }

  public void setConstraintsAdded(int constraintsAdded) {
    this.constraintsAdded = constraintsAdded;
  }

  public int getConstraintsRemoved() {
    return constraintsRemoved;
  }

  public void setConstraintsRemoved(int constraintsRemoved) {
    this.constraintsRemoved = constraintsRemoved;
  }

  @JsonIgnore
  public boolean containsUpdates() {
    return getContainsUpdates();
  }

  public boolean getContainsUpdates() {
    return containsUpdates;
  }

  public void setContainsUpdates(boolean containsUpdates) {
    this.containsUpdates = containsUpdates;
  }
}
