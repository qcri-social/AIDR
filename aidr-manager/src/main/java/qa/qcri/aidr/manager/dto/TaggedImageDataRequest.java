package qa.qcri.aidr.manager.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import qa.qcri.aidr.manager.dto.ImageTaskQueueDTO;

@XmlRootElement
public class TaggedImageDataRequest implements Serializable {

	private static final long serialVersionUID = -6584267982492562317L;

	@XmlElement
	private List<ImageTaskQueueDTO> taggedImageData;

    public List<ImageTaskQueueDTO> getTaggedImageData() {
        return taggedImageData;
    }

    public void setTaggedImageData(List<ImageTaskQueueDTO> taggedImageData) {
        this.taggedImageData = taggedImageData;
    }
}
