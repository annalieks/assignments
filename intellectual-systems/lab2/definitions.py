from util import is_var


class Fact():
    def __init__(self, statement, supported_by=None):
        super(Fact, self).__init__()
        if supported_by is None:
            supported_by = []
        self.statement = statement if isinstance(statement, Statement) else Statement(statement)
        self.asserted = not supported_by
        self.supported_by = []
        self.supports_facts = []
        self.supports_rules = []
        for pair in supported_by:
            self.supported_by.append(pair)

    def __repr__(self):
        return 'Fact({!r}, {!r}, {!r}, {!r}, {!r})'.format( self.statement,
            self.asserted, self.supported_by,
            self.supports_facts, self.supports_rules)

    def __eq__(self, other):
        return isinstance(other, Fact) and self.statement == other.statement


class Rule(object):
    def __init__(self, rule, supported_by=None):
        super(Rule, self).__init__()
        if supported_by is None:
            supported_by = []
        self.lhs = [statement if isinstance(statement, Statement) else Statement(statement) for statement in rule[0]]
        self.rhs = rule[1] if isinstance(rule[1], Statement) else Statement(rule[1])
        self.asserted = not supported_by
        self.supported_by = []
        self.supports_facts = []
        self.supports_rules = []
        for pair in supported_by:
            self.supported_by.append(pair)

    def __repr__(self):
        return 'Rule({!r}, {!r}, {!r}, {!r}, {!r}, {!r})'.format(
            self.lhs, self.rhs,
            self.asserted, self.supported_by,
            self.supports_facts, self.supports_rules)


class Statement(object):
    def __init__(self, statement_list=[]):
        super(Statement, self).__init__()
        self.terms = []
        self.predicate = ""

        if statement_list:
            self.predicate = statement_list[0]
            self.terms = [t if isinstance(t, Term) else Term(t) for t in statement_list[1:]]

    def __repr__(self):
        return 'Statement({!r}, {!r})'.format(self.predicate, self.terms)

    def __eq__(self, other):
        if self.predicate != other.predicate:
            return False

        for self_term, other_term in zip(self.terms, other.terms):
            if self_term != other_term:
                return False

        return True


class Term(object):
    def __init__(self, term):
        super(Term, self).__init__()
        is_var_or_const = isinstance(term, Variable) or isinstance(term, Constant)
        self.term = term if is_var_or_const else (Variable(term) if is_var(term) else Constant(term))

    def __repr__(self):
        return 'Term({!r})'.format(self.term)

    def __eq__(self, other):
        return (self is other
                or isinstance(other, Term) and self.term.element == other.term.element
                or ((isinstance(other, Variable) or isinstance(other, Constant))
                    and self.term.element == other.element))


class Variable(object):
    def __init__(self, element):
        super(Variable, self).__init__()
        self.element = element

    def __repr__(self):
        return 'Variable({!r})'.format(self.element)

    def __eq__(self, other):
        return (self is other
                or isinstance(other, Term) and self.term.element == other.term.element
                or ((isinstance(other, Variable) or isinstance(other, Constant))
                    and self.term.element == other.element))


class Constant(object):
    def __init__(self, element):
        super(Constant, self).__init__()
        self.element = element

    def __repr__(self):
        return 'Constant({!r})'.format(self.element)

    def __eq__(self, other):
        return (self is other
                or isinstance(other, Term) and self.term.element == other.term.element
                or ((isinstance(other, Variable) or isinstance(other, Constant))
                    and self.term.element == other.element))


class Binding(object):
    def __init__(self, variable, constant):
        super(Binding, self).__init__()
        self.variable = variable
        self.constant = constant

    def __repr__(self):
        return 'Binding({!r}, {!r})'.format(self.variable, self.constant)

    def __str__(self):
        return self.variable.element.upper() + " : " + self.constant.element


class Bindings():
    def __init__(self):
        self.bindings = []
        self.bindings_dict = {}

    def __repr__(self):
        return 'Bindings({!r}, {!r})'.format(self.bindings_dict, self.bindings)

    def __str__(self):
        if self.bindings == []:
            return "No bindings"
        return ", ".join((str(binding) for binding in self.bindings))

    def __getitem__(self, key):
        return (self.bindings_dict[key]
                if (self.bindings_dict and key in self.bindings_dict)
                else None)

    def add_binding(self, variable, value):
        self.bindings_dict[variable.element] = value.element
        self.bindings.append(Binding(variable, value))

    def bound_to(self, variable):
        if variable.element in self.bindings_dict.keys():
            value = self.bindings_dict[variable.element]
            if value:
                return Variable(value) if is_var(value) else Constant(value)

        return False

    def test_and_bind(self, variable_term, value_term):
        bound = self.bound_to(variable_term.term)
        if bound:
            return value_term.term == bound

        self.add_binding(variable_term.term, value_term.term)
        return True


class ListOfBindings(object):
    def __init__(self):
        super(ListOfBindings, self).__init__()
        self.list_of_bindings = []

    def __len__(self):
        return len(self.list_of_bindings)

    def __getitem__(self, key):
        return self.list_of_bindings[key][0]

    def add_bindings(self, bindings, facts_rules=[]):
        self.list_of_bindings.append((bindings, facts_rules))
