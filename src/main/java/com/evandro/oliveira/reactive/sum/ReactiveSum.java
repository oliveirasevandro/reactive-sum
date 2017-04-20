package com.evandro.oliveira.reactive.sum;

import rx.Observable;
import rx.Observer;

/**
 * Created by eso on 20/04/17.
 */
public class ReactiveSum implements Observer<Double> {

    private double sum;

    public ReactiveSum(Observable<Double> a, Observable<Double> b) {

        this.sum = 0;
        Observable.combineLatest(a, b, (n1, n2) -> n1 + n2).subscribe(this);
    }

    @Override
    public void onCompleted() {
        System.out.println("Exiting last sum was : " + this.sum); // (4)
    }

    @Override
    public void onError(Throwable e) {
        System.err.println("Got an error!"); // (3)
        e.printStackTrace();
    }

    @Override
    public void onNext(Double sum) {
        this.sum = sum;
        System.out.println("update : a + b = " + sum); // (2)
    }

}
