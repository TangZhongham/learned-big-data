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
    class AlterUseJson < Command
      def help
        return <<-EOF
Alter a table using json description. 
Examples:
  hbase> alterUseJson 't2', '{"tableName":"t2","base":{"f":{"DATA_BLOCK_ENCODING":"NONE","BLOOMFILTER":"ROW","REPLICATION_SCOPE":0,"VERSIONS":1,"COMPRESSION":"NONE","MIN_VERSIONS":0,"TTL":2147483647,"BLOCKSIZE":65536,"IN_MEMORY":false,"BLOCKCACHE":true}}'
  hbase> alterUseJson 't2', '{"tableName":"t2","base":{"f":{"DATA_BLOCK_ENCODING":"NONE","BLOOMFILTER":"ROW","REPLICATION_SCOPE":0,"VERSIONS":1,"COMPRESSION":"NONE","MIN_VERSIONS":0,"TTL":2147483647,"BLOCKSIZE":65536,"IN_MEMORY":false,"BLOCKCACHE":true}}','whetherUseHexInSplitKey'
  hbase> alterUseJson 't2','/tmp/json.txt'
  hbase> alterUseJson 't2','/tmp/json.txt','whetherUseHexInSplitKey'
EOF
      end

      def command(table,*args)
	json = ""
        useFile = false
        jsonPattern = /^\{.*\}$/
	if jsonPattern =~ args[0]
	  json = args[0]
	else 
          useFile = true
          if !File.exist?(args[0].strip.chomp)
             puts "FileNotFoundException : your input file " + args[0] + " does not exists!"
             puts help
             return
          end
	  f = File.new(args[0],"r")
	  f.each {|line| json+= line.strip.chomp }
          f.close() 
	end
	useHex = false
	autoYes = false
	if args.size > 1
	  if "true".eql?(args[1].strip.downcase)
	    useHex = true
	  end
	  if "-y".eql?(args[1].strip.downcase)
	    autoYes = true
	  end
	end
	descriptorAdmin.updateDescribeInJson(table,json,useHex,autoYes)
      end
    end
  end
end
