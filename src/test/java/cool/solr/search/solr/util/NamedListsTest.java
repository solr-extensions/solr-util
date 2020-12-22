/*
 * Copyright 2015 Shopping24 GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.solr.search.solr.util;

import static cool.solr.search.solr.util.NamedLists.get;
import static cool.solr.search.solr.util.NamedLists.getCollection;
import static cool.solr.search.solr.util.NamedLists.getList;
import static cool.solr.search.solr.util.NamedLists.getSet;
import static cool.solr.search.solr.util.NamedLists.getString;
import static cool.solr.search.solr.util.NamedLists.navigate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.solr.common.util.NamedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NamedListsTest {

   NamedList<Object> list;
   NamedList<Object> nested;
   NamedList<Object> deeplyNested;

   List<Integer> javaList = Arrays.asList(1, 2, 3);
   Set<Integer> javaSet = new HashSet<>(Arrays.asList(1, 2, 3));

   @Before
   public void setUp() {
      list = new NamedListBuilder<>()
            .add("nested", nested = new NamedListBuilder<>()
                  .add("deeplyNested", deeplyNested = new NamedListBuilder<>()
                        .add("greeting", "hello")
                        .build())
                  .add("none", null)
                  .add("str", "foo")
                  .add("list", javaList)
                  .add("set", javaSet)
                  .build())
            .add("none", null)
            .build();
   }

   @Test
   public void testNavigate() throws Exception {
      assertSame(nested, NamedLists.navigate(list, "nested"));
      assertSame(deeplyNested, NamedLists.navigate(NamedLists.navigate(list, "nested"), "deeplyNested"));

      assertSame(deeplyNested, NamedLists.navigate(list, "nested", "deeplyNested"));
   }

   @Test
   public void testNavigate_nullValueOrNoElement() throws Exception {
      assertNull(NamedLists.navigate(nested, "none"));
      assertNull(NamedLists.navigate(nested, "does-not-exist"));

      assertNull(NamedLists.navigate(list, "nested", "none"));
      assertNull(NamedLists.navigate(list, "nested", "does-not-exist"));

      // Should return null if any of the path elements is null or does not exist
      assertNull(NamedLists.navigate(list, "none", "whatever"));
      assertNull(NamedLists.navigate(list, "does-not-exist", "whatever"));
   }

   @Test
   public void testNavigate_nullList() throws Exception {
      assertNull(NamedLists.navigate(null, "whatever"));
      assertNull(NamedLists.navigate(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testNavigate_elementIsString() throws Exception {
      NamedLists.navigate(nested, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testNavigate_elementIsString_nested() throws Exception {
      NamedLists.navigate(list, "nested", "deeplyNested", "greeting");
   }

   @Test
   public void testNavigateByIndex() throws Exception {
      assertSame(nested, NamedLists.navigate(list, 0));
   }

   @Test
   public void testNavigateByIndex_nullValue() throws Exception {
      assertNull(NamedLists.navigate(list, 1));
   }

   @Test
   public void testNavigateByIndex_nullList() throws Exception {
      assertNull(NamedLists.navigate(null, 0));
   }

   @Test
   public void testNavigateByIndex_invalidIndex() throws Exception {
      assertNull(NamedLists.navigate(list, 42));
   }

   @Test
   public void testKeys_null() throws Exception {
      assertNull(NamedLists.keys(null));
   }

   @Test
   public void testKeys() throws Exception {
      Set<String> expected = new HashSet<>();
      expected.add("nested");
      expected.add("none");
      Assert.assertEquals(expected, NamedLists.keys(list));
   }

   @Test
   public void testGetSet() throws Exception {
      assertSame(javaSet, NamedLists.getSet(nested, "set"));
      assertSame(javaSet, NamedLists.getSet(list, "nested", "set"));
   }

   @Test
   public void testGetSet_nullValueOrNoElement() throws Exception {
      assertNull(NamedLists.getSet(list, "does-not-exist"));
      assertNull(NamedLists.getSet(list, "none"));
      assertNull(NamedLists.getSet(list, "nested", "none"));
      assertNull(NamedLists.getSet(list, "nested", "does-not-exist"));
   }

   @Test
   public void testGetSet_nullList() throws Exception {
      assertNull(NamedLists.getSet(null, "whatever"));
      assertNull(NamedLists.getSet(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetSet_elementIsString() throws Exception {
      NamedLists.getSet(nested, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testgetSet_elementIsString_nested() throws Exception {
      NamedLists.getSet(list, "nested", "str");
   }

   @Test
   public void testGetList() throws Exception {
      assertSame(javaList, NamedLists.getList(nested, "list"));
      assertSame(javaList, NamedLists.getList(list, "nested", "list"));
   }

   @Test
   public void testGetList_nullValueOrNoElement() throws Exception {
      assertNull(NamedLists.getList(list, "does-not-exist"));
      assertNull(NamedLists.getList(list, "none"));
      assertNull(NamedLists.getList(list, "nested", "none"));
      assertNull(NamedLists.getList(list, "nested", "does-not-exist"));
   }

   @Test
   public void testGetList_nullList() throws Exception {
      assertNull(NamedLists.getList(null, "whatever"));
      assertNull(NamedLists.getList(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetList_elementIsString() throws Exception {
      NamedLists.getList(nested, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testgetList_elementIsString_nested() throws Exception {
      NamedLists.getList(list, "nested", "str");
   }

   @Test
   public void testGetCollection() throws Exception {
      // Lists and sets can be retrieved as Collection instances
      assertSame(javaSet, NamedLists.getCollection(nested, "set"));
      assertSame(javaSet, NamedLists.getCollection(list, "nested", "set"));
      assertSame(javaList, NamedLists.getCollection(nested, "list"));
      assertSame(javaList, NamedLists.getCollection(list, "nested", "list"));
   }

   @Test
   public void testGetCollection_nullValueOrNoElement() throws Exception {
      assertNull(NamedLists.getCollection(list, "does-not-exist"));
      assertNull(NamedLists.getCollection(list, "none"));
      assertNull(NamedLists.getCollection(list, "nested", "none"));
      assertNull(NamedLists.getCollection(list, "nested", "does-not-exist"));
   }

   @Test
   public void testGetCollection_nullList() throws Exception {
      assertNull(NamedLists.getCollection(null, "whatever"));
      assertNull(NamedLists.getCollection(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetCollection_elementIsString() throws Exception {
      NamedLists.getCollection(nested, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testgetCollection_elementIsString_nested() throws Exception {
      NamedLists.getCollection(list, "nested", "str");
   }

   @Test
   public void testGetString() throws Exception {
      Assert.assertEquals("foo", NamedLists.getString(nested, "str"));
      Assert.assertEquals("hello", NamedLists.getString(list, "nested", "deeplyNested", "greeting"));
   }

   @Test
   public void testGetString_nullValueOrNoElement() throws Exception {
      assertNull(NamedLists.getString(list, "does-not-exist"));
      assertNull(NamedLists.getString(list, "none"));
      assertNull(NamedLists.getString(list, "nested", "none"));
      assertNull(NamedLists.getString(list, "nested", "does-not-exist"));
   }

   @Test
   public void testGetString_nullList() throws Exception {
      assertNull(NamedLists.getString(null, "whatever"));
      assertNull(NamedLists.getString(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetString_elementIsList() throws Exception {
      NamedLists.getString(nested, "list");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testgetString_elementIsList_nested() throws Exception {
      NamedLists.getString(list, "nested", "list");
   }

   @Test
   public void testGet() throws Exception {
      Assert.assertEquals("foo", NamedLists.get(nested, String.class, "str"));
      Assert.assertEquals("foo", NamedLists.get(list, String.class, "nested", "str"));
   }

   @Test
   public void testGet_nullValueOrNoElement() throws Exception {
      // When returning null, the expected class should be irrelevant
      assertNull(NamedLists.get(list, Void.class, "does-not-exist"));
      assertNull(NamedLists.get(list, Void.class, "none"));
      assertNull(NamedLists.get(list, Void.class, "nested", "none"));
      assertNull(NamedLists.get(list, Void.class, "nested", "does-not-exist"));
   }

   @Test
   public void testGet_nullList() throws Exception {
      assertNull(NamedLists.get(null, Void.class, "whatever"));
      assertNull(NamedLists.get(null, Void.class, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGet_elementOfUnexpectedClass() throws Exception {
      NamedLists.get(nested, Void.class, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGet_elementOfUnexpectedClass_nested() throws Exception {
      NamedLists.get(list, Void.class, "nested", "str");
   }

   @Test
   public void testGetIndex() throws Exception {
      Assert.assertEquals(deeplyNested, NamedLists.get(nested, NamedList.class, 0));
      Assert.assertEquals("foo", NamedLists.get(nested, String.class, 2));
   }

   @Test
   public void testGetIndex_nullList() throws Exception {
      assertNull(NamedLists.get(null, Void.class, 0));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetIndex_elementOfUnexpectedClass() throws Exception {
      NamedLists.get(list, Void.class, 0);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testgetIndex_invalidIndex() throws Exception {
      NamedLists.get(list, String.class, 42);
   }
}
