At a conceptual level, the _task buffer_ is the set of items that have been either labelled by a human, or are waiting to be labelled by a human. At an implementation level, the _task buffer_ is a set of elements in the database built around the **document** table.

# Tables included in the task buffer

* document
* document_nominal_label
* nominal_label
* task_assignment
* task_answer