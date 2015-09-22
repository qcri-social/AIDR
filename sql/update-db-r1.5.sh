#!/bin/bash
output=()
min=0
MYSQL_USER=aidr_admin
MYSQL_PASS=aidr_admin
while read -r output_line; do
    output+=("$output_line")
done < <(mysql -N -u $MYSQL_USER -p$MYSQL_PASS -e 'select nominalAttributeID from aidr_predict.nominal_attribute where userid != 1 order by nominalAttributeID')

cnt=${#output[@]}

for ((i=0; i<${cnt}; i++ ))
do
 min=$(mysql -N -u $MYSQL_USER -p$MYSQL_PASS -e 'select min(nominalLabelID) from aidr_predict.nominal_label where nominalAttributeID='${output[$i]})
 mysql -u $MYSQL_USER -p$MYSQL_PASS -e 'update aidr_predict.nominal_label set sequence = (100+nominalLabelID-'$min') where nominalAttributeID ='${output[$i]};
done
