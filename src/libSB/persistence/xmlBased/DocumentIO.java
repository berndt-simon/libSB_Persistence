/* 
 * The MIT License
 *
 * Copyright 2015 Simon Berndt.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package libSB.persistence.xmlBased;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Simon Berndt
 */
public class DocumentIO {

    private static final Logger LOG = Logger.getLogger(DocumentIO.class.getName());

    private final Transformer xmlOutputTransformer;
    private final DocumentBuilder documentBuilder;

    public DocumentIO() {
        try {
            this.xmlOutputTransformer = TransformerFactory.newInstance().newTransformer();
            this.xmlOutputTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            this.xmlOutputTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException | TransformerConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Document loadDocumentFromXML(Path sourcePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(sourcePath, StandardOpenOption.READ)) {
            return this.documentBuilder.parse(inputStream);
        } catch (final SAXException ex) {
            throw new IOException(ex);
        }
    }

    public void writeDocumentToXML(Document xmlDoc, Path destinationPath) throws IOException {
        try {
            Files.createDirectories(destinationPath.getParent());
            try (Writer fileWriter = Files.newBufferedWriter(destinationPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                this.xmlOutputTransformer.transform(new DOMSource(xmlDoc), new StreamResult(fileWriter));
            }
        } catch (TransformerException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

}
