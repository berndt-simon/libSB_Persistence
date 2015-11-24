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
package libSB.persistence.propertiesBased;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import libSB.persistence.SaveVisitor;

/**
 *
 * @author Simon Berndt
 */
class PropertiesBase64_SaveVisitor implements SaveVisitor {

    private final Map<Object, Object> properties;
    private final Function<byte[], byte[]> encoder;

    PropertiesBase64_SaveVisitor(Map<Object, Object> properties, Function<byte[], byte[]> encoder) {
	this.properties = properties;
	this.encoder = encoder;
    }

    @Override
    public <T> void persistSingle(String key, Supplier<? extends T> value, Function<T, byte[]> typeConverter) {
	final byte[] bytes = typeConverter.apply(value.get());
	final byte[] encodedBytes = this.encoder.apply(bytes);
	final String property = new String(encodedBytes, StandardCharsets.UTF_8);
        this.properties.put(key, property);
    }

    @Override
    public void persistNested(String key, Stream<? extends Consumer<? super SaveVisitor>> values) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
