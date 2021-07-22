# [Cool](https://en.wikipedia.org/wiki/Cool_(programming_language)) Compiler

## Objective

Write a Cool (Classroom Object Oriented Language) compiler by following the instructions in the edX "Compilers" course.

The compiler will have 4 of the 5 parts described in the top level [README.md](../README.md). Optimization is left out (might add it later).

The compiler will be developed and tested inside a Docker container running Ubuntu 18.04. The `cool-compiler/shared` directory will be bind-mounted to the container's `cool` directory so that files can be dynamically edited on the host machine.

This project includes a Cool reference compiler (`coolc`) that will be used for testing.

## Completed assignments/compiler parts

1. [Lexical analyser](shared/lexer/)

## Docker container setup

1. Download the archive containing reference compiler binaries from [here](https://courses.edx.org/asset-v1:StanfordOnline+SOE.YCSCS1+1T2020+type@asset+block@student-dist.tar.gz).
2. Move downloaded `student-dist.tar.gz` archive to the `cool-compiler` directory. (Rename the archive if the its name is not `student-dist.tar.gz`).
3. Build Docker image: `$ ./build-image.sh`
4. Start a container from built Docker image: `$ ./run-container.sh`

## Commands (inside Docker container)

* `$ coolc <my-cool-file.cl>`: Compiles an assembly file from the given Cool file using the reference compiler.
* `$ spim <my-cool-file.s>`: Executes the compiled assembly file in a [SPIM](https://en.wikipedia.org/wiki/SPIM) simulator.   

## Credits

[Dockerfile](Dockerfile) was inspired by the [instructions](https://courses.edx.org/courses/course-v1:StanfordOnline+SOE.YCSCS1+3T2020/6b750292e90d4950b895f621a5671b49/) in the edX "Compilers" course.