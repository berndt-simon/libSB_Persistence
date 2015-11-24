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

import java.util.prefs.Preferences;
import libSB.persistence.LoadVisitor;
import libSB.persistence.Loadable;
import libSB.persistence.PersistenceManager;
import libSB.persistence.SaveVisitor;
import libSB.persistence.Saveable;

/**
 *
 * @author Simon Berndt
 */
public class PreferenceBasedPersistanceManager implements PersistenceManager {

    private final Preferences preferences;

    private final SaveVisitor saveVisitor;
    private final LoadVisitor loadVisitor;

    public PreferenceBasedPersistanceManager(String applicationName) {
	final Preferences appLocal = Preferences.userRoot();
	this.preferences = appLocal.node(applicationName);
	this.saveVisitor = new PreferenceSaveVisitor(this.preferences);
	this.loadVisitor = new PreferenceLoadVisitor(this.preferences);
    }

    public PreferenceBasedPersistanceManager(String applicationName, String subModule) {
	final Preferences appLocal = Preferences.userRoot();
	this.preferences = appLocal.node(applicationName + '/' + subModule);
	this.saveVisitor = new PreferenceSaveVisitor(this.preferences);
	this.loadVisitor = new PreferenceLoadVisitor(this.preferences);
    }

    @Override
    public void restore(Loadable peristable) {
	peristable.accept(this.loadVisitor);
    }

    @Override
    public void save(Saveable peristable) {
	peristable.accept(this.saveVisitor);
    }

}
