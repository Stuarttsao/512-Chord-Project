# P2Pass: A Distributed Password Storage System Using Peer-to-Peer Lookup with Chord
By: Brandon Bae, Havish Malladi, Edison Ooi, Stuart Tsao

P2Pass is a novel distributed password storage system that
uses the Chord framework to enhance the security of password storage. P2Pass incorporates two main components to
ensure enhanced security - shredding, hashing, and salting passwords, and inserting different segments into different Chord nodes. The results support that our system, when a hacker only breaks
a part of the Chord network, is more secure than if a hacker
purely brute forces a password in a centralized system.

P2Pass was built as our final research project for CS512: Distributed Systems at Duke University. You can read our full paper [here](P2Pass_Final_Report.pdf).

## Chord
The Chord framework serves one purpose: Given a key, return the ID of the node it belongs to. Chord is peer-to-peer, meaning that any node in the ring can be queried to find the location of any key. It implements a circular key-space organized as a ring, where node ID's and keys are consistently hashed into the same key-space. Keys belong to its successor node, the first node encountered while traversing the ring starting from the key's hash. Its implementation also involves finger tables, which allow key location lookups to have a time complexity of O(log N). For more information about Chord, please refer to the [Chord Paper](https://pdos.csail.mit.edu/papers/ton:chord/paper-ton.pdf).

![Chord Finger Table](pictures/Chord%20Finger%20Table.png)

P2Pass was built on top of an existing Chord implementation done by former Duke graduate student Chuan Xia. We thank them for their wonderful implementation and willingness to open-source the codebase. You can find their implementation [here](https://github.com/ChuanXia/Chord). 

## Usage
To run P2Pass locally, you will first need to clone the repository, and then create a Chord ring as well as an instance of `MainServer`. The Chord ring will contain an arbitrary number of nodes where key-value pairs will be stored. The MainServer is the interface through which users can create and verify username-password entries, using the `createPasswordEntry` and `verifyPassword` commands.

### Creating a Chord Ring

**Compile**

Open terminal, change directory to **Chord**.


	cd /Users/.../src/main/java/Chord


Now you can compile it using the Makefile! Just type `make`.

	
Hopefully now you have all ***.class** files in your current directory.

**Run**

	- Create a Chord ring

			java Chord 8001
	  
	  Note here the C for Chord is uppercase, and 8050 is the port that you want this node to listen to.	

	  
	  Hopefully you are going to see something like this:

	  
	  		Joining the Chord ring.

			Local IP: 10.190.92.156


			You are listening on port 8001.

			Your position is 8459f9fa (51%).

			Your predecessor is yourself.

			Your successor is yourself.


	
	- Join an existing ring

	
			java Chord 8010 10.190.92.156 8001

	
	  This means you are creating a node that listen to port 8051 and it is joining a ring containing 10.190.92.156:8001.

	  
	 
	  If the input is right and the port 8010 is not occupied, you will see the following lines:

	  
	  
	  		Joining the Chord ring.

			Local IP: 10.190.92.156


			You are listening on port 8010.

			Your position is 1ac96434 (10%).

			Your predecessor is updating.

			Your successor is node /10.190.92.156, port 8001, position 8459f9fa (51%).

	  After you create a node, you could input `info` at any time to check this node's socket address, predecessor and finger table. You could also terminate this node and leave chord ring by inputing `quit` or just press ctrl+C.

### MainServer Creation
Creating an instance of `MainServer` is very similar to adding a node to an existing Chord ring, except that we will run the `MainServer` file.

			java MainServer 8011 10.190.92.156 8001
    
This creates a `MainServer` node on port 8011 and adds it to the existing Chord ring. This node will not be able to store any key-value information.

Upon execution, you should be able to see the appropriate prompts in the terminal to create or verify username-password entries.