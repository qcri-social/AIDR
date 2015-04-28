Name: aidr-persister

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-persister

# Overview

The aidr-persister module stores (_persists_) the items that have been collected and/or tagged into files. The items are persisted in JSON format in zip-compressed files.

The aidr-persister module also allow users to perform filtering and conversion operations on these files.

Filtering operations are the same ones as implemented in [aidr-output](Output).

Conversion operations are basically to allow three types of file format:
* One JSON array containing all items.
* One JSON object on a line per item.
* One comma-separated-value (CSV) line per item.

This module [No explicit start](does not require an explicit per-collection start).

# Technologies

* Google GSON 2.2.4 (JSON processor)
* JEDIS 2.4.2 (REDIS API library)
* SuperCSV (CSV library)
* Jersey 2+ (for JAX-RX 2.0)
* FasterXML (JSON processor)

# Module Dependencies

* [aidr-common](Common)
* [aidr-collector](Collector)
* [aidr-tagger](Tagger)
* [aidr-output](Output)
