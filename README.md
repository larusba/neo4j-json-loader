# JSON loader plugin for Neo4j

Welcome to the [Neo4j](http://neo4j.com/) JSON loader project.

The main goal of this project is to provide a configurable Neo4j server extension to load, transform and save JSON documents into the graph.

Please be aware that this project is still under development and at this first stage will work only for a restricted set of data models.

**Looking for contributors** If you are interested in contributing to this project we would love to hear from you and encourage you to submit a Pull Request.

## Usage

This example shows how to use the Cassandra Neo4j Connector to convert the music playlist dataset from Cassandra into Neo4j:

1. Download Neo4j and extract it into your $PATH_TO_YOUR_NEO4J_TEST_INSTANCE: `wget http://dist.neo4j.org/neo4j-community-2.3.2-unix.tar.gz`
2. Clone this repository into your $PATH_TO_JSON_LOADER: `git clone https://github.com/larusba/neo4j-json-loader.git`
3. `cd $PATH_TO_JSON_LOADER/neo4j-json-loader`
4. Run `mvn clean package`
5. Copy `PATH_TO_JSON_LOADER/target/neo4j-json-loader-1.0-SNAPSHOT.jar` and its dependencies `PATH_TO_JSON_LOADER/target/lib/*.jar` into your neo4j test instance at `$PATH_TO_YOUR_NEO4J_TEST_INSTANCE/plugins`
6. Start Neo4j `PATH_TO_YOUR_NEO4J_TEST_INSTANCE/bin/neo4j start`
7. Run `curl -v -X PUT -H "Content-Type:application/json" -d '{"content": "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": \"CEO @ LARUS Business Automation\"}", "type":"Person"}' http://localhost:7474/jsonloader/`

Current implementation should print an early draft version of the cypher script that will be used to load JSON data into neo4j.

## License

   Licensed under the Apache License, Version 2.0 (the "License").
   You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
