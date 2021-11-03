from faker import Faker
from faker.providers import BaseProvider

import sys,os
sys.path.append(os.getcwd())
from BiProvider import BiProvider
import faker.providers.person

fake = Faker('zh_CN')

bi = BiProvider("test",1, 2)

if __name__ == '__main__':
    for _ in range(10):
        print(bi.get_name())
        print(fake.name())
        print(fake.address())
        print(fake.company())
        print(fake.job())
        print(fake.phone_number())
        print(fake.currency())
        print(fake.color())
        print(
            'xxxxxxxxxxxxXXXXXXXXXXX\n\n\n\n'
        )
        # from faker.providers import internet
        #
        # fake = Faker('zh_CN')
        # fake.add_provider(internet)
        #
        # print(fake.ipv4_private())

        # 3 多线程 提速
