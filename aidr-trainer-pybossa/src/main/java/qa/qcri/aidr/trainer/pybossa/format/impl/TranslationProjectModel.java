package qa.qcri.aidr.trainer.pybossa.format.impl;

/**
 * Created by kamal on 3/22/15.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public class TranslationProjectModel {
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("client_id")
    private String clientId;
    private String name;
    private String summary;
    private String description;

    public String getProjectId() {
        return projectId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }
}
