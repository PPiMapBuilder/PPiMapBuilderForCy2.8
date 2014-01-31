![](https://fr.gravatar.com/userimage/46678059/7c7f65f2ea5b01dfc46adac45048df6b.jpg?size=40) PPiMapBuilder
=============

Cytoscape plug-in development (using maven)

##Build

Maven build :
> mvn clean install

##Use

The PPiMapBuilder plug-in for Cytoscape 2.8 need an access to a dedicated [PPiRetriever](https://github.com/PPiMapBuilder/PPiRetriever) PostgreSQL database server.
The access configuration for the server need to be set in "server.cfg" placed at root of resources folder.

Example server.cfg:
```
jdbc:postgresql://HOST/DB_NAME
USER
PASSWORD
```
