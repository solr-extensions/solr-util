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

package com.s24.search.solr.util;

import static com.s24.search.solr.util.NamedLists.get;
import static com.s24.search.solr.util.NamedLists.getCollection;
import static com.s24.search.solr.util.NamedLists.getList;
import static com.s24.search.solr.util.NamedLists.getSet;
import static com.s24.search.solr.util.NamedLists.getString;
import static com.s24.search.solr.util.NamedLists.navigate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.solr.common.util.NamedList;
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
      assertSame(nested, navigate(list, "nested"));
      assertSame(deeplyNested, navigate(navigate(list, "nested"), "deeplyNested"));

      assertSame(deeplyNested, navigate(list, "nested", "deeplyNested"));
   }

   @Test
   public void testNavigate_nullValueOrNoElement() throws Exception {
      assertNull(navigate(nested, "none"));
      assertNull(navigate(nested, "does-not-exist"));

      assertNull(navigate(list, "nested", "none"));
      assertNull(navigate(list, "nested", "does-not-exist"));

      // Should return null if any of the path elements is null or does not exist
      assertNull(navigate(list, "none", "whatever"));
      assertNull(navigate(list, "does-not-exist", "whatever"));
   }

   @Test
   public void testNavigate_nullList() throws Exception {
      assertNull(navigate(null, "whatever"));
      assertNull(navigate(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testNavigate_elementIsString() throws Exception {
      navigate(nested, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testNavigate_elementIsString_nested() throws Exception {
      navigate(list, "nested", "deeplyNested", "greeting");
   }

   @Test
   public void testNavigateByIndex() throws Exception {
      assertSame(nested, navigate(list, 0));
   }

   @Test
   public void testNavigateByIndex_nullValue() throws Exception {
      assertNull(navigate(list, 1));
   }

   @Test
   public void testNavigateByIndex_nullList() throws Exception {
      assertNull(navigate(null, 0));
   }

   @Test
   public void testNavigateByIndex_invalidIndex() throws Exception {
      assertNull(navigate(list, 42));
   }

   @Test
   public void testGetSet() throws Exception {
      assertSame(javaSet, getSet(nested, "set"));
      assertSame(javaSet, getSet(list, "nested", "set"));
   }

   @Test
   public void testGetSet_nullValueOrNoElement() throws Exception {
      assertNull(getSet(list, "does-not-exist"));
      assertNull(getSet(list, "none"));
      assertNull(getSet(list, "nested", "none"));
      assertNull(getSet(list, "nested", "does-not-exist"));
   }

   @Test
   public void testGetSet_nullList() throws Exception {
      assertNull(getSet(null, "whatever"));
      assertNull(getSet(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetSet_elementIsString() throws Exception {
      getSet(nested, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testgetSet_elementIsString_nested() throws Exception {
      getSet(list, "nested", "str");
   }

   @Test
   public void testGetList() throws Exception {
      assertSame(javaList, getList(nested, "list"));
      assertSame(javaList, getList(list, "nested", "list"));
   }

   @Test
   public void testGetList_nullValueOrNoElement() throws Exception {
      assertNull(getList(list, "does-not-exist"));
      assertNull(getList(list, "none"));
      assertNull(getList(list, "nested", "none"));
      assertNull(getList(list, "nested", "does-not-exist"));
   }

   @Test
   public void testGetList_nullList() throws Exception {
      assertNull(getList(null, "whatever"));
      assertNull(getList(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetList_elementIsString() throws Exception {
      getList(nested, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testgetList_elementIsString_nested() throws Exception {
      getList(list, "nested", "str");
   }

   @Test
   public void testGetCollection() throws Exception {
      // Lists and sets can be retrieved as Collection instances
      assertSame(javaSet, getCollection(nested, "set"));
      assertSame(javaSet, getCollection(list, "nested", "set"));
      assertSame(javaList, getCollection(nested, "list"));
      assertSame(javaList, getCollection(list, "nested", "list"));
   }

   @Test
   public void testGetCollection_nullValueOrNoElement() throws Exception {
      assertNull(getCollection(list, "does-not-exist"));
      assertNull(getCollection(list, "none"));
      assertNull(getCollection(list, "nested", "none"));
      assertNull(getCollection(list, "nested", "does-not-exist"));
   }

   @Test
   public void testGetCollection_nullList() throws Exception {
      assertNull(getCollection(null, "whatever"));
      assertNull(getCollection(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetCollection_elementIsString() throws Exception {
      getCollection(nested, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testgetCollection_elementIsString_nested() throws Exception {
      getCollection(list, "nested", "str");
   }

   @Test
   public void testGetString() throws Exception {
      assertEquals("foo", getString(nested, "str"));
      assertEquals("hello", getString(list, "nested", "deeplyNested", "greeting"));
   }

   @Test
   public void testGetString_nullValueOrNoElement() throws Exception {
      assertNull(getString(list, "does-not-exist"));
      assertNull(getString(list, "none"));
      assertNull(getString(list, "nested", "none"));
      assertNull(getString(list, "nested", "does-not-exist"));
   }

   @Test
   public void testGetString_nullList() throws Exception {
      assertNull(getString(null, "whatever"));
      assertNull(getString(null, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetString_elementIsList() throws Exception {
      getString(nested, "list");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testgetString_elementIsList_nested() throws Exception {
      getString(list, "nested", "list");
   }

   @Test
   public void testGet() throws Exception {
      assertEquals("foo", get(nested, String.class, "str"));
      assertEquals("foo", get(list, String.class, "nested", "str"));
   }

   @Test
   public void testGet_nullValueOrNoElement() throws Exception {
      // When returning null, the expected class should be irrelevant
      assertNull(get(list, Void.class, "does-not-exist"));
      assertNull(get(list, Void.class, "none"));
      assertNull(get(list, Void.class, "nested", "none"));
      assertNull(get(list, Void.class, "nested", "does-not-exist"));
   }

   @Test
   public void testGet_nullList() throws Exception {
      assertNull(get(null, Void.class, "whatever"));
      assertNull(get(null, Void.class, "some", "path"));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGet_elementOfUnexpectedClass() throws Exception {
      get(nested, Void.class, "str");
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGet_elementOfUnexpectedClass_nested() throws Exception {
      get(list, Void.class, "nested", "str");
   }

   @Test
   public void testGetIndex() throws Exception {
      assertEquals(deeplyNested, get(nested, NamedList.class, 0));
      assertEquals("foo", get(nested, String.class, 2));
   }

   @Test
   public void testGetIndex_nullList() throws Exception {
      assertNull(get(null, Void.class, 0));
   }

   @Test(expected = NamedListEntryClassCastException.class)
   public void testGetIndex_elementOfUnexpectedClass() throws Exception {
      get(list, Void.class, 0);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testgetIndex_invalidIndex() throws Exception {
      get(list, String.class, 42);
   }
}
