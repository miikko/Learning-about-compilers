# Learning about compilers <!-- omit in toc -->

## Compiler structure <!-- omit in toc -->

- [**1. Lexical analysis**](#1-lexical-analysis)
- [**2. Parsing**](#2-parsing)
- [**3. Semantic analysis**](#3-semantic-analysis)
- [**4. Optimization**](#4-optimization)
- [**5. Code generation**](#5-code-generation)

### **1. Lexical analysis**

*divides source code into "tokens"*

```
Token == <class, lexeme>
```

Lexemes are the divided substrings of the original source code string.

Example:

`if x == y then z = 1; else z = 2;`

The above string is divided into tokens:

* Keyword: `if`, `then`, `else`
* Identifier: `x`, `y`, `z`
* Operator: `==`, `=`
* Number: `1`, `2`
* Punctuation: `;`
* Whitespace: ` `

The token class names listed above are not standardized. Compilers for different programming languages can classify tokens differently.

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