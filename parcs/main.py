from Pyro4 import expose


class Solver:
    def __init__(self, workers=None, input_file_name=None, output_file_name=None):
        self.input_file_name = input_file_name
        self.output_file_name = output_file_name
        self.workers = workers
        print("Inited")

    def solve(self):
        print("Job Started")
        print("Workers %d" % len(self.workers))

        (a,b) = self.read_input()
        step_n = (b - a) / len(self.workers)
        step_left = (b - a) % len(self.workers)

        # map
        mapped = []
        for i in xrange(0, len(self.workers)):
            print("map %d" % i)
            if i == len(self.workers) - 1:
                mapped.append(self.workers[i].mymap(str(a + i * step_n), str(a + step_left + (i + 1) * step_n)))
            else:
                mapped.append(self.workers[i].mymap(str(a + i * step_n), str(a + (i + 1) * step_n)))

        primes = self.myreduce(mapped)
        self.write_output(primes)
        print("Job Finished")

    @staticmethod
    @expose
    def mymap(a, b):
        print(a)
        print(b)
        a = int(a)
        b = int(b)
        primes = []


        while  a < b:
            if Solver.armstrong_number(a):
                primes.append(str(a))
            a += 1

        return primes

    @staticmethod
    @expose
    def myreduce(mapped):
        print("reduce")
        output = []

        for primes in mapped:
            print("reduce loop")
            output = output + primes.value
        print("reduce done")
        return output

    def read_input(self):
        f = open(self.input_file_name, 'r')
        n = int(f.readline())
        k = int(f.readline())
        f.close()
        return n, k

    def write_output(self, output):
        f = open(self.output_file_name, 'w')
        f.write(', '.join(output))
        f.write('\n')
        f.close()
        print("output done")

    @staticmethod
    @expose
    def armstrong_number(number):
        result = 0
        snumber = str(number)
        l = len(snumber)
        for digit in snumber:
            result += int(digit) ** l
            if result > number:
                return False
        if result != number:
            return False
        return True