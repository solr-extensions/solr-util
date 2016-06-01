/*
 * Copyright 2016 Shopping24 GmbH
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

public class SolrParamsUtilTest {

   @Test
   public void testModifiable() throws Exception {
      Map<String, String> map = new HashMap<>();
      map.put("foo", "bar");
      SolrParams params = new MapSolrParams(map);

      ModifiableSolrParams modifiable = SolrParamsUtil.modifiable(params);
      assertEquals(1, modifiable.size());
      assertEquals("bar", modifiable.get("foo"));
   }

   @Test
   public void testModifiable_noCopyIfAlreadyModifiable() throws Exception {
      ModifiableSolrParams params = new ModifiableSolrParams();
      params.set("foo", "bar");

      ModifiableSolrParams modifiable = SolrParamsUtil.modifiable(params);
      assertSame("Should not create unnecessary copy if input is already ModifiableSolrParams", params, modifiable);
      assertEquals(1, modifiable.size());
      assertEquals("bar", modifiable.get("foo"));
   }

   @Test
   public void testModifiable_null() throws Exception {
      ModifiableSolrParams modifiable = SolrParamsUtil.modifiable(null);
      assertNotNull(modifiable);
      assertEquals(0, modifiable.size());
   }
}
