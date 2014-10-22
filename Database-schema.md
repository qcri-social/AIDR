## Schema A 
Used by: AIDR-Tagger, AIDR-Task-Manager, and AIDR-Tagger-API.

![](http://i.imgur.com/WEn6pcl.png)

[High quality version (PNG)](http://i.imgur.com/WEn6pcl.png)

## Schema B
Used by: AIDR-Manager

(to be available)


## Schema C

Used by: AIDR-Analytics

# tag_data Table

+----------------+--------------+------+-----+---------+-------+
| Field          | Type         | Null | Key | Default | Extra |
+----------------+--------------+------+-----+---------+-------+
| attribute_code | varchar(255) | NO   | PRI | NULL    |       |
| crisis_code    | varchar(255) | NO   | PRI | NULL    |       |
| granularity    | bigint(20)   | NO   | PRI | NULL    |       |
| label_code     | varchar(255) | NO   | PRI | NULL    |       |
| timestamp      | bigint(20)   | NO   | PRI | NULL    |       |
| count          | int(11)      | NO   |     | NULL    |       |
| max_created_at | bigint(20)   | YES  |     | NULL    |       |
| min_created_at | bigint(20)   | YES  |     | NULL    |       |
+----------------+--------------+------+-----+---------+-------+
