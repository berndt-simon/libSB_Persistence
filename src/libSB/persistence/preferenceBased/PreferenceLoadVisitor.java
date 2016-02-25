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
package libSB.persistence.preferenceBased;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Stream;
import libSB.persistence.LoadVisitor;

/**
 *
 * @author Simon Berndt
 */
class PreferenceLoadVisitor implements LoadVisitor {

    private static final Logger LOG = Logger.getLogger(PreferenceLoadVisitor.class.getName());

    private final Preferences preferences;

    PreferenceLoadVisitor(Preferences preferences) {
	this.preferences = preferences;
    }

    @Override
    public <T> void restoreSingle(String key, Consumer<? super T> field, Function<byte[], T> typeConverter) {
        Objects.requireNonNull(typeConverter);
	if (key != null) {
	    final byte[] stored = this.preferences.getByteArray(key, null);
	    if (stored != null) {
		field.accept(typeConverter.apply(stored));
	    }
	}
    }

    @Override
    public Stream<LoadVisitor> restoreNested(String key) throws RuntimeException {
	try {
	    if (key == null || key.isEmpty() || key.charAt(0) == '/') {
		throw new IllegalArgumentException();
	    }
	    if (this.preferences.nodeExists(key)) {
		final Preferences multiSubNode = this.preferences.node(key);
		return Arrays.stream(multiSubNode.childrenNames()).filter((String singleSubNodeName) -> {
		    return singleSubNodeName.startsWith(key + '_');
		}).map(multiSubNode::node).map(PreferenceLoadVisitor::new);
	    }
	} catch (BackingStoreException ex) {
	    LOG.log(Level.WARNING, null, ex);
	}
	return Stream.empty();
    }

}
