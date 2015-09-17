package com.redhat.samples.ws.wsdl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaImport;
import javax.wsdl.extensions.schema.SchemaReference;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;

public class WsdlTransformer {

    private WSDLFactory factory;

    public WsdlTransformer() {
        try {
            factory = WSDLFactory.newInstance();
        } catch (WSDLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private WSDLReader wsdlReader() {
        WSDLReader reader = factory.newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", true); // true by default
        reader.setFeature("javax.wsdl.importDocuments", true); //  true by default
        return reader;
    }

    private WSDLWriter wsdlWriter() {
        WSDLWriter writer = factory.newWSDLWriter();
        return writer;
    }

    public String inlineSchemas(String wsdl) {
        try {
            Definition definition = wsdlReader().readWSDL(wsdl);
            doInlineSchemas(definition.getTypes());
            StringWriter inlined = new StringWriter();
            wsdlWriter().writeWSDL(definition, inlined);
            return inlined.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void doInlineSchemas(Types types) {
        List<Schema> externalSchemas = new ArrayList<Schema>();
        for (Object element : types.getExtensibilityElements()) {
            Schema schema = (Schema) element;
            // imports
            for (Object key : schema.getImports().keySet()) {
                for (Object value : (Vector<?>) schema.getImports().get(key)) {
                    SchemaImport schemaImport = (SchemaImport) value;
                    externalSchemas.add(schemaImport.getReferencedSchema());
                }
            }
            // includes
            for (Object include : schema.getIncludes()) {
                externalSchemas.add(((SchemaReference) include).getReferencedSchema());
            }
        }
        types.getExtensibilityElements().clear();
        for (Schema schema : externalSchemas) {
            types.addExtensibilityElement(schema);
        }
    }

}
