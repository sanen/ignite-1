/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.cache;

import org.apache.ignite.*;
import org.apache.ignite.cache.query.*;
import org.apache.ignite.cache.query.annotations.*;
import org.apache.ignite.configuration.*;
import org.apache.ignite.internal.util.typedef.*;
import org.apache.ignite.internal.util.typedef.internal.*;
import org.apache.ignite.lang.*;
import org.apache.ignite.spi.discovery.tcp.*;
import org.apache.ignite.testframework.junits.common.*;

import javax.cache.*;
import java.io.*;
import java.util.*;

/**
 * Test {@link CacheConfiguration#setQueryIndexEnabled} checked before executing query
 */
public class GridCacheQueryIndexDisabledSelfTest extends GridCommonAbstractTest {
    /** */
    public GridCacheQueryIndexDisabledSelfTest() {
        super(true);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override protected IgniteConfiguration getConfiguration(String gridName) throws Exception {
        IgniteConfiguration cfg = super.getConfiguration(gridName);

        CacheConfiguration ccfg = defaultCacheConfiguration();

        ccfg.setQueryIndexEnabled(false);

        cfg.setCacheConfiguration(ccfg);

        TcpDiscoverySpi disco = new TcpDiscoverySpi();

        cfg.setDiscoverySpi(disco);

        return cfg;
    }

    /**
     * @throws Exception If failed.
     */
    public void testSqlQuery() throws Exception {
        IgniteCache<Integer, SqlValue> cache = jcache();

        try {
            Collection<Cache.Entry<Integer, SqlValue>> res =
                cache.query(new SqlQuery(SqlValue.class, "val >= 0")).getAll();

            assert false;
        }
        catch (CacheException e) {
            X.println("Caught expected exception: " + e);
        }
        catch (Exception e) {
            assert false;
        }
    }

    /**
     * @throws Exception If failed.
     */
    public void testSqlFieldsQuery() throws Exception {
        IgniteCache<Integer, SqlValue> cache = jcache();

        try {
            Collection<Cache.Entry<Integer, SqlValue>> res =
                cache.query(new SqlFieldsQuery("select * from Person")).getAll();

            assert false;
        }
        catch (CacheException e) {
            X.println("Caught expected exception: " + e);
        }
        catch (Exception e) {
            assert false;
        }

        try {
            List<List<?>> res =
                cache.queryFields(new SqlFieldsQuery("select * from Person")).getAll();

            assert false;
        }
        catch (CacheException e) {
            X.println("Caught expected exception: " + e);
        }
        catch (Exception e) {
            assert false;
        }
    }

    /**
     * @throws Exception If failed.
     */
    public void testFullTextQuery() throws Exception {
        IgniteCache<Integer, String> cache = jcache();

        try {
            Collection<Cache.Entry<Integer, String>> res =
                cache.query(new TextQuery(String.class, "text")).getAll();

            assert false;
        }
        catch (CacheException e) {
            X.println("Caught expected exception: " + e);
        }
        catch (Exception e) {
            assert false;
        }
    }

    /**
     * @throws Exception If failed.
     */
    public void testScanQuery() throws Exception {
        IgniteCache<Integer, String> cache = jcache();

        try {
            Collection<Cache.Entry<Integer, String>> res =
                cache.query(new ScanQuery<>(new IgniteBiPredicate<Integer, String>() {
                    @Override public boolean apply(Integer id, String s) {
                        return s.equals("");
                    }
                })).getAll();

        }
        catch (IgniteException e) {
            assert false;
        }
    }

    /**
     * Value object class.
     */
    private static class SqlValue {
        /**
         * Value.
         */
        @QuerySqlField
        private final int val;

        /**
         * @param val Value.
         */
        SqlValue(int val) {
            this.val = val;
        }

        /**
         * @return Value.
         */
        int value() {
            return val;
        }

        /**
         * {@inheritDoc}
         */
        @Override public String toString() {
            return S.toString(SqlValue.class, this);
        }
    }
}
