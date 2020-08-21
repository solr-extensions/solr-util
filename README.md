# Utility classes for Apache Solr

[![travis ci build status](https://travis-ci.org/solr-cool/solr-util.png)](https://travis-ci.org/github/solr-cool/solr-util)
[![Maven Central](https://img.shields.io/maven-central/v/cool.solr/solr-util)](https://search.maven.org/artifact/cool.solr/solr-util/)

> ♻️ this is the official and maintained fork of the original [@shopping24](https://github.com/shopping24) repository maintained by [solr.cool](https://solr.cool).

Utility classes for working with Apache Solr. Currently contains only some convenience methods for working with
instances of `NamedList`.

For more information, read the [JavaDoc](https://www.javadoc.io/doc/cool.solr/solr-util).

## Usage

The library is published via Maven Central. If you're using Maven, simply add the following dependency to your project:

    <dependency>
        <groupId>cool.solr</groupId>
        <artifactId>solr-util</artifactId>
        <version>1.5.0</version>
    </dependency>

### Releasing the project to maven central
    
Define new versions
    
    $ export NEXT_VERSION=<version>
    $ export NEXT_DEVELOPMENT_VERSION=<version>-SNAPSHOT

Then execute the release chain

    $ mvn org.codehaus.mojo:versions-maven-plugin:2.8.1:set -DgenerateBackupPoms=false -DnewVersion=$NEXT_VERSION
    $ git commit -a -m "pushes to release version $NEXT_VERSION"
    $ git tag -a v$NEXT_VERSION -m "`curl -s http://whatthecommit.com/index.txt`"
    $ mvn -P release
    
Then, increment to next development version:
    
    $ mvn org.codehaus.mojo:versions-maven-plugin:2.0:set -DgenerateBackupPoms=false -DnewVersion=$NEXT_DEVELOPMENT_VERSION
    $ git commit -a -m "pushes to development version $NEXT_DEVELOPMENT_VERSION"
    $ git push origin tag v$NEXT_VERSION && git push origin

## Contributing

We're looking forward to your comments, issues and pull requests!

## License

This library is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for more information.
