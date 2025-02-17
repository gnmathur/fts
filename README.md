# Full Text Search
Full Text Search is a lightweight, Elasticsearch-like mini full-text search engine implemented in Java. It builds an inverted index to enable efficient text search over documents. This project is inspired by the ideas presented in [Let's Build a Full-Text Search Engine](https://artem.krylysov.com/blog/2020/07/28/lets-build-a-full-text-search-engine/).

## Features
* __Inverted Index__: Quickly search through text documents by indexing terms.
* __Rebuildable Index__: Rebuild the index on demand to reflect updates in the data.
* __Simple and Lightweight__: A minimal implementation to help you understand the inner workings of full-text search.

## Prerequisites
* Java 8+
* Maven

## Build and Run
Clone the repository and navigate into the project directory:

### Standard Run
To compile and run the project with the current index, use the following Maven command:

```bash
mvn compile exec:java -Dexec.mainClass="dev.gnmathur.FTS"
```

### Rebuild the Index and Run
If you need to rebuild the index (for example, after updating the data), run:

```
mvn compile exec:java -Dexec.mainClass="dev.gnmathur.FTS" -Dexec.args="--rebuild"
```

## Usage
Once the application is running, you can perform full-text searches through the `/search?text=<search terms>` endpoint. For example:

```bash
% curl -s -G "http://localhost:7100/search" --data-urlencode "text=Longest river in the world" | jq
[
  {
    "title": "Wikipedia: Nile (disambiguation)\n",
    "abstractText": "The Nile, in northeast Africa, is one of the world's longest rivers.\n"
  },
  {
    "title": "Wikipedia: Massive Murray Paddle\n",
    "abstractText": "The Massive Murray Paddle, formerly the Red Cross Murray Marathon or Murray Marathon, and later the YMCA Massive Murray Paddle, is an Australian 415 km, 5-day canoe/kayak flatwater race on the Murray River. One of the longest annual flatwater canoe races in the world, it starts in Yarrawonga and ends in Koondrook.\n"
  },
  {
    "title": "Wikipedia: USS Brazos\n",
    "abstractText": "USS Brazos (AO-4) was an Kanawha-class fleet oiler built during World War I for service in the United States Navy, and named for the Brazos River, the longest river in the State of Texas.\n"
  },
  {
    "title": "Wikipedia: Diaoshuilou Falls\n",
    "abstractText": "|world_rank=|average_flow=|watercourse=|run=Mudan River|average_width=|width=|number_drops=|height_longest=|elevation=|photo=|location=|coords_ref=|coordinates=|map_width=|map_caption=|map=Heilongjiang#China|photo_caption=|photo_width=|type=}}The Diaoshuilou Falls () are a  wide waterfall in Heilongjiang Province, People's Republic of China at the northern end of Lake Jingpo. The cascade is at its most impressive during the wetter summer months whilst in winter it freezes into a curtain of ice.\n"
  },
  {
    "title": "Wikipedia: Geography of the African Union\n",
    "abstractText": "The African Union covers almost the entirety of continental Africa and several off-shore islands. Consequently, it is wildly diverse, including the world's largest hot desert (the Sahara), huge jungles and savannas, and the world's longest river (the Nile).\n"
  },
  {
    "title": "Wikipedia: List of disasters on the Severn\n",
    "abstractText": "The following is a list of notable accidents on the River Severn and Severn Estuary, which runs through England and Wales. The river is the longest river in Britain and at its mouth has the second highest rise and fall of tide in the world.\n"
  },
  {
    "title": "Wikipedia: Brower's Spring\n",
    "abstractText": "Brower's Spring is a spring in the Centennial Mountains of Beaverhead County, Montana that was identified by surveyor Jacob V. Brower in 1888 as the ultimate headwaters of the Missouri River and thus of the fourth-longest river system in the world, the  Mississippi–Missouri River.\n"
  },
  ....
```

## License
This project is licensed under the MIT License. See the LICENSE file for details.