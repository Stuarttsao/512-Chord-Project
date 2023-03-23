# main.py
import sys
import os
if __name__ == "__main__":
    print(f"Arguments count: {len(sys.argv)}")
    num_nodes = sys.argv[1]
    start_port_num = 8001
    os.system("java Chord " + str(start_port_num) + " &")
    start_port_num += 1
    for i in range(int(num_nodes)):
        curr_port_num = (start_port_num + i)
        os.system("java Chord " + str(curr_port_num) + " 10.197.54.168 8001 &")
        """
    for i in range(int(num_nodes)):
        os.system("fg")
        os.system("quit")
    os.system("fg")
    os.system("quit")"""
