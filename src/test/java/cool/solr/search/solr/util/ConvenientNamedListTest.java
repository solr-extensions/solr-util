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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;

public class ConvenientNamedListTest {

   ConvenientNamedList list;
   NamedList<Object> underylingList;
   NamedList<Object> nested;
   NamedList<Object> deeplyNested;

   List<Integer> javaList = Arrays.asList(1, 2, 3);
   Set<Integer> javaSet = new HashSet<>(Arrays.asList(1, 2, 3));

   @Before
   public void setUp() {
      underylingList = new NamedListBuilder<>()
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
      list = new ConvenientNamedList(underylingList);
   }

   @Test
   public void testGetNamedList() throws Exception {
      assertSame(underylingList, list.getNamedList());
   }

   @Test
   public void testGetNested() throws Exception {
      assertSame(nested, list.getNested("nested").getNamedList());
      assertSame(deeplyNested, list.getNested("nested", "deeplyNested").getNamedList());
   }

   @Test
   public void testGetNested_invalidPath() throws Exception {
      ConvenientNamedList result = list.getNested("does-not-exist");
      assertNotNull(result);
      assertEquals(0, result.size());
   }

   @Test
   public void testGetNested_nullElementInPath() throws Exception {
      ConvenientNamedList result = list.getNested("nested", "none");
      assertNotNull(result);
      assertEquals(0, result.size());
   }

   @Test
   public void testGetNestedByIndex() throws Exception {
      assertSame(nested, list.getNested(0).getNamedList());
   }

   @Test
   public void testgetNestedByIndex_nullElement() throws Exception {
      ConvenientNamedList result = list.getNested(1);
      assertNotNull(result);
      assertEquals(0, result.size());
   }

   @Test
   public void testGetNestedByIndex_invalidIndex() throws Exception {
      ConvenientNamedList result = list.getNested(42);
      assertNotNull(result);
      assertEquals(0, result.size());
   }

   @Test
   public void testGetSet() throws Exception {
      assertSame(javaSet, list.getNested("nested").getSet("set"));
   }

   @Test
   public void testGetSet_nullElement() throws Exception {
      assertNull(list.getSet("none"));
   }

   @Test
   public void testgetSet_elementDoesNotExist() throws Exception {
      assertNull(list.getSet("does-not-exist"));
   }

   @Test
   public void testGetList() throws Exception {
      assertSame(javaList, list.getNested("nested").getList("list"));
   }

   @Test
   public void testGetList_nullElement() throws Exception {
      assertNull(list.getList("none"));
   }

   @Test
   public void testgetList_elementDoesNotExist() throws Exception {
      assertNull(list.getList("does-not-exist"));
   }

   @Test
   public void testGetCollection() throws Exception {
      assertSame(javaSet, list.getNested("nested").getCollection("set"));
   }

   @Test
   public void testGetCollection_nullElement() throws Exception {
      assertNull(list.getCollection("none"));
   }

   @Test
   public void testgetCollection_elementDoesNotExist() throws Exception {
      assertNull(list.getCollection("does-not-exist"));
   }

   @Test
   public void testGetString() throws Exception {
      assertEquals("foo", list.getNested("nested").getString("str"));
   }

   @Test
   public void testGetString_nullElement() throws Exception {
      assertNull(list.getString("none"));
   }

   @Test
   public void testGetString_elementDoesNotExist() throws Exception {
      assertNull(list.getString("does-not-exist"));
   }

   @Test
   public void testToString() throws Exception {
      assertEquals(underylingList.toString(), list.toString());
      assertEquals("{}", new ConvenientNamedList(null).toString());
   }
}
