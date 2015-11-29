package cgeo.geocaching.utils;

import static org.assertj.core.api.Assertions.assertThat;

import junit.framework.TestCase;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class RxUtilsTest extends TestCase {

    @Test
    public static void testRememberLast() {
        final PublishSubject<String> rawObservable = PublishSubject.create();
        final Observable<String> observable = RxUtils.rememberLast(rawObservable, "initial");

        // Check that the initial value is present, and is kept there
        assertThat(observable.toBlocking().first()).isEqualTo("initial");
        assertThat(observable.toBlocking().first()).isEqualTo("initial");

        // Check that if the observable is not subscribed, changes are not propagated (similar to not keeping the
        // inner subscription active).
        rawObservable.onNext("without subscribers");
        assertThat(observable.toBlocking().first()).isEqualTo("initial");

        // Check that new values are propagated and cached
        final Subscription subscription = observable.subscribe();
        rawObservable.onNext("first");
        assertThat(observable.toBlocking().first()).isEqualTo("first");
        subscription.unsubscribe();
        assertThat(observable.toBlocking().first()).isEqualTo("first");
    }

}