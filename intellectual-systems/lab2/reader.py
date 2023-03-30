from definitions import *


def parse_file(file):
    with open(file, 'r') as file:
        elements = []
        current = ""
        for line in file:
            if line[0:5] in ("fact:", "rule:"):
                elements.append(current)
                current = line.rstrip()
            else:
                current = current + " " + line.rstrip().strip()
        elements.append(current)
        output = []
        for e in elements:
            parsed = parse_input(e)
            if isinstance(parsed, Fact) or isinstance(parsed, Rule):
                output.append(parsed)
        return output


def parse_input(e):
    if len(e) == 0:
        return None
    elif e[0] == '#':
        return e[1:]
    elif e[0:5] == "fact:":
        e = e[5:].replace(")", "").replace("(", "").rstrip().strip().split()
        return Fact(e)
    elif e[0:5] == "rule:":
        e = e[5:].split("->")
        rhs = e[1].replace(")", "").replace("(", "").rstrip().strip().split()
        lhs = e[0].rstrip(") ").strip("( ").replace("(", "").split(")")
        lhs = map(lambda x: x.rstrip().strip().split(), lhs)
        return Rule([lhs, rhs])
    else:
        print("PARSE ERROR: input header", e[0:5], "not recognized.")
