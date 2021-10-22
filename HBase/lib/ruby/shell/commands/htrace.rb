
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
    class Htrace < Command
      def help
        return <<-EOF
        
  Usage: command, serverNameList(delimited by semicolon)
  Example:

  hbase> htrace 'status'
  hbase> htrace 'enable'
  hbase> htrace 'disable'
  hbase> htrace 'status', 'demo1,60000,1557911960436;demo1,60000,1557911960436'
  hbase> htrace 'enable', 'demo1,60000,1557911960436;demo1,60000,1557911960436'
  hbase> htrace 'disable','demo1,60000,1557911960436;demo1,60000,1557911960436'

        EOF
      end

      def command(qed='status',servers='all')
        htrace(qed, servers)
      end

      def htrace(qed, servers)
        admin.htrace(qed,servers)
      end
    end
  end
end
