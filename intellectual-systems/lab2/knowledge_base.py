from definitions import *
from util import *

verbose = 0


class KnowledgeBase(object):
    def __init__(self):
        self.facts = []
        self.rules = []

    def add(self, item):
        if isinstance(item, Fact):
            self.add_fact(item)
        elif isinstance(item, Rule):
            self.add_rule(item)

    def add_fact(self, fact):
        if fact not in self.facts:
            self.facts.append(fact)
            for rule in self.rules:
                self.infer(fact, rule, self)
        else:
            if fact.supported_by:
                ind = self.facts.index(fact)
                for f in fact.supported_by:
                    self.facts[ind].supported_by.append(f)
            else:
                ind = self.facts.index(fact)
                self.facts[ind].asserted = True

    def add_rule(self, rule):
        if rule not in self.rules:
            self.rules.append(rule)
            for fact in self.facts:
                self.infer(fact, rule, self)
        else:
            if rule.supported_by:
                ind = self.rules.index(rule)
                for f in rule.supported_by:
                    self.rules[ind].supported_by.append(f)
            else:
                ind = self.rules.index(rule)
                self.rules[ind].asserted = True

    def ask(self, fact):
        if isinstance(fact, Fact):
            f = Fact(fact.statement)
            bindings_lst = ListOfBindings()
            for fact in self.facts:
                binding = match(f.statement, fact.statement)
                if binding:
                    bindings_lst.add_bindings(binding, [fact])

            return bindings_lst if bindings_lst.list_of_bindings else []

        else:
            print("Invalid question:", fact.statement)
            return []

    def infer(self, fact, rule, kb):
        bindings = match(rule.lhs[0], fact.statement)
        if not bindings:
            return None

        if len(rule.lhs) == 1:
            new_fact = Fact(instantiate(rule.rhs, bindings), [[rule, fact]])
            rule.supports_facts.append(new_fact)
            fact.supports_facts.append(new_fact)
            kb.add(new_fact)
        else:
            local_lhs = []
            local_rule = []
            for i in range(1, len(rule.lhs)):
                local_lhs.append(instantiate(rule.lhs[i], bindings))
            local_rule.append(local_lhs)
            local_rule.append(instantiate(rule.rhs, bindings))
            newrule = Rule(local_rule, [[rule, fact]])
            rule.supports_rules.append(newrule)
            fact.supports_rules.append(newrule)
            kb.add(newrule)
