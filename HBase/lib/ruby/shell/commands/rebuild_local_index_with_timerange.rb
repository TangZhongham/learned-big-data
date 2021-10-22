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
    class RebuildLocalIndexWithTimerange < Command
      def help
        return <<-EOF
Rebuild an exist local index.
Examples:

  hbase> rebuild_local_index_with_timerange 't1','index_name','start_timestamp', 'end_timestamp', 'indexDisableDuringRebuild'
EOF
      end

      def command(table, index_name, start_timestamp, end_timestamp, indexDisable = "true")
        format_simple_command do

          if "true".eql?(indexDisable.strip.downcase) # disable index during rebuilding
             index_rebuilder.rebuild_local_index_with_timerange(table, index_name, start_timestamp, end_timestamp, true)
          else # not disable index
             index_rebuilder.rebuild_local_index_with_timerange(table, index_name, start_timestamp, end_timestamp, false)
         	end
        end
      end
    end
  end
end
