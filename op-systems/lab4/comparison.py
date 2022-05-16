import sys
import re
import subprocess

out_pattern = 'Counter value: .* Time spent: ([0-9]+)ns'
N = 100


def compare(exe_path: str):
    sum_a, sum_b = 0, 0
    for i in range(0, N):
        output_a = subprocess.check_output([f'{exe_path}\\lab4.exe', 'true']).decode(sys.stdout.encoding)
        output_b = subprocess.check_output([f'{exe_path}\\lab4.exe']).decode(sys.stdout.encoding)

        m = re.match(out_pattern, output_a)
        time_a = int(m.group(1))
        m = re.match(out_pattern, output_b)
        time_b = int(m.group(1))

        print(f'With sync: {time_a}, without sync: {time_b}')
        sum_a += time_a
        sum_b += time_b

    print(f'With sync avg: {sum_a / N}, without sync avg: {sum_b / N}')
    print(f'Performance drop with mutex: {sum_a / sum_b} times slower')


if __name__ == '__main__':
    path = sys.argv[1]
    compare(path)
