CREATE INDEX TAG_FETCH_INDEX1 ON aidr_analysis.tag_data (crisis_code, attribute_code, timestamp, granularity);
CREATE INDEX TAG_FETCH_INDEX2 ON aidr_analysis.tag_data (crisis_code, attribute_code);
CREATE INDEX TAG_FETCH_INDEX3 ON aidr_analysis.tag_data (crisis_code);
CREATE INDEX TAG_FETCH_INDEX4 ON aidr_analysis.tag_data (attribute_code);
CREATE INDEX TAG_FETCH_INDEX5 ON aidr_analysis.tag_data (timestamp);
CREATE INDEX TAG_FETCH_INDEX6 ON aidr_analysis.tag_data (crisis_code, attribute_code, label_code, timestamp, granularity);
CREATE INDEX TAG_FETCH_INDEX7 ON aidr_analysis.tag_data (crisis_code, attribute_code, label_code);
