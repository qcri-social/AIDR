package qa.qcri.aidr.trainer.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/19/13
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "lookup")
public class Lookup implements Serializable {

    private static final long serialVersionUID = -5527566248002296042L;

    public Long getLookupID() {
        return lookupID;
    }

    public void setLookupID(Long lookupID) {
        this.lookupID = lookupID;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Integer columnValue) {
        this.columnValue = columnValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Id
    @Column(name = "lookupID")
    private Long lookupID;

    @Column (name = "tableName", nullable = false)
    private String tableName;

    @Column (name = "columnName", nullable = false)
    private String columnName;

    @Column (name = "columnValue", nullable = false)
    private Integer columnValue;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "created", nullable = false)
    private Date created;


}
