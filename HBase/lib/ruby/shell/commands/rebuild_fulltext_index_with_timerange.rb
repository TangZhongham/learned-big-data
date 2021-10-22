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
    class RebuildFulltextIndexWithTimerange < Command
      def help
        return <<-EOF
Rebuild an exist fulltext index.
Examples:

  hbase> rebuild_fulltext_index_with_timerange 't1', 'start_timestamp', 'end_timestamp', 'indexDisableDuringRebuild'
For cluster that security is turned on, a special federation token should be obtained from guardian federation module and passed to rebuild command:
  hbase> rebuild_fulltext_index_with_timerange 't1', 'start_timestamp', 'end_timestamp', 'indexDisableDuringRebuild', 'token'
EOF
      end

      def command(table, start_timestamp, end_timestamp, indexDisable = "true", token=nil)
        format_simple_command do
	  if "true".eql?(indexDisable.strip.downcase) # disable index during rebuilding
            index_rebuilder.rebuild_fulltext_index_with_timerange(table, start_timestamp, end_timestamp, true, token)
          elsif "false".eql?(indexDisable.strip.downcase)
            index_rebuilder.rebuild_fulltext_index_with_timerange(table, start_timestamp, end_timestamp, false, token)
          else
            raise "IllegalArgument Exception. indexDisable can only be true/false"
          end
        end
      end
    end
  end
end
