import unittest

import reader
from knowledge_base import KnowledgeBase


class KnowledgeBaseTest(unittest.TestCase):

    def setUp(self):
        self.data = reader.parse_file('data.txt')
        self.knowledge_base = KnowledgeBase()
        for item in self.data:
            self.knowledge_base.add(item)

    def test1(self):
        ask1 = reader.parse_input("fact: (motherof ada ?X)")
        answer = self.knowledge_base.ask(ask1)
        self.assertEqual(str(answer[0]), "?X : bing")

    def test2(self):
        ask1 = reader.parse_input("fact: (grandmotherof ada ?X)")
        answer = self.knowledge_base.ask(ask1)
        self.assertEqual(str(answer[0]), "?X : felix")
        self.assertEqual(str(answer[1]), "?X : chen")

    def test3(self):
        ask1 = reader.parse_input("fact: (motherof eva ?X)")
        answer = self.knowledge_base.ask(ask1)
        self.assertEqual(str(answer[0]), "?X : bing")


if __name__ == '__main__':
    unittest.main()
