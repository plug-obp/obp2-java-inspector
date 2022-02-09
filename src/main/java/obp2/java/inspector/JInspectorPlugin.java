/*
 * MIT License
 *
 * Copyright (c) 2022 Ciprian Teodorov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package obp2.java.inspector;

import obp2.runtime.core.ILanguageModule;
import obp2.runtime.core.ILanguagePlugin;
import obp2.runtime.core.LanguageModule;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.function.Function;

public class JInspectorPlugin implements ILanguagePlugin<URI, JInspectorConfiguration, Field, Void> {
    @Override
    public String getName() {
        return "OBP2 Java Inspector";
    }

    @Override
    public String[] getExtensions() {
        return new String[] {".j"};
    }

    @Override
    public Function<URI, ILanguageModule<JInspectorConfiguration, Field, Void>> languageModuleFunction() {
        return this::getModule;
    }

    public ILanguageModule<JInspectorConfiguration, Field, Void> getModule(URI uri) {
        return new LanguageModule<>(
                  new JInspectorTransitionRelation()
                , new JInspectorEvaluator()
                , new JInspectorTreeProjector()

        );
    }
}
