#
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

module Shell
  module Commands
    class CreateTable < Command
      def help
        return <<-EOF
Creates a table. Pass a table name, and a set of column family
specifications (at least one), and, optionally, table configuration.
Column specification can be a simple string (name), or a dictionary
(dictionaries are described below in main help output), necessarily
including NAME attribute.
Examples:

Create a table with namespace=default and table qualifier=t1
  hbase> # The above in shorthand would be the following:
  hbase> create_table 't1', ':key:string,f:c1:int,f:c3:struct<int,varchar(10), decimal(9,33), float, double, bigint>', 'f2', 'f3'

Table configuration options can be put at the end.
Examples:
':key:string,f:c1:int,f:c2:double,f:c3:varchar(10), f:c4:struct<int,varchar(10), decimal(9,33), string, float, double, bigint>'
  hbase> create_table 't1', ':key:string,f:c1:int,f:c3:struct<int,varchar(10), decimal(9,33), float, double, bigint>', SPLITS => ['10', '20', '30', '40']
  hbase> create_table 't1', ':key:string,f:c1:int,f:c3:struct<int,varchar(10), decimal(9,33), float, double, bigint>', SPLITS_FILE => 'splits.txt', OWNER => 'johndoe'
  hbase> # Optionally pre-split the table into NUMREGIONS, using
  hbase> # SPLITALGO ("HexStringSplit", "UniformSplit" or classname)
  hbase> create_table 't1', ':key:string,f:c1:int,f:c3:struct<int,varchar(10), decimal(9,33), float, double, bigint>', {NUMREGIONS => 15, SPLITALGO => 'HexStringSplit'}
  hbase> create_table 't1', ':key:string,f:c1:int,f:c3:struct<int,varchar(10), decimal(9,33), float, double, bigint>', {NUMREGIONS => 15, SPLITALGO => 'HexStringSplit', CONFIGURATION => {'hbase.hregion.scan.loadColumnFamiliesOnDemand' => 'true'}}

Which gives you a reference to the table named 't1', on which you can then
call methods.
EOF
      end

      def command(table, *args)
        format_simple_command do
          ret = hyperbase_admin.create_table(table, *args)
        end
        #and then return the table you just created
        table(table)
      end
    end
  end
end
