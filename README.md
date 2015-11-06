# Decentralized P2P File Sharing System

This project is a simple implementation of a Decentralized P2P File Sharing System
with a Distributed Hashtable, using Java and Sockets.

To compile the project, you need [Apache Ant](http://ant.apache.org/). An then just:

```sh
$ ant clean & ant compile & ant jar
```

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

To start a server, you need to run:

```sh
& java -jar build/Deploy.jar <ServerId>
```

The ```<ServerId>``` will reflect the order specified in the config file. For example,
Server 0 will be "localhost:15000" in the example above.

A client could be started using:

```sh
& java -jar build/Client.jar <PeerId>
```

This assumes that you have a peer0 folder with files.
There are some helpers scripts to automate the creation of folders
and files for testbeds on the folder [scripts](https://github.com/gmendonca/decentralize-p2p-file-sharing/tree/master/scripts).
