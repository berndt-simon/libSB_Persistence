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

import libSB.persistence.LoadVisitor;
import libSB.persistence.Loadable;
import libSB.persistence.PersistenceManager;
import libSB.persistence.SaveVisitor;
import libSB.persistence.Saveable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Simon Berndt
 */
public class DOMPersistenceManager implements PersistenceManager {

	public static final String DEFAULT_ROOT_ELEMENT_IDENTIFIER = "persistedValues";

	private final Document document;
	private final Node root;

	public DOMPersistenceManager(Document document) {
		this.document = document;
		this.root = findOrCreateRoot(document);
	}

	public DOMPersistenceManager(Document document, Node root) {
		this.document = document;
		this.root = root;
	}

	private static Node findOrCreateRoot(Document doc) {
	    final NodeList childNodes = doc.getChildNodes();
	    for (int i = 0; i < childNodes.getLength(); i++) {
		final Node childNode = childNodes.item(i);
		if (childNode instanceof Element) {
		    return childNode;
		}
	    }
	    final Element rootNode = doc.createElement(DEFAULT_ROOT_ELEMENT_IDENTIFIER);
	    doc.appendChild(rootNode);
	    return rootNode;
	}

	@Override
	public void save(Saveable persistable) {
	    final SaveVisitor xmlSaveVisitor = new XMLSaveVisitor(this.root, this.document::createElement);
	    persistable.accept(xmlSaveVisitor);
	}

    @Override
    public void restore(Loadable persistable) {
	final LoadVisitor xmlLoadVisitor = new XMLLoadVisitor(this.root);
	persistable.accept(xmlLoadVisitor);
	}

}
