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
    class DescribeInJson < Command
      def help
        return <<-EOF
Describe the named table. For example:

Useage: describeInJson 'tableName','useFormat','outputJsonFile','wetherUseHexInSplitKeys'
  hbase> describeInJson 't1'
  hbase> describeInJson 'ns1:t1'
  hbase> describeInJson 't1','true'
  hbase> describeInJson 't1','true','/tmp/json.txt'
  hbase> describeInJson 't1','true','/tmp/json.txt','true'
EOF
      end

      # describeInJson 'tableName' [,'true|false'] [,'true|false','/tmp/tmp_json.txt']
      # describeInJson tableName,useFormat,useFile,filePath
      def command(table,*args)
	now = Time.now
	useHex = false
	if args.size > 2 and "true".eql?(args[2].strip.downcase)
	  useHex = true
	end	
	if args.size > 0 and "true".eql?(args[0].strip.downcase) # use format
	  desc = descriptorAdmin.describeInJson(table,true,useHex)
        else # not use format
	  desc = descriptorAdmin.describeInJson(table,false,useHex)
	end
	if args.size > 1 # use file
	  f = File.new(args[1].strip.chomp,"w+")
	  f.puts(desc)
	  desc = "describe "+table + " to " + args[1] +" finished!"
          f.close() 
	end
        formatter.header([ "DESCRIPTION", "ENABLED" ], [ 64 ])
        formatter.row([ desc, admin.enabled?(table).to_s ], true, [ 64 ])
        formatter.footer(now)
      end
    end
  end
end
