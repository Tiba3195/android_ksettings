package com.khadas.util;

public class CoreSpec {
    public TonalSpec getA1() {
        return a1;
    }

    public TonalSpec getA2() {
        return a2;
    }

    public TonalSpec getA3() {
        return a3;
    }

    public TonalSpec getN1() {
        return n1;
    }

    public TonalSpec getN2() {
        return n2;
    }

    private final TonalSpec a1 ;
    private final TonalSpec a2 ;
    private final TonalSpec a3 ;
    private final TonalSpec n1 ;
    private final TonalSpec n2 ;

    public CoreSpec(TonalSpec a1, TonalSpec a2, TonalSpec a3, TonalSpec n1, TonalSpec n2) {
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.n1 = n1;
        this.n2 = n2;
    }
}
