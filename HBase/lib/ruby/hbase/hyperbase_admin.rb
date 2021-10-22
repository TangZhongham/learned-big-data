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
  class HyperbaseAdmin
    include HBaseConstants

    def initialize(configuration, formatter)
      @conf = configuration
      @hyperbase_admin = org.apache.hadoop.hyperbase.client.HyperbaseAdmin.new(configuration)
      @formatter = formatter
    end

    #----------------------------------------------------------------------------------------------
    #  query, turn on or turn off  function 'client insert'
    def client_insert(table_name, qoo)
      table = org.apache.hadoop.hbase.TableName.valueOf(table_name)
      result = nil
      if(qoo == "on")
        @hyperbase_admin.setClientInsert(table, true)
      elsif(qoo == "off")
        @hyperbase_admin.setClientInsert(table,false)
      elsif(qoo != "query")
        result = "wrong args : " + qoo + " (only 'on','off','query' supported)"
      end
      if(result == nil)
        result=@hyperbase_admin.getClientInsert(table)
      end
      puts(result)
    end



    #----------------------------------------------------------------------------------------------
    # delete a global index
    def delete_global_index(primary_table, index_name)
      @hyperbase_admin.deleteGlobalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_name.to_java_bytes)
    end

    #----------------------------------------------------------------------------------------------
    # delete a local index
    def delete_local_index(primary_table, index_name)
      @hyperbase_admin.deleteLocalIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table), index_name.to_java_bytes)
    end

    #----------------------------------------------------------------------------------------------
    # delete fulltext index
    def delete_fulltext_index(primary_table)
      @hyperbase_admin.deleteFulltextIndex(org.apache.hadoop.hbase.TableName.valueOf(primary_table))
    end

    #----------------------------------------------------------------------------------------------
    # split a fulltext index partition
    def split_fulltext_index_partition(primary_table, max)                                                                                             
      @hyperbase_admin.splitFulltextIndexPartition(org.apache.hadoop.hbase.TableName.valueOf(primary_table), max)
    end

    #----------------------------------------------------------------------------------------------
    # truncate table with all index
    def truncate(primary_table)
      @hyperbase_admin.truncate(org.apache.hadoop.hbase.TableName.valueOf(primary_table))
    end

    #----------------------------------------------------------------------------------------------
    # delete table with all index
    def delete_table(primary_table)
      @hyperbase_admin.deleteTable(org.apache.hadoop.hbase.TableName.valueOf(primary_table), true)
    end

    #----------------------------------------------------------------------------------------------
    # add global index , make this to compactiable with original add_index
    def add_global_index(tableName, index_name, conf_str)
      index = org.apache.hadoop.hyperbase.secondaryindex.SecondaryIndexUtil.createSecondaryIndexFromPattern(conf_str)
      table = org.apache.hadoop.hbase.TableName.valueOf(tableName)
      @hyperbase_admin.addGlobalIndex(table, index, index_name.to_java_bytes, nil, true)
    end

    #----------------------------------------------------------------------------------------------
    # Creates a table
    def create_table(table_name, *args)
      # Fail if table name is not a string
      raise(ArgumentError, "Table name must be of type String") unless table_name.kind_of?(String)

      # Flatten params array
      args = args.flatten.compact
      has_columns = false

      # Start defining the table
      htd = nil
      schema = nil
      splits = nil
      # Args are either columns or splits, add them to the table definition
      # TODO: add table options support
      args.each do |arg|
        unless arg.kind_of?(String) || arg.kind_of?(Hash)
          raise(ArgumentError, "#{arg.class} of #{arg.inspect} is not of Hash or String type")
        end

        # First, handle all the cases where arg is a column family.
        if arg.kind_of?(String)
          # If the arg is a string, default action is to add a column to the table.
          # If arg has a name, it must also be a column descriptor.
          htd = org.apache.hadoop.hyperbase.util.SchemaUtil::tblDesc(table_name, arg)
          schema = org.apache.hadoop.hyperbase.util.SchemaUtil::genSchema(arg)
          has_columns = true
          next
        end

        # The hash is not a column family. Figure out what's in it.
        # First, handle splits.
        if arg.has_key?(SPLITS_FILE)
          splits_file = arg.delete(SPLITS_FILE)
          unless File.exist?(splits_file)
            raise(ArgumentError, "Splits file #{splits_file} doesn't exist")
          end
          arg[SPLITS] = []
          File.foreach(splits_file) do |line|
            arg[SPLITS].push(line.strip())
          end
          htd.setValue(SPLITS_FILE, arg[SPLITS_FILE])
        end

        if arg.has_key?(SPLITS)
          splits = Java::byte[][arg[SPLITS].size].new
          idx = 0
          arg.delete(SPLITS).each do |split|
            splits[idx] = org.apache.hadoop.hbase.util.Bytes.toBytesBinary(split)
            idx = idx + 1
          end
        elsif arg.has_key?(NUMREGIONS) or arg.has_key?(SPLITALGO)
          # deprecated region pre-split API; if one of the above is specified, will be ignored.
          raise(ArgumentError, "Number of regions must be specified") unless arg.has_key?(NUMREGIONS)
          raise(ArgumentError, "Split algorithm must be specified") unless arg.has_key?(SPLITALGO)
          raise(ArgumentError, "Number of regions must be greater than 1") unless arg[NUMREGIONS] > 1
          num_regions = arg.delete(NUMREGIONS)
          split_algo = RegionSplitter.newSplitAlgoInstance(@conf, arg.delete(SPLITALGO))
          splits = split_algo.split(JInteger.valueOf(num_regions))
        end

        # Done with splits; apply formerly-table_att parameters.
        htd.setOwnerString(arg.delete(OWNER)) if arg[OWNER]
        htd.setMaxFileSize(JLong.valueOf(arg.delete(MAX_FILESIZE))) if arg[MAX_FILESIZE]
        htd.setReadOnly(JBoolean.valueOf(arg.delete(READONLY))) if arg[READONLY]
        htd.setCompactionEnabled(JBoolean.valueOf(arg[COMPACTION_ENABLED])) if arg[COMPACTION_ENABLED]
        htd.setMemStoreFlushSize(JLong.valueOf(arg.delete(MEMSTORE_FLUSHSIZE))) if arg[MEMSTORE_FLUSHSIZE]
        htd.setAsyncLogFlush(JBoolean.valueOf(arg.delete(DEFERRED_LOG_FLUSH))) if arg[DEFERRED_LOG_FLUSH]
        htd.setDurability(org.apache.hadoop.hbase.client.Durability.valueOf(arg.delete(DURABILITY))) if arg[DURABILITY]
        set_user_metadata(htd, arg.delete(METADATA)) if arg[METADATA]
        set_descriptor_config(htd, arg.delete(CONFIGURATION)) if arg[CONFIGURATION]

        arg.each_key do |ignored_key|
          puts("An argument ignored (unknown or overridden): %s" % [ ignored_key ])
        end
      end

      # Fail if no column families defined
      raise(ArgumentError, "Table must have at least one column family") if !has_columns

      if splits.nil?
        # Perform the create table call
        @hyperbase_admin.createTable(htd, schema)
      else
        # Perform the create table call
        @hyperbase_admin.createTable(htd, schema, splits)
      end
    end

    #-----------------------------------------------------------------------------------------------
    # get metadata
    def get_hyperbase_schema(primary_table)
     @hyperbase_admin.getTableMetadata(org.apache.hadoop.hbase.TableName.valueOf(primary_table))
    end
  end
  #----------------------------------------------------------------------------------------------

end
