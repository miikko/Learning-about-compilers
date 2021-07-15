# Learning about compilers <!-- omit in toc -->

## Compiler structure <!-- omit in toc -->

- [**1. Lexical analysis**](#1-lexical-analysis)
  - [**Implementation**](#implementation)
- [**2. Parsing**](#2-parsing)
- [**3. Semantic analysis**](#3-semantic-analysis)
- [**4. Optimization**](#4-optimization)
- [**5. Code generation**](#5-code-generation)

### **1. Lexical analysis**

*divides source code into "tokens"*

```
Token == <class, lexeme>
```

Lexemes are substrings of the original source code string.

**Example: Extracting tokens from a source code string.**

```
if x == y then z = 1; else z = 2;
```

| Class       | Lexemes              |
|-------------| ---------------------|
| Keyword     | `if`, `then`, `else` |
| Identifier  | `x`, `y`, `z`        |
| Operator    | `==`, `=`            |
| Number      | `1`, `2`             |
| Punctuation | `;`                  |
| Whitespace  | ` `                  |

The token classes listed above are not standardized. During programming language design, designers typically use [regular languages](https://en.wikipedia.org/wiki/Regular_language) to define the token classes and what strings belong in each of them. Regular languages are typically defined using regular expressions. Therefore regular expressions can be used for extracting tokens from source code.

#### **Implementation**

1. Write a regular expression ***R<sub>i</sub>*** for each token class so that it matches each lexeme belonging to that class.
   - For example, for the Number class the following regular expression could be used: `"\d+"`
   - Make sure to assign a priority to each ***R<sub>i<sub>***. This will help in step 4, when classifying lexemes that match with multiple regular expressions.
2. Iterate the input string ***S = c<sub>0</sub>...c<sub>n</sub>***. For each ***c<sub>i</sub>*** check whether any ***R<sub>i</sub>*** matches it. In case of a match, go to step 3. If there are no matches, display an error message and exit the program.   
3. Scan the input string forward ***c<sub>i</sub>...c<sub>j</sub>***, until no ***R<sub>i</sub>*** matches the scanned part.
   - Having token classes with shared lexemes often means one cannot be certain of a lexeme's class based on the first character. For example, having `"[a-zA-Z]+"` as the regular expression for identifier tokens causes it to match with the first character of `"if"`, which is commonly classified as a keyword token.
4. Move the scanner back one index and store the lexeme together with its matching class to a token. If there are multiple matching classes, choose the one with the highest priority.
5. Loop back to step 2 and resume iteration of ***S*** from the last scanned character ***c<sub>j</sub>***.

An alternative to implementing the aforementioned steps is to use a tool that generates a lexical analysers. [Lex](https://en.wikipedia.org/wiki/Lex_(software)) is a popular tool used for generating lexical analysers (programs responsible for lexical analysis).

### **2. Parsing**

*forms a tree from "tokens" received from lexical analysis*

`if x == y then z = 1; else z = 2;`

![parse-tree](./imgs/parse-tree.PNG)

### **3. Semantic analysis**

*catches **inconsistencies** in source code*

Natural language example:

"Jack said Jerry left **his** assignment at home."

Does Jack talk about his own assignment or Jerry's assignment? This kind of ambiguity is not present in proper programming languages due to their rules:

```rust
fn main() {
    let person: &str = "Jack";
    {
        let person: &str = "Jerry";
        //Jerry's ...
        println!("{}'s assignment is at home.", person);
    }
    // Jack's ...
    println!("{}'s assignment is at home.", person);
}
```

### **4. Optimization**

*modifies a program so that it runs as efficiently as possible without compromising its original meaning or functionality*

Natural language example:

"It is a little bit like editing" -> "It is akin to editing".

Naive code example:

"X = Y * 0" -> "X = 0"

But what if Y is not an integer? Even if Y was a float, this would still be an invalid optimization because of [NaN](https://stackoverflow.com/questions/30242196/is-floating-point-multiplication-by-zero-guaranteed-to-produce-zero).

### **5. Code generation**

*translates input code into output code of a different language (typically assembly code)*

## Credits <!-- omit in toc -->

This README was written based on the Stanford undergraduate course "Compilers" lectures available at [edX](https://www.edx.org/course/compilers).