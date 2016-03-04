#!/bin/bash

echo "Test script to check whether 'json loader' extension works once installed into your neo4j test instance or not."
echo ""
echo "This script pretends server security to be disabled: add dbms.security.auth_enabled=false to your test instance file neo4-server.properties."
echo "Or alternatively, please add your credentials such as http://neo4j:larus@localhost:7474/ to this script."
echo ""
echo ""

curl -v -X PUT -H "Content-Type:application/json" -d '{"content": "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": \"CEO @ LARUS Business Automation\"}", "type":"Person"}' http://localhost:7474/jsonloader/
