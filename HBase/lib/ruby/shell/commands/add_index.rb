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
    class AddIndex < Command
      def help
        return <<-EOF
Add a global index for an exist table. Deprecated,please use add_global_index instead.
Examples:

  hbase> add_index 't1', 'index_name, 'COMBINE_INDEX|INDEXED=f1:q1:9|rowKey:rowKey:10,UPDATE=true'
EOF
      end

      def command(table, index_name, conf_str)
        format_simple_command do
          hyperbase_admin.add_global_index(table, index_name, conf_str)
        end
      end
    end
  end
end