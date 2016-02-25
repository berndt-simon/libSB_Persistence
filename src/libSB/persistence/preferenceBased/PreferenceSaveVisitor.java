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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.prefs.Preferences;
import java.util.stream.Stream;
import libSB.persistence.SaveVisitor;

/**
 *
 * @author Simon Berndt
 */
class PreferenceSaveVisitor implements SaveVisitor {

    private final Preferences preferences;

    PreferenceSaveVisitor(Preferences preferences) {
	this.preferences = preferences;
    }

    @Override
    public <T> void persistSingle(String key, Supplier<? extends T> value, Function<T, byte[]> typeConverter) {
        Objects.requireNonNull(typeConverter);
	if (key != null) {
	    final byte[] bytes = typeConverter.apply(value.get());
	    if (bytes != null) {
		    this.preferences.putByteArray(key, bytes);
	    } else {
		    this.preferences.remove(key);
	    }
	}
    }

    @Override
    public void persistNested(String key, Stream<? extends Consumer<? super SaveVisitor>> values) {
        Objects.requireNonNull(values);
	if (key == null || key.isEmpty() || key.charAt(0) == '/') {
	    throw new IllegalArgumentException();
	}
	final Preferences multiSubNode = this.preferences.node(key);
	final AtomicInteger childs = new AtomicInteger();
	values.forEach((Consumer<? super SaveVisitor> consumer) -> {
	    final String singleSubNodeName = String.format("%s_%d", key, childs.getAndIncrement());
	    final Preferences individualSubNode = multiSubNode.node(singleSubNodeName);
	    final SaveVisitor subVisitor = new PreferenceSaveVisitor(individualSubNode);
	    consumer.accept(subVisitor);
	});
    }

}
