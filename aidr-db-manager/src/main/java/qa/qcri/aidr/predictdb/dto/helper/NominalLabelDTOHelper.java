package qa.qcri.aidr.predictdb.dto.helper;

import java.util.ArrayList;
import java.util.Collection;

import qa.qcri.aidr.predictdb.dto.NominalLabelDTO;

public class NominalLabelDTOHelper {
	
	public static Collection<NominalLabel> toNominalLabelCollection(Collection<NominalLabelDTO> list) {
		if (list != null) {
			Collection<NominalLabel> nominalLabelList = new ArrayList<NominalLabel>();
			for (NominalLabelDTO t: list) {
				if (t != null) {
					NominalLabel nominalLabel  = new NominalLabel(t.getNominalLabelID(), t.getNominalLabelCode(), t.getName(), t.getDescription());
					nominalLabelList.add(nominalLabel);
				}
			}
			return nominalLabelList;
		}
		return null;
	}

	public static Collection<NominalLabelDTO> toNominalLabelDTOCollection(Collection<NominalLabel> list) {
		if (list != null) {
			Collection<NominalLabelDTO> nominalLabelDTOList = new ArrayList<NominalLabelDTO>();
			for (NominalLabel t: list) {
				if (t != null) {
					NominalLabelDTO nominalLabelDTO = new NominalLabelDTO(t.getNominalLabelID(), t.getNominalLabelCode(), t.getName(), t.getDescription());
					nominalLabelDTOList.add(nominalLabelDTO);
				}
			}
			return nominalLabelDTOList;
		}
		return null;
	}
}
