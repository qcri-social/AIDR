package qa.qcri.aidr.predictdb.dto.helper;

import java.util.ArrayList;
import java.util.List;

import qa.qcri.aidr.predictdb.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.predictdb.entities.task.DocumentNominalLabel;

public class DocumentNominalLabelDTOHelper {
	public static DocumentNominalLabel toDocumentNominalLabel(DocumentNominalLabelDTO dto) {
		if (dto != null) {
			DocumentNominalLabel doc = new DocumentNominalLabel(dto.getDocumentID(), dto.getNominalLabelID(), dto.getUserID());
			return doc;
		} 
		return null;
	}

	public static DocumentNominalLabelDTO toDocumentNominalLabelDTO(DocumentNominalLabel doc) {
		if (doc != null) {
			DocumentNominalLabelDTO dto = new DocumentNominalLabelDTO(doc.getDocumentID(), doc.getNominalLabelID(), doc.getUserID());
			return dto;
		} 
		return null;
	}
	
	public static List<DocumentNominalLabel> toDocumentNominalLabelList(List<DocumentNominalLabelDTO> list) {
		if (list != null) {
			List<DocumentNominalLabel> docList = new ArrayList<DocumentNominalLabel>();
			for (DocumentNominalLabelDTO dto: list) {
				DocumentNominalLabel doc = new DocumentNominalLabel(dto.getDocumentID(), dto.getNominalLabelID(), dto.getUserID());
				docList.add(doc);
			}
			return docList;
		} 
		return null;
	}

	public static List<DocumentNominalLabelDTO> toDocumentNominalLabelDTOList(List<DocumentNominalLabel> list) {
		if (list != null) {
			List<DocumentNominalLabelDTO> dtoList = new ArrayList<DocumentNominalLabelDTO>();
			for (DocumentNominalLabel doc: list) {
				DocumentNominalLabelDTO dto = new DocumentNominalLabelDTO(doc.getDocumentID(), doc.getNominalLabelID(), doc.getUserID());
				dtoList.add(dto);
			}
			return dtoList;
		} 
		return null;
	}
}
