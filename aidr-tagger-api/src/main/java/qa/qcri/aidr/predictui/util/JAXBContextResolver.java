package qa.qcri.aidr.predictui.util;

import qa.qcri.aidr.predictui.entities.NominalAttribute;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;
import javax.ws.rs.ext.Provider;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

    private JAXBContext context;

    private final Class[] types = {
            ResponseWrapper.class,
            NominalAttribute.class
    };

    public JAXBContextResolver() throws Exception {
    	this.context = JAXBContext.newInstance(types);
    }

    @Override
    public JAXBContext getContext(final Class objectType) {
        for (Class type : types) {
            if (type == objectType) {
                return context;
            }
        }
        return null;
    }

}
