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
import java.util.stream.Stream;
import libSB.persistence.LoadVisitor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Simon Berndt
 */
public class XMLLoadVisitor implements LoadVisitor {

    private final Node root;

    public XMLLoadVisitor(Node root) {
	this.root = root;
    }

    @Override
    public <T> void restoreSingle(String key, Consumer<? super T> field, Function<byte[], T> typeConverter) {
	byte[] data = null;
	final NodeList childNodes = this.root.getChildNodes();
	for (int i = 0; i < childNodes.getLength(); i++) {
	    final Node childNode = childNodes.item(i);
	    if (childNode.getNodeName().equals(key)) {
		final String nodeValue = childNode.getNodeValue();
		if (nodeValue != null) {
		    data = Base64.getDecoder().decode(nodeValue.getBytes(StandardCharsets.UTF_8));
		    break;
		}
	    }
	}
	if (data != null) {
	    field.accept(typeConverter.apply(data));
	}
    }

    @Override
    public Stream<LoadVisitor> restoreNested(String key) {
	final NodeList childNodes = this.root.getChildNodes();
	Node nestedNode = null;
	for (int i = 0; i < childNodes.getLength(); i++) {
	    if (childNodes.item(i).getNodeName().equals(key)) {
		nestedNode = childNodes.item(i);
		break;
	    }
	}
	if (nestedNode != null) {
	    final NodeList subChildNodes = nestedNode.getChildNodes();
	    final Stream.Builder<Node> builder = Stream.builder();
	    for (int i = 0; i < subChildNodes.getLength(); i++) {
		builder.add(subChildNodes.item(i));
	    }
	    return builder.build().filter((Node node) -> {
		return node.getNodeName().equals(key + "Item");
	    }).map(XMLLoadVisitor::new);
	}
	return Stream.empty();
    }

}
