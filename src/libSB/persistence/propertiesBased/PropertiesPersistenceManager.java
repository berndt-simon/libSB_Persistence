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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import libSB.persistence.LoadVisitor;
import libSB.persistence.Loadable;
import libSB.persistence.PersistenceManager;
import libSB.persistence.SaveVisitor;
import libSB.persistence.Saveable;

/**
 *
 * @author Simon Berndt
 */
public class PropertiesPersistenceManager implements PersistenceManager {

    private static final Logger LOG = Logger.getLogger(PropertiesPersistenceManager.class.getName());
    
    public static final Path DEFAULT_CONFIG_PATH = Paths.get("persitedValues.xml");

    private final Path configFileLocation;

    public PropertiesPersistenceManager() {
	this(DEFAULT_CONFIG_PATH);
    }

    public PropertiesPersistenceManager(Path configFileLocation) {
	this.configFileLocation = configFileLocation;
    }

    @Override
    public void restore(Loadable peristable) {
	final Properties properties = new Properties();
	if (Files.exists(this.configFileLocation)) {
	    try (InputStream inputStream = Files.newInputStream(this.configFileLocation, StandardOpenOption.READ)) {
		properties.loadFromXML(inputStream);
		final LoadVisitor loadVisitor = new PropertiesBase64_LoadVisitor(properties, Base64.getDecoder()::decode);
		peristable.accept(loadVisitor);
	    } catch (final IOException ex) {
		LOG.log(Level.SEVERE, null, ex);
	    }
	}
    }

    @Override
    public void save(Saveable peristable) {
	final Properties properties = new Properties();
	final SaveVisitor saveVisitor = new PropertiesBase64_SaveVisitor(properties, Base64.getEncoder()::encode);
	peristable.accept(saveVisitor);
	try {
	    Files.createDirectories(this.configFileLocation.getParent());
	    try (OutputStream outputStream = Files.newOutputStream(this.configFileLocation, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
		properties.storeToXML(outputStream, null, StandardCharsets.UTF_8.name());
	    } catch (final IOException e) {
		LOG.log(Level.SEVERE, null, e);
	    }
	} catch (final IOException ex) {
	    LOG.log(Level.SEVERE, null, ex);
	}
    }

}
