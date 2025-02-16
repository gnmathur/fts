# fts
Full Text Search - An Elasticsearch-like mini inverted index. 

Based on the ideas presented in this article - https://artem.krylysov.com/blog/2020/07/28/lets-build-a-full-text-search-engine/

# Build and Run

```bash
mvn compile exec:java  -Dexec.mainClass="dev.gnmathur.FTS"
```

Build with index rebuilt - 
```bash
 mvn compile exec:java  -Dexec.mainClass="dev.gnmathur.FTS" -Dexec.args="--rebuild"
```
