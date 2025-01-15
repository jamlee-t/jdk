/*
 * Copyright (c) 2014, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package validation;

import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

/*
 * @test
 * @bug 6531160
 * @library /javax/xml/jaxp/libs /javax/xml/jaxp/unittest
 * @run testng/othervm validation.Bug6531160
 * @summary Test document generated by newDocument() can be validated with a Schema.
 */
public class Bug6531160 {

    private static final String XSD = "<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema'>\n" + "  <xs:element name='root' type='xs:string'/>\n"
            + "</xs:schema>";

    @Test
    public void testDOMLevel1Validation() throws Exception {
        SchemaFactory fact = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = fact.newSchema(new StreamSource(new StringReader(XSD)));
        DocumentBuilderFactory docfact = DocumentBuilderFactory.newInstance();
        docfact.setNamespaceAware(true);

        Document doc = docfact.newDocumentBuilder().newDocument();
        doc.appendChild(doc.createElement("root"));

        try {
            schema.newValidator().validate(new DOMSource(doc));
        } catch (SAXParseException e) {
            Assert.fail("Validation failed: " + e.getMessage());
        }
    }

}
