# Programming assignment #2 (Java edition)

This assignment involved writing a parser specification [`cool.cup`](cool.cup) that the Java-based parser generator CUP uses to generate the parser `CoolParser.java`. The assignment PDF and README can be accessed from the links in the [**Supporting resources**](#supporting-resources) section.

## Files

A bunch of premade files (mostly Java files) were once again provided. The purpose of each one of them did not become clear, though they are probably used throughout the assignments. From the provided files, `cool.cup` and [`Makefile`](Makefile) were manually edited. Completing the template in `cool.cup` was the objective of the assignment. The `Makefile` was edited to fit the setup of using a Docker container as an execution environment.

## Completing the assignment

The assignment comes with a command-line based grading system. The grading is done by running the CUP generated parser against several Cool source files and reviewing the parser's output. The parser specification in `cool.cup` can be graded by running the grading script:

```
$ perl pa2-grading.pl
```

This will create a `grading` folder containing the Cool source files used for testing along with complementary files showing the desired parser output. The parser specification in this repository got a full score (70 points) from the grading.

## Additional commands

Generate `CoolParser.java` parser from the specification in `cool.cup`:

```
$ make parser
```

Generate symlinks to rest of the compiler parts and parse `test.cl` Cool source file with the generated parser:

```
$ make && ./myparser test.cl
```

Use generated parser together with `lexer`, `semant` and `codegen` to form a complete Cool compiler and test it:

```
$ ./mycoolc test.cl
$ spim test.s
```

## Supporting resources

* [**Assignment PDF**](https://courses.edx.org/assets/courseware/v1/4d4e0c23c74c47af7be1efdddfff4e0c/asset-v1:StanfordOnline+SOE.YCSCS1+3T2020+type@asset+block/PA2.pdf)
* [**CUP manual**](http://www2.cs.tum.edu/projects/cup/manual.html)
* [**COOL manual**](http://theory.stanford.edu/~aiken/software/cool/cool-manual.pdf)
* [**README that came with the assignment**](COURSE_README)
