# Decentralized P2P File Sharing System

## Compilation
This project is a simple implementation of a Decentralized P2P File Sharing System
with a Distributed Hashtable, using Java and Sockets.

To compile the project, you need [Apache Ant](http://ant.apache.org/). An then just:

```sh
$ ant clean & ant compile & ant jar
```

## Configuration
All the configuration of peers and and server is done in the [config.json](https://github.com/gmendonca/decentralize-p2p-file-sharing/blob/master/config.json) file.
This is a example how the file look like with 8 servers and 8 peers. In the provided example
it's using a single machine with different ports and folders, but it's possible to use ip address here too.

```json
{
	"peers":[
		"localhost:13000:peer0",
		"localhost:13001:peer1",
		"localhost:13002:peer2",
		"localhost:13003:peer3",
		"localhost:13004:peer4",
		"localhost:13005:peer5",
		"localhost:13006:peer6",
		"localhost:13007:peer7"
	],
	"servers":[
		"localhost:15000",
		"localhost:15001",
		"localhost:15002",
		"localhost:15003",
		"localhost:15004",
		"localhost:15005",
		"localhost:15006",
		"localhost:15007"
	]
}
```

## Running
To start a server, you need to run:

```sh
$ java -jar build/Deploy.jar <ServerId>
```

The ```<ServerId>``` will reflect the order specified in the config file. For example,
Server 0 will be "localhost:15000" in the example above.

A client could be started using:

```sh
$ java -jar build/Client.jar <PeerId>
```

This assumes that you have a ```peer<PeerId>``` folder with files.
There are some helpers scripts to automate the creation of folders
and files for testbeds on the folder [scripts](https://github.com/gmendonca/decentralize-p2p-file-sharing/tree/master/scripts).
Again, it reflects the order of the config file.

## Benchmark

There are two types of Benchmarking available on this project, a remote and a local one.

For running the local benchmarking, you need to run the following command:

```sh
$ java -jar build/LocalBench.jar <Bench Option> <Number of operations>
```

In this case, you don't need to run the server or the client separated,
it will create everything based on the config file.
However, you still need to provide the folder and files.
The files should be in the format ```file-p<PeerId>-0<Number>```, the script [createfiles.sh](https://github.com/gmendonca/decentralize-p2p-file-sharing/tree/master/scripts/createfiles.sh) create files using the this pattern. You can select the number of peers and files per peer and run it like that:

```sh
$ sh configfiles.sh <NUMPEERS> <NUMFILES>
```

The <Bench Option> is two different approaches for the Benchmarking, The ```0``` option is the normal
one that will benchmark registry, search and obtain files. The second one ```1``` it will benchmark just the download.


The remote benchmarking will do the same things as the local benchmark but it should run individually in each node.
For this, it's necessary to run the Server First and then the Remote Bench. To this to work, the server must be running in each node specified on the config file and each node has to have a copy of the config file.

```sh
$ java -jar build/Deploy.jar <ServerId> &
$ java -jar build/RemoteBench.jar <PeerId> <Number of operations>
```

For all the options you can specify how many operations of Registry, Search and Obtain files it will run.
