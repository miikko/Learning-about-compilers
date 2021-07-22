class Main inherits IO {

    main() : Object {
        let conv: A2I <- new A2I in {
            out_string(conv.i2a(
                fact(conv.a2i(in_string()))
            ).concat("\n"));    
        }
    };

    fact(i: Int) : Int {
        (*
        Recursive
        if (i = 0) then 1 else i * fact(i-1) fi
        *)
        (* Iterative *)
        let fact: Int <- 1 in {
            while (not (i = 0)) loop
                {
                    fact <- fact * i;
                    i <- i - 1;
                }
            pool;
            fact;
        }
    };
};