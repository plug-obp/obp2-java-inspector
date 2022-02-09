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

import obp2.core.IFiredTransition;
import obp2.core.defaults.FiredTransition;
import obp2.runtime.core.ITransitionRelation;
import obp2.runtime.core.defaults.DefaultLanguageService;
import plug.utils.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class JInspectorTransitionRelation
        extends DefaultLanguageService<JInspectorConfiguration, Field, Void>
        implements ITransitionRelation<JInspectorConfiguration, Field, Void> {

    @Override
    public Set<JInspectorConfiguration> initialConfigurations() {
        LinkedList<Pair> object = new LinkedList<>();
        object.add(new Pair(2,3));
        object.add(new Pair('c',3));
        return Collections.singleton(
                new JInspectorConfiguration(
                        System.getSecurityManager()
                )
        );
    }

    @Override
    public Collection<Field> fireableTransitionsFrom(JInspectorConfiguration source) {
        if (source.data == null) return Collections.emptyList();
        List<Field> fields = getAllFields(source.data.getClass());
        return fields;
    }

    @Override
    public IFiredTransition<JInspectorConfiguration, Field, Void> fireOneTransition(JInspectorConfiguration source, Field action) {
        action.setAccessible(true);
        try {
            Object target = action.get(source.data);
            return new FiredTransition<>(source, new JInspectorConfiguration(target), action);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * @param clazz the class
     * @return all declared and inherited fields for this class.
     */
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Field[] theFields = clazz.getDeclaredFields();
        //filter static fields
        fields.addAll(Arrays.stream(theFields)
                .filter( this::nonStatic )
                .collect(Collectors.toCollection(ArrayList::new)));

        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }

        return fields;
    }

    boolean nonStatic(Field f) {
        return !Modifier.isStatic(f.getModifiers());
    }
}
