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
    class Hput < Command
      def help
        return <<-EOF
Put a cell 'value' at specified table/row/column and optionally
timestamp coordinates.  To put a cell value into table 'ns1:t1' or 't1'
at row 'r1' under column 'c1' marked with the time 'ts1', do:

  hbase> hput 'ns1:t1', 'r1', 'c1', 'value'
  hbase> hput 't1', 'r1', 'c1', 'value'
  hbase> hput 't1', 'r1', 'c1', 'value', ts1
  hbase> hput 't1', 'r1', 'c1', 'value', {ATTRIBUTES=>{'mykey'=>'myvalue'}}
  hbase> hput 't1', 'r1', 'c1', 'value', ts1, {ATTRIBUTES=>{'mykey'=>'myvalue'}}
  hbase> hput 't1', 'r1', 'c1', 'value', ts1, {VISIBILITY=>'PRIVATE|SECRET'}

EOF
      end

      def command(table, row, column, value, timestamp=nil, args = {})
        hput(hyperbase_table(table), row, column, value, timestamp, args)
      end

      def hput(table, row, column, value, timestamp = nil, args = {})
        format_simple_command do
          table._put_internal(row, column, value, timestamp, args)
        end
      end
    end
  end
end

::Hbase::HyperbaseTable.add_shell_command("hput")
