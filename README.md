# generator-boot-seed

> A generator for the Seed microservices projects.

## Installation
Install node: [node.js](https://nodejs.org/))

Then install Yeoman and the generator with the following command.

```bash
npm install -g yo
```

## Initiation

Git clone this repository into a folder on your computer.

cd into the generator-seed-boot folder.

Run the following to install and link the generator:
```
npm install
npm link
```

You have now successfully added the generator to your environment.

## Create Your First Repo

Create an empty folder somewhere on your machine.

Cd into the folder.

Then generate your new project:

```bash
yo boot-seed
This will install the base project

```bash
yo boot-seed:datastore
```
Pick JPA and a JPA datastore will be added.

```bash
yo boot-seed:data 
```
Add a noun(Taco) and create an entity but not a field.

```bash
yo boot-seed:stack
```
Adds the plumbing. 

```
gradle clean build bootrun
```
To build & run the application.


Once it is running navigate to any of the following endpoints:
localhost:<PORT>/health
localhost:<PORT>/metrics
localhost:<PORT>/swagger-ui.html

To load it in your ide do the following:

Eclipse:
```
gradle cleaneclipse eclipse
```

Intelij
```
gradle cleanidea idea
```

## Run the seed generator into a Docker Container
To build the image:

```bash
cd generator-boot-seed
docker build -t generator-boot-seed -f Dockerfile.seed .
```

To run the image:
```bash
docker run -it -v /Users/<username>/seed_dest:/src generator-boot-seed:latest /bin/bash
```

Then you can jump on 'Create your own repo' section and type those instructions into the Container.
The application will be generated in the /app directory of the container. 
Then cp * -r your /app to your /src folder which is a volume mounted on your own computer.
And cp * -r your /usr/local/lib/node_modules/generator-boot-seed/node_modules to /src/node_modules.
