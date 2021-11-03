from faker.providers import BaseProvider
from random import choice, sample, randint, randrange
from faker import Faker

import datetime

import string


class BiProvider(BaseProvider):
    """
        A Provider for travel related test data.
        >>> from faker import Faker
        >>> fake = Faker()
        用于fake一系列表数据来进行性能模拟测试

        问题简化：
        可以主要数据尽量随机
        特殊查询数据特别构造插入
    """

    def __init__(self: object, table_name: str, filename: str, rownum: int, generator):
        super().__init__(generator)
        self.table_name = table_name
        self.faker = Faker('zh_CN')
        self.__filename__ = filename + ".csv"
        self.rownum = rownum / 5
        if self.rownum < int(1000):
            self.chunks = int(self.rownum)
            self.iters = int(1)
        else:
            self.chunks = int(1000)
            self.iters = int(self.rownum / 1000)

    # fake = Faker('zh_CN')

    def get_date(self, year=2021, month=1, day=1):
        """
        构造时间参数，需要在一定范围内
        """
        start_date = datetime.date(year, month, day)
        end_date = datetime.date(year, month+11, day)

        time_between_dates = end_date - start_date
        days_between_dates = time_between_dates.days
        random_number_of_days = randrange(days_between_dates)
        random_date = start_date + datetime.timedelta(days=random_number_of_days)

        return random_date

    def get_name(self):
        return self.faker.name()

    def get_company(self):

        return self.faker.company()

    def get_rdm_string(self, size=8, chars=string.ascii_uppercase + string.digits):
        """
        获取随机的String 去填充不需要的值
        """
        # goal = ''.join(sample(string.ascii_letters + string.digits + string.punctuation, 8))
        goal = ''.join(choice(chars) for _ in range(size))
        return goal

    def get_rdm_int(self):
        """
        用于获取随机的 Int 去填充不重要的值
        """
        rdm = randint(1000000, 99999999)
        return rdm

    def one_liner(self):
        return

import os


def get_FileSize(filePath):
        fsize = os.path.getsize(filePath)
        fsize = fsize/float(1024*1024)
        return round(fsize,2)

if __name__ == '__main__':
    # TODO: 真随机数据生成速度太慢太慢，必须简化比方说主要的数据从50条内抽选，一定要随机的才随机

    # 完全不随机 生成 216618 条数据总耗时： 19.546558141708374 文件大小 9.9
    # 真随机的数据简直不能看
    import time
    import threading

    def check():
        for _ in range(10000):
            # xx = BiProvider("T_PUB_CUST_CPTL_PRFT", "test",1, 2)
            # print(xx.get_date())
            # print(xx.get_rdm_int())
            # print(xx.get_rdm_string())
            # print(xx.get_rdm_int())
            # T_PUB_CUST_CPTL_PRFT = [xx.get_name(),xx.get_rdm_int(), xx.get_date(), xx.get_rdm_string(), xx.get_rdm_string(), '\n']
            # one_line = ",".join([str(i) for i in T_PUB_CUST_CPTL_PRFT])
            # print(one_line)
            one_line = "马秀梅,79886627,2021-09-11,DUMWEJ8C,8NVJY1D6\n"
            with open("test.txt", "a") as f:
                f.writelines(one_line)
    time_start=time.time()

    try:
        for i in range(20):
            t = threading.Thread(target=check())
            t.start()

    finally:
        time_end = time.time()
        count = len(open("./test.txt", 'rU').readlines())
        print("生成", str(count), "条数据总耗时：", time_end-time_start, "文件大小", get_FileSize("./test.txt"))