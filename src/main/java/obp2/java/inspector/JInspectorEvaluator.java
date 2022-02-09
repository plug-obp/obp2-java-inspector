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

import obp2.runtime.core.IAtomicPropositionsEvaluator;
import obp2.runtime.core.defaults.DefaultLanguageService;
import org.apache.commons.jexl3.*;

import java.lang.reflect.Field;

public class JInspectorEvaluator
    extends DefaultLanguageService<JInspectorConfiguration, Field, Void>
        implements IAtomicPropositionsEvaluator<JInspectorConfiguration, Field, Void> {
    // Create or retrieve a jexl engine
    JexlEngine jexl = new JexlBuilder().create();

    JexlExpression expressions[];
    @Override
    public int[] registerAtomicPropositions(String[] atomicPropositions) throws Exception {
        expressions = new JexlExpression[atomicPropositions.length];
        int[] indices = new int[atomicPropositions.length];

        for (int i = 0; i < atomicPropositions.length; i++) {
            expressions[i] = jexl.createExpression(atomicPropositions[i]);
            indices[i] = i;
        }

        return indices;
    }

    @Override
    public boolean[] getAtomicPropositionValuations(JInspectorConfiguration configuration) {
        // Create a context and add data
        JexlContext context = new ClassAwareContext();
        context.set("t", configuration.data );

        return evaluateOn(context);
    }

    @Override
    public boolean[] getAtomicPropositionValuations(JInspectorConfiguration source, Field fireable, Void output, JInspectorConfiguration target) {
        // Create a context and add data
        JexlContext context = new ClassAwareContext();
        context.set("s", source.data );
        context.set("a", fireable );
        context.set("p", output );
        context.set("t", target.data );

        return evaluateOn(context);
    }

    boolean[] evaluateOn(JexlContext context) {
        // Now evaluate the expression, getting the result
        boolean[] results = new boolean[expressions == null ? 0 : expressions.length];
        for (int i=0; i<results.length; i++) {
            results[i] = (boolean)expressions[i].evaluate(context);
        }

        return results;
    }

    public static class ClassAwareContext extends MapContext {
        @Override
        public boolean has(String name) {
            try {
                return super.has(name) || Class.forName(name) != null;
            } catch (ClassNotFoundException xnf) {
                return false;
            }
        }

        @Override
        public Object get(String name) {
            try {
                Object found = super.get(name);
                if (found == null && !super.has(name)) {
                    found = Class.forName(name);
                }
                return found;
            } catch (ClassNotFoundException xnf) {
                return null;
            }
        }
    }
}
