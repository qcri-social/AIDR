/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.util;

/**
 *
 * @author Imran
 */
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class Lists<T> {

    private List<T> values = new ArrayList<T>();

    @XmlAnyElement(lax=true)
    public List<T> getValues() {
        return values;
    }

}