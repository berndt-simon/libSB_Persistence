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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import libSB.persistence.SaveVisitor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Simon Berndt
 */
public class XMLSaveVisitor implements SaveVisitor {

    private final Node root;
    private final Function<String, Node> nodeFactory;

    public XMLSaveVisitor(Node root, Function<String, Node> nodeFactory) {
	this.root = root;
	this.nodeFactory = nodeFactory;
    }

    @Override
    public <T> void persistSingle(String key, Supplier<? extends T> value, Function<T, byte[]> typeConverter) {
	insertElement(key, typeConverter.apply(value.get()));
    }

    @Override
    public void persistNested(String key, Stream<? extends Consumer<? super SaveVisitor>> values) {
	final NodeList childNodes = this.root.getChildNodes();
	Node nestedNode = null;
	for (int i = 0; i < childNodes.getLength(); i++) {
	    if (childNodes.item(i).getNodeName().equals(key)) {
		nestedNode = childNodes.item(i);
		break;
	    }
	}
	if (nestedNode == null) {
	    nestedNode = this.nodeFactory.apply(key);
		this.root.appendChild(nestedNode);
	}
	while (nestedNode.hasChildNodes()) {
	    nestedNode.removeChild(nestedNode.getFirstChild());
	}
	final Node finalNestedNode = nestedNode;
	values.forEach((Consumer<? super SaveVisitor> consumer) -> {
	    final Node nestedSubNode = this.nodeFactory.apply(key + "Item");
	    final SaveVisitor subVisitor = new XMLSaveVisitor(nestedSubNode, this.nodeFactory);
	    consumer.accept(subVisitor);
	    if (finalNestedNode.hasChildNodes()) {
		finalNestedNode.appendChild(nestedSubNode);
	    }
	});
    }

    private void insertElement(String identifier, byte[] value) {
	final NodeList childNodes = this.root.getChildNodes();
	Node nestedNode = null;
	for (int i = 0; i < childNodes.getLength(); i++) {
	    if (childNodes.item(i).getNodeName().equals(identifier)) {
		nestedNode = childNodes.item(i);
		break;
	    }
	}
	if (nestedNode == null && value != null) {
	    nestedNode = this.nodeFactory.apply(identifier);
		this.root.appendChild(nestedNode);
	}
	if (nestedNode != null) {
	    nestedNode.setNodeValue(new String(Base64.getEncoder().encode(value), StandardCharsets.UTF_8));
	}
    }

}
