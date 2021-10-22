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

include Java

# Wrapper for org.apache.hadoop.hyperbase.newdescriptor.TableDescriptorManager

module Hbase
  class DescriptorAdmin
    include HBaseConstants

    def initialize(configuration,formatter)
      @objectMapper = org.codehaus.jackson.map.ObjectMapper.new()
      @conf = configuration
      @formatter = formatter
    end

    #----------------------------------------------------------------------------------------------
    # Returns describes of table , using json string format
    def describeInJson(tableName,whetherUseFormat = false,whetherUseHexToParseSplitKey = false)
      table_name = org.apache.hadoop.hbase.TableName.valueOf(tableName)
      @conf.setBoolean("hyperbase.json.descriptor.outputhex", whetherUseHexToParseSplitKey)
      manager = org.apache.hadoop.hyperbase.newdescriptor.TableDescriptorManager.new(table_name,@conf)
      if whetherUseFormat
	json = @objectMapper.defaultPrettyPrintingWriter().writeValueAsString(manager)
      else 
	json = @objectMapper.writeValueAsString(manager)
      end
    end

    #----------------------------------------------------------------------------------------------
    # verify tables modify , check if user want to alter this table's descriptor
    def updateDescribeInJson(tableName,jsonStr,whetherUseHexToParseSplitKey = false, autoYes = false)
      table_name = org.apache.hadoop.hbase.TableName.valueOf(tableName)
      @conf.setBoolean("hyperbase.json.descriptor.outputhex", whetherUseHexToParseSplitKey)
      manager = org.apache.hadoop.hyperbase.newdescriptor.TableDescriptorManager.new(table_name,@conf)
      begin # rescue json parse exception 
        manager = @objectMapper.updatingReader(manager).readValue(jsonStr);
        rescue Exception => e
          puts $!
          return
      end # end rescue block
      if autoYes # auto yes 
	 manager.update()
         return 
      end
      puts "------ change will be: ------"
      changeExists = false
      num = 1
      manager.getVerifyResults().each do |result|
        puts num.to_s + ". " + result.getVerifyResult().name() 
        puts result.getVerifyDescription()
        if result.getVerifyResult().name().eql?(org.apache.hadoop.hyperbase.newdescriptor.UpdateVerifyResult::VERIFY_RESULT::NEED_DOUBLE_CHECK.name())
           changeExists = true 
        end
        num = num + 1 
      end # end each
      if !changeExists
	puts 
	puts "------ operation result [SUCC] do nothing due to nothing is changed! ------"
	return
      end
      puts
      puts "are you sure to alter table? Y/N"
      sureToChange = gets
      if "Y".eql?(sureToChange.strip.upcase)
	puts "It may take a while , please wait !"
	manager.update()
	puts "------ operation result [SUCC] alter table descriptor complete ! ------"
      else
	puts "------ operation result [SUCC] alter table descriptor cancel ! ------"
      end # end if
    end # end def updateDesc...

  end # end class
end # end module
