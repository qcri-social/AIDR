package qa.qcri.aidr.manager.dto;

public class TaggerLabel {

    private Integer nominalLabelID;

    private String name;

    private String nominalLabelCode;

    private String description;

    private TaggerAttribute nominalAttribute;

    public TaggerLabel() {
    }

    public TaggerLabel(String name, Integer nominalLabelID) {
        this.name = name;
        this.nominalLabelID = nominalLabelID;
    }

    public TaggerLabel(String name, String nominalLabelCode, String description, TaggerAttribute nominalAttribute) {
        this.name = name;
        this.nominalLabelCode = nominalLabelCode;
        this.description = description;
        this.nominalAttribute = nominalAttribute;
    }

    public Integer getNominalLabelID() {
        return nominalLabelID;
    }

    public void setNominalLabelID(Integer nominalLabelID) {
        this.nominalLabelID = nominalLabelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNominalLabelCode() {
        return nominalLabelCode;
    }

    public void setNominalLabelCode(String nominalLabelCode) {
        this.nominalLabelCode = nominalLabelCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaggerAttribute getNominalAttribute() {
        return nominalAttribute;
    }

    public void setNominalAttribute(TaggerAttribute nominalAttribute) {
        this.nominalAttribute = nominalAttribute;
    }
}
