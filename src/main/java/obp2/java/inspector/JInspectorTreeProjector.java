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

import obp2.runtime.core.TreeItem;
import obp2.runtime.core.defaults.DefaultTreeProjector;
import obp2.runtime.core.defaults.Object2TreeItem;

import java.lang.reflect.Field;

public class JInspectorTreeProjector extends DefaultTreeProjector<JInspectorConfiguration, Field, Void> {

    @Override
    public TreeItem projectConfiguration(JInspectorConfiguration configuration) {
        if (configuration == null || configuration.data == null) return TreeItem.empty;
        TreeItem item = Object2TreeItem.getTreeItem(configuration.data);
        item.name = item.name +" : "+ configuration.data.getClass().getSimpleName();
        return item;
    }

    @Override
    public TreeItem projectFireable(Field action) {
        return new TreeItem(action.getName() + " : " + action.getType().getSimpleName());
    }
}
