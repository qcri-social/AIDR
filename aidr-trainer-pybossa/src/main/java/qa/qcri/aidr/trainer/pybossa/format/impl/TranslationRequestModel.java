package qa.qcri.aidr.trainer.pybossa.format.impl;

/**
 * Created by kamal on 3/22/15.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import qa.qcri.aidr.trainer.pybossa.entity.TaskTranslation;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;


@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslationRequestModel {
    @JsonProperty("contact_email")
    private String contactEmail;
    @JsonProperty("title")
    private String title;

    @JsonProperty("source_lang")
    private String sourceLanguage;

    @JsonProperty("target_langs")
    private String[] targetLanguages;

    @JsonProperty("source_document_ids")
    private long[] sourceDocumentIds;

    @JsonProperty("source_wordcount")
    private int sourceWordCount;

    @JsonProperty("instructions")
    private String instructions;

    @JsonProperty("deadline")
    private Date deadline;

    @JsonProperty("urgency")
    private String urgency;

    @JsonProperty("project_id")
    private long projectId;

    @JsonProperty("callback_url")
    private String callbackURL;

    private List<TaskTranslation> translationList;

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String[] getTargetLanguages() {
        return targetLanguages;
    }

    public void setTargetLanguages(String[] targetLanguages) {
        this.targetLanguages = targetLanguages;
    }

    public long[] getSourceDocumentIds() {
        return sourceDocumentIds;
    }

    public void setSourceDocumentIds(long[] sourceDocumentIds) {
        this.sourceDocumentIds = sourceDocumentIds;
    }

    public int getSourceWordCount() {
        return sourceWordCount;
    }

    public void setSourceWordCount(int sourceWordCount) {
        this.sourceWordCount = sourceWordCount;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public List<TaskTranslation> getTranslationList() {
        return translationList;
    }

    public void setTranslationList(List<TaskTranslation> translationList) {
        this.translationList = translationList;
    }

}

