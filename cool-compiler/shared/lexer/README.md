# Programming assignment #1 (Java edition)

This assignment involved writing a lexer specification [`cool.lex`](cool.lex) that the lexer generator JLex uses to generate the lexer `CoolLexer.java`. The assignment PDF and README can be accessed from the links in the [**Supporting resources**](#supporting-resources) section.

## Files

A bunch of premade files (mostly Java files) were provided. The purpose of each one of them did not become clear, though they are probably used throughout the assignments. The only files that were manually edited during this assignment were `cool.lex`, [test.cl](test.cl), [`Makefile`](Makefile) and [`pa1-grading.pl`](pa1-grading.pl). Completing the template in `cool.lex` was the objective of the assignment and `test.cl` had syntax errors that were fixed. The other two files were edited to fit the setup of using a Docker container as an execution environment.

## Completing the assignment

The assignment comes with a command-line based grading system. The grading is done by running the JLex generated lexer against several Cool source files and reviewing the lexer's output. The lexer specification in `cool.lex` can be graded by running the grading script:

```
$ perl pa1-grading.pl
```

This will create a `grading` folder containing the Cool source files used for testing along with complementary files showing the desired lexer output. The lexer specification in this repository got a full score (63 points) from the grading.

## Additional commands

Generate `CoolLexer.java` lexer from the specification in `cool.lex`:

```
$ make lexer
```

Parse `test.cl` Cool source file with the generated lexer:

```
$ ./lexer test.cl
```

Use generated lexer together with `parser`, `semant` and `codegen` to form a complete Cool compiler and test it:

```
$ ./mycoolc test.cl
$ spim test.s
```

## Supporting resources

* [**Assignment PDF**](https://courses.edx.org/assets/courseware/v1/00e29b916fa002225f3ab7590307d69c/asset-v1:StanfordOnline+SOE.YCSCS1+3T2020+type@asset+block/PA1.pdf)
* [**JLex manual**](https://www.cs.princeton.edu/~appel/modern/java/JLex/)
* [**COOL manual**](http://theory.stanford.edu/~aiken/software/cool/cool-manual.pdf)
* [**README that came with the assignment**](COURSE_README)
