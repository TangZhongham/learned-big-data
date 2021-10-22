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

module Shell
  module Commands
    class RebuildLocalIndexWithRange < Command
      def help
        return <<-EOF
Rebuild an exist local index.
Usage: rebuild_local_index_with_range 't1', 'index_family', 'start_key', 'stop_key','encoder'
encoder can be: string,hex,binary

Examples:
     default using string encode startkey/stopkey
  hbase> rebuild_local_index_with_range 't1','index_family',  'rowkey1', 'rowkey2'
  hbase> rebuild_local_index_with_range 't1','index_family',  'rowkey1', 'rowkey2','string'
     use binary encoder and startkey/stopkey are copied from 60010 table links page or other
  hbase> rebuild_local_index_with_range 't1','index_family',  '\\x00\\x00\\x00\\x00\\x1D', '\\x00\\xC1\\xF2\\xF1\\x8C','binary'
  hbase> rebuild_local_index_with_range 't1','index_family',  '\\x000.07593071128814799', '\\x002.4329335438689883','binary'
     use hex encoder and startkey='abc',stopkey='xyz',than hex encoded abc => 616263, xyz => 78797a
  hbase> rebuild_local_index_with_range 't1','index_family',  '616263', '78797a','hex'


EOF
      end

      def command(table, index_family,start_key,end_key, encoder = "string")
        format_simple_command do
          index_rebuilder.rebuild_local_index_with_range(table, index_family,start_key, end_key, encoder)
        end
      end
    end
  end
end
