package com.evandro.oliveira.reactive.sum;

import rx.Observable;
import rx.Subscriber;
import rx.observables.ConnectableObservable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Created by eso on 20/04/17.
 */
public class Application {

    public static void main(String[] args) {

        ConnectableObservable<String> input = from(System.in);

        Observable<Double> a = varStream("a", input);
        Observable<Double> b = varStream("b", input);

        ReactiveSum sum = new ReactiveSum(a, b);

        input.connect();
    }

    private static Observable<Double> varStream(String varName, ConnectableObservable<String> input) {

        final Pattern pattern = Pattern.compile(varName + "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$");

        return input.map(pattern::matcher)
                .filter(matcher -> matcher.matches() && matcher.group(1) != null)
                .map(matcher -> Double.parseDouble(matcher.group(1)));
    }

    private static ConnectableObservable<String> from(InputStream stream) {


        return from(new BufferedReader(new InputStreamReader(stream)));
    }

    private static ConnectableObservable<String> from(final BufferedReader reader) {

        return Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                try {
                    String line;

                    while (!subscriber.isUnsubscribed() &&
                            (line = reader.readLine()) != null) {

                        if (line == null || line.equalsIgnoreCase("exit")) {
                            break;
                        }

                        subscriber.onNext(line);
                    }
                } catch (IOException ex) {
                    subscriber.onError(ex);
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        }).publish();

    }


}
