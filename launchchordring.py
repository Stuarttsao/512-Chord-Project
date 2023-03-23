# main.py
import sys
import os
if __name__ == "__main__":
    print(f"Arguments count: {len(sys.argv)}")
    num_nodes = sys.argv[1]
    start_port_num = 8001
    os.system("java Chord " + start_port_num + " &")
    start_port_num += 1
    for i in range(num_nodes):
        curr_port_num = (start_port_num + i)
        os.system("java Chord " + curr_port_num + " 127.0.0.1 8001 &")
