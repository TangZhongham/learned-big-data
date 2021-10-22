#
# Copyright 2010 The Apache Software Foundation
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

include Java

module Hbase
  class IndexRebuilder
    include HBaseConstants

    def initialize(configuration, formatter)
      @conf = configuration
      @index_rebuilder = org.apache.hadoop.hyperbase.mapreduce.IndexRebuilder.new(configuration)
      @formatter = formatter
    end

    #----------------------------------------------------------------------------------------------
    # rebuild a global index
    def rebuild_global_index(primary_table, index_name, token)
      @index_rebuilder.rebuildGlobalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_name.to_java_bytes, token)
    end

    #----------------------------------------------------------------------------------------------
    # rebuild a global index with range
    def rebuild_global_index_with_range(primary_table, index_name, start_key, end_key, encoder, token)
      if "string".eql?(encoder.strip.downcase)
        @index_rebuilder.rebuildGlobalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_name.to_java_bytes, start_key.to_java_bytes, end_key.to_java_bytes, token)
      elsif "hex".eql?(encoder.strip.downcase)
        @index_rebuilder.rebuildGlobalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_name.to_java_bytes,org.apache.hadoop.hbase.util.Bytes.fromHex(start_key), org.apache.hadoop.hbase.util.Bytes.fromHex(end_key), token)
      elsif "binary".eql?(encoder.strip.downcase)
        @index_rebuilder.rebuildGlobalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_name.to_java_bytes,org.apache.hadoop.hbase.util.Bytes.toBytesBinary(start_key), org.apache.hadoop.hbase.util.Bytes.toBytesBinary(end_key), token)
      else
        raise "IllegalArgument Exception. encoder can only be string/hex/binary"
      end
    end

    #----------------------------------------------------------------------------------------------
    # rebuild a global index with timerange
    def rebuild_global_index_with_timerange(primary_table, index_name, start_timestamp, end_timestamp, indexDisableDuringRebuild, token)
      @index_rebuilder.rebuildGlobalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_name.to_java_bytes, start_timestamp, end_timestamp, indexDisableDuringRebuild, token)
    end

    #----------------------------------------------------------------------------------------------
    # rebuild a local index
    def rebuild_local_index(primary_table, index_family)
      @index_rebuilder.rebuildLocalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_family.to_java_bytes)
    end

    #----------------------------------------------------------------------------------------------
    # rebuild a local index with range
    def rebuild_local_index_with_range(primary_table, index_family,start_key, end_key, encoder)
      if "string".eql?(encoder.strip.downcase)
        @index_rebuilder.rebuildLocalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_family.to_java_bytes, start_key.to_java_bytes, end_key.to_java_bytes)
      elsif "hex".eql?(encoder.strip.downcase)
        @index_rebuilder.rebuildLocalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_family.to_java_bytes,org.apache.hadoop.hbase.util.Bytes.fromHex(start_key), org.apache.hadoop.hbase.util.Bytes.fromHex(end_key))
      elsif "binary".eql?(encoder.strip.downcase)
        @index_rebuilder.rebuildLocalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_family.to_java_bytes,org.apache.hadoop.hbase.util.Bytes.toBytesBinary(start_key), org.apache.hadoop.hbase.util.Bytes.toBytesBinary(end_key))
      else
        raise "IllegalArgument Exception. encoder can only be string/hex/binary"
      end
    end

    #----------------------------------------------------------------------------------------------
    # rebuild fulltext index
    def rebuild_fulltext_index(primary_table, token)
      @index_rebuilder.rebuildFulltextIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), token)
    end

    #----------------------------------------------------------------------------------------------
    # rebuild fulltext index with range
    def rebuild_fulltext_index_with_range(primary_table, start_key, end_key, encoder, token)
      if "string".eql?(encoder.strip.downcase)
        @index_rebuilder.rebuildFulltextIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), start_key.to_java_bytes, end_key.to_java_bytes, token)
      elsif "hex".eql?(encoder.strip.downcase)
        @index_rebuilder.rebuildFulltextIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), org.apache.hadoop.hbase.util.Bytes.fromHex(start_key), org.apache.hadoop.hbase.util.Bytes.fromHex(end_key), token)
      elsif "binary".eql?(encoder.strip.downcase)
        @index_rebuilder.rebuildFulltextIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), org.apache.hadoop.hbase.util.Bytes.toBytesBinary(start_key), org.apache.hadoop.hbase.util.Bytes.toBytesBinary(end_key), token)
      else
        raise "IllegalArgument Exception. encoder can only be string/hex/binary"
      end
    end

    #----------------------------------------------------------------------------------------------
    # rebuild fulltext index with timerange 
    def rebuild_fulltext_index_with_timerange(primary_table, start_timestamp, end_timestamp, indexDisableDuringRebuild, token)
      @index_rebuilder.rebuildFulltextIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), start_timestamp, end_timestamp, indexDisableDuringRebuild, token)
    end

  end
end
