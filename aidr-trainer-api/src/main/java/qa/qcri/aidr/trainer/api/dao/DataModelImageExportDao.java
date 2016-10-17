package qa.qcri.aidr.trainer.api.dao;

import qa.qcri.aidr.trainer.api.entity.DataModelImageExport;

/**
 * @author Latika
 *
 */
public interface DataModelImageExportDao extends AbstractDao<DataModelImageExport, Long> {

	Long getTotalImageCountForCollection(String collectionCode);
}
