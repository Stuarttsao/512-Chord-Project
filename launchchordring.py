# main.py
import sys
import os
import subprocess
import signal
import time
if __name__ == "__main__":
    print(f"Arguments count: {len(sys.argv)}")
    num_nodes = sys.argv[1]
    start_port_num = 8001
    local_ip = "127.0.0.1"
    process_list=[]
    create_ring_process = subprocess.Popen(["java", "Chord", str(start_port_num)], shell=False)
    process_list.append(create_ring_process)
    ##os.system("java Chord " + str(start_port_num) + " &")
    start_port_num += 1
    for i in range(int(num_nodes)):
        curr_port_num = (start_port_num + i)
        curr_join_process = subprocess.Popen(["java", "Chord", str(curr_port_num), local_ip, str(start_port_num)], shell=False)
        process_list.append(curr_join_process)

    ##time.sleep(3)
    cmd_input = input('Kill?\n')
    if cmd_input is 'y':
        for p in process_list:
            pid = p.pid
            os.kill(pid, signal.SIGKILL)
