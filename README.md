
# OpenAPI Light-4J Server


### Build and Start

The scaffolded project contains a single module. A fat jar server.jar will be generated in target directory after running the build command below.

```
./mvnw clean install -Prelease
```

With the fatjar in the server/target directory, you can start the server with the following command.

```
java -jar server/target/server.jar
```

To speed up the test, you can avoid the fat jar generation and start the server from Maven.

```
./mvnw clean install exec:exec
```




### Test

By default, the OAuth2 JWT security verification is disabled, so you can use Curl or Postman to test your service right after the server is started. For example, the petstore API has the following endpoint.

```
curl -k https://localhost:8443/v1/pets
```

For your API, you need to change the path to match your specifications.

### Tutorial

To explore more features, please visit the [petstore tutorial](https://doc.networknt.com/tutorial/rest/openapi/petstore/).


## Jackson 3 source build

This application uses Jackson 3.2.3 and the framework's immutable mapper.
Jackson annotation imports remain `com.fasterxml.jackson.annotation` by design.
The application has no direct classic SnakeYAML parser to replace; its packaged
runtime includes SnakeYAML Engine through the Jackson 3/framework stack.
Classic SnakeYAML may still be supplied by upstream compatibility dependencies;
this change does not claim to remove every transitive classic library.

Run `mvn clean verify` against a compatible source-built framework stack. A clean
build matters because the shade plugin replaces the main JAR. The migration tests
exercise application binding/results and reject Jackson 2 databind on the runtime
classpath. CI additionally checks the packaged JAR and applies the strict artifact
audit, allowing the shared Jackson annotations artifact.

`.github/workflows/jackson3-source-build.yml` builds the SHA-pinned prerequisites
in `ci/jackson3-source-refs.json` in an isolated Maven repository and retains test,
source and dependency-audit evidence. These pins describe an intentionally qualified
stack, not the current heads of every upstream repository. Update them as a reviewed
batch when adopting a new baseline. This application migration does not move the
qualified core/policy tuple or claim deployment/native qualification.

JSON Schema Validator is 3.0.8 and Logstash Encoder is 9.0, avoiding their older
Jackson 2 dependency paths. Generated model annotations and wire names are unchanged.
