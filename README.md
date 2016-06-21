# JSON loader plugin for Neo4j

Welcome to the [Neo4j](http://neo4j.com/) JSON loader project.

The main goal of this project is to provide a _configurable_ Neo4j server extension to load, transform and save JSON documents into the graph.

Please be aware that this project is still under development and at this first stage will work only for a restricted set of data models.

**Looking for contributors** If you are interested in contributing to this project we would love to hear from you and encourage you to submit a Pull Request.

## Usage

This example shows how to use build, install and run this server extension:

1. Download Neo4j and extract it into your *$PATH_TO_YOUR_NEO4J_TEST_INSTANCE*: `wget http://dist.neo4j.org/neo4j-community-2.3.2-unix.tar.gz`
2. Edit `$PATH_TO_YOUR_NEO4J_TEST_INSTANCE/conf/neo4-server.conf` adding `dbms.security.auth_enabled=false` - *BE AWARE! Just for testing purpose, so you can avoid passing credentials during tests.*
3. Edit `$PATH_TO_YOUR_NEO4J_TEST_INSTANCE/conf/neo4-server.conf` adding `org.neo4j.server.thirdparty_jaxrs_classes=it.larusba.integration.neo4j.jsonloader.rest=/jsonloader` - To active this server extension
4. Clone this repository into your *$PATH_TO_JSON_LOADER*: `git clone https://github.com/larusba/neo4j-json-loader.git`
5. `cd $PATH_TO_JSON_LOADER/neo4j-json-loader`
6. Run `mvn clean package`
7. Copy `PATH_TO_JSON_LOADER/target/neo4j-json-loader-1.0-SNAPSHOT.jar` and its dependencies `PATH_TO_JSON_LOADER/target/lib/*.jar` into your neo4j test instance at `$PATH_TO_YOUR_NEO4J_TEST_INSTANCE/plugins`
8. Start Neo4j `PATH_TO_YOUR_NEO4J_TEST_INSTANCE/bin/neo4j start`
9. Run `curl -v -X PUT -H "Content-Type:application/json" -d '{"content": "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": \"CEO @ LARUS Business Automation\"}", "type":"Person"}' http://localhost:7474/jsonloader/`

Current implementation should print an early draft version of the cypher script that will be used to load JSON data into neo4j.

## License

Copyright (c) 2016 [LARUS Business Automation](http://www.larus-ba.it)

This file is part of the "LARUS Integration Framework for Neo4j".

The "LARUS Integration Framework for Neo4j" is licensed
under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
