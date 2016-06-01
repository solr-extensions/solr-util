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

import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;

/**
 * Convenience methods for working with {@link SolrParams}.
 *
 * @since 1.2
 */
public class SolrParamsUtil {

   // private constructor to prevent instantiation
   private SolrParamsUtil() {
   }

   /**
    * Returns a modifiable version of the given parameters. If {@code params} already is an instance of
    * {@code ModifiableSolrParams}, the same instance is returned, otherwise, a modifiable copy of the parameters is
    * created and returned.
    * 
    * @param params the parameters. If {@code null}, an empty {@code ModifiableSolrParams} instance will be returned.
    */
   public static ModifiableSolrParams modifiable(SolrParams params) {
      if (params instanceof ModifiableSolrParams) {
         return (ModifiableSolrParams) params;
      }
      return new ModifiableSolrParams(params);
   }
}
